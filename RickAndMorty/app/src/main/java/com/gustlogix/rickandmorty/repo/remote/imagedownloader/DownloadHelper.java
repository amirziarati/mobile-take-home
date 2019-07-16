package com.gustlogix.rickandmorty.repo.remote.imagedownloader;

public interface DownloadHelper {
    void download(String url, DownloadManagerCallback callback);
}
