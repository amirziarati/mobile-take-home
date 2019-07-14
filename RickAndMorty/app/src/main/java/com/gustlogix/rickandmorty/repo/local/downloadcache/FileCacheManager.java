package com.gustlogix.rickandmorty.repo.local.downloadcache;

import com.gustlogix.rickandmorty.repo.remote.RemoteRepositoryCallback;

public interface FileCacheManager {
    void cache(String url, String fileName);
    void retrieve(String url, RemoteRepositoryCallback<String> callback);
}
