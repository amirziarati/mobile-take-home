package com.gustlogix.rickandmorty.repo.local.downloadcache;

import com.gustlogix.rickandmorty.repo.RepositoryCallback;

import java.util.List;

public interface FileCacheLocalRepo {
    void insert(FileCacheEntry fileCacheEntry);
    void update(FileCacheEntry fileCacheEntry);
    void remove(FileCacheEntry fileCacheEntry);
    FileCacheEntry retrieve(String url, RepositoryCallback<FileCacheEntry> callback);
    List<FileCacheEntry> retrieveAllSortedByLastRetrieved(RepositoryCallback<List<FileCacheEntry>> callback);
}
