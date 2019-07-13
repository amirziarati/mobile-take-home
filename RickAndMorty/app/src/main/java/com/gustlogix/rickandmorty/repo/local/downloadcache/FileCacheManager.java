package com.gustlogix.rickandmorty.repo.local.downloadcache;

import com.gustlogix.rickandmorty.repo.RepositoryCallback;

public interface FileCacheManager {
    void cache(String url, String fileName);
    void retrieve(String url, RepositoryCallback<String> callback);
}
