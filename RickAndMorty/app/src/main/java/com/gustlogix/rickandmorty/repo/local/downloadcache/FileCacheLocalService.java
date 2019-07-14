package com.gustlogix.rickandmorty.repo.local.downloadcache;

import com.gustlogix.rickandmorty.repo.remote.RemoteRepositoryCallback;

import java.util.List;

public interface FileCacheLocalService {
    void insert(FileCacheEntry fileCacheEntry);
    void update(FileCacheEntry fileCacheEntry);
    void remove(FileCacheEntry fileCacheEntry);
    void retrieve(String url, RemoteRepositoryCallback<FileCacheEntry> callback);
    void retrieveAllSortedByLastRetrieved(RemoteRepositoryCallback<List<FileCacheEntry>> callback);
}
