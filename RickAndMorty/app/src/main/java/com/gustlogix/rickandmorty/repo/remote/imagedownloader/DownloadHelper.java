package com.gustlogix.rickandmorty.repo.remote.imagedownloader;

import android.content.Context;

public interface DownloadHelper {
    void download(Context context, String url, DownloadManagerCallback callback);
}
