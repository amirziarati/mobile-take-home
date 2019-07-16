package com.gustlogix.rickandmorty.repo.remote.imagedownloader;

import android.widget.ImageView;
import android.widget.ProgressBar;

public interface ImageDownloader {
    void loadImage(String url, ImageView imageView, ProgressBar progressBar);

    void loadImage(String url, ImageView imageView);
}
