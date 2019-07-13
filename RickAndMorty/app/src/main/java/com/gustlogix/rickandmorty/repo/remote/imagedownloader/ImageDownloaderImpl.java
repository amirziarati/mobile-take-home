package com.gustlogix.rickandmorty.repo.remote.imagedownloader;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.gustlogix.rickandmorty.repo.RepositoryCallback;
import com.gustlogix.rickandmorty.repo.local.downloadcache.CacheEntryNotFoundException;
import com.gustlogix.rickandmorty.repo.local.downloadcache.FileCacheManager;

public class ImageDownloaderImpl implements ImageDownloader {

    DownloadHelper downloadHelper;
    FileCacheManager fileCacheManager;

    public ImageDownloaderImpl(DownloadHelper downloadHelper,
                               FileCacheManager fileCacheManager) {
        this.downloadHelper = downloadHelper;
        this.fileCacheManager = fileCacheManager;
    }

    @Override
    public void loadImage(final String url, final ImageView imageView, final ProgressBar progressBar) {
        fileCacheManager.retrieve(url, new RepositoryCallback<String>() {
            @Override
            public void onSuccess(String bmpFileAddress) {
                showFileInView(bmpFileAddress, imageView, progressBar);
            }

            @Override
            public void onError(Exception e) {
                if (e instanceof CacheEntryNotFoundException) {
                    downloadAndSaveImage(url, new DownloadManagerCallback() {
                        @Override
                        public void onDone(String fileName) {
                            fileCacheManager.cache(url, fileName);
                            showFileInView(fileName, imageView, progressBar);
                        }

                        @Override
                        public void onFail(Exception e) {
                            imageView.setImageBitmap(null);
                            if (progressBar != null)
                                progressBar.setVisibility(View.GONE);
                        }
                    });
                }
            }
        });

    }

    private void showFileInView(String bmpFileAddress, ImageView imageView, ProgressBar progressBar) {
        Bitmap bmp = getScaleDownBitmap(bmpFileAddress, imageView.getWidth(), imageView.getHeight());
        imageView.setImageBitmap(bmp);
        if (progressBar != null)
            progressBar.setVisibility(View.GONE);
    }

    private Bitmap getScaleDownBitmap(String bmpFileAddress, int reqWidth, int reqHeight) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(bmpFileAddress, options);
        int height = options.outHeight;
        int width = options.outWidth;
        String imageType = options.outMimeType;

        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width.
            while ((halfHeight / inSampleSize) >= reqHeight
                    && (halfWidth / inSampleSize) >= reqWidth) {
                inSampleSize *= 2;
            }
        }

        options.inSampleSize = inSampleSize;
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeFile(bmpFileAddress, options);
    }

    private void downloadAndSaveImage(String url, DownloadManagerCallback callback) {
        if (url.trim().isEmpty())
            callback.onFail(new IllegalArgumentException("url is invalid"));

        downloadHelper.download(url, callback);
    }

    @Override
    public void loadImage(String url, ImageView imageView) {
        loadImage(url, imageView, null);
    }
}
