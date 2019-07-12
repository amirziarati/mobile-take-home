package com.gustlogix.rickandmorty.repo.remote.imagedownloader;

public interface DownloadHelper {
    public void download(String url, DownloadManagerCallback callback);
}
