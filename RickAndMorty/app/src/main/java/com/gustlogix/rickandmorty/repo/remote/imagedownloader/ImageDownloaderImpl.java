package com.gustlogix.rickandmorty.repo.remote.imagedownloader;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;

public class ImageDownloaderImpl implements ImageDownloader {

    private DownloadHelper downloadHelper;

    public ImageDownloaderImpl(DownloadHelper downloadHelper) {
        this.downloadHelper = downloadHelper;
    }

    @Override
    public void loadImage(Context context, final String url, final ImageView imageView, final ProgressBar progressBar) {
        if (url.trim().isEmpty()) {
            showResultImageInView(imageView, progressBar, null);
        }

        downloadHelper.download(context, url, new DownloadManagerCallback() {
            @Override
            public void onDone(String fileName) {
                showFileInView(fileName, imageView, progressBar);
            }

            @Override
            public void onFail(Exception e) {
                showResultImageInView(imageView, progressBar, null);
            }
        });
    }

    @Override
    public void loadImage(Context context, String url, ImageView imageView) {
        loadImage(context, url, imageView, null);
    }

    private void showFileInView(String bmpFileAddress, ImageView imageView, ProgressBar progressBar) {
        Bitmap bmp = getScaleDownBitmap(bmpFileAddress, imageView.getWidth(), imageView.getHeight());
        showResultImageInView(imageView, progressBar, bmp);
    }

    private void showResultImageInView(ImageView imageView, ProgressBar progressBar, Bitmap o) {
        imageView.setImageBitmap(o);
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
}
