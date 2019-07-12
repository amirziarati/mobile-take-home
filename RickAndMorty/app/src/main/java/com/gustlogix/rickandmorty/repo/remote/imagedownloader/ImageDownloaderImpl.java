package com.gustlogix.rickandmorty.repo.remote.imagedownloader;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;

import java.util.HashMap;

public class ImageDownloaderImpl implements ImageDownloader {

    //TODO: persis cache in DB
    //TODO: create LRU cache manager to limit the cache size
    HashMap<String, String> mapUrlFileNameCache = new HashMap<>();
    DownloadHelper downloadHelper;

    public ImageDownloaderImpl(DownloadHelper downloadHelper) {
        this.downloadHelper = downloadHelper;
    }

    @Override
    public void loadImage(final String url, final ImageView imageView, final ProgressBar progressBar) {
        if (mapUrlFileNameCache.containsKey(url)) {
            String bmpFileAddress = mapUrlFileNameCache.get(url);
            showFileInView(bmpFileAddress, imageView, progressBar);
        } else {
            downloadAndSaveImage(url, new DownloadManagerCallback() {
                @Override
                public void onDone(String fileName) {
                    if (!mapUrlFileNameCache.containsKey(url))
                        mapUrlFileNameCache.put(url, fileName);
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
