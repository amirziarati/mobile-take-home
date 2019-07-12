package com.gustlogix.rickandmorty.repo.remote.imagedownloader;

public interface DownloadManagerCallback {

    void onDone(String fileName);

    void onFail(Exception e);
}
