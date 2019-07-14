package com.gustlogix.rickandmorty.repo.local.downloadcache;

import com.gustlogix.rickandmorty.repo.local.db.DbHelper;
import com.gustlogix.rickandmorty.repo.remote.RemoteRepositoryCallback;
import com.gustlogix.rickandmorty.thread.ApplicationThreadPool;
import com.gustlogix.rickandmorty.thread.Task;
import com.gustlogix.rickandmorty.thread.TaskCallback;

import java.util.List;

public class FileCacheLocalServiceImpl implements FileCacheLocalService {

    DbHelper dbHelper;

    public FileCacheLocalServiceImpl(DbHelper dbHelper) {
        this.dbHelper = dbHelper;
    }

    @Override
    public void insertOrUpdate(final FileCacheEntry fileCacheEntry) {
        ApplicationThreadPool.execute(new Task<Void>() {
            @Override
            public Void execute() {
                dbHelper.insertFileCache(fileCacheEntry);
                return null;
            }
        }, new TaskCallback<Void>() {
            @Override
            public void onResult(Void result) {

            }

            @Override
            public void onError(Exception e) {

            }
        });
    }

    @Override
    public void remove(final FileCacheEntry fileCacheEntry) {
        ApplicationThreadPool.execute(new Task<Void>() {
            @Override
            public Void execute() {
                dbHelper.removeFileCache(fileCacheEntry);
                return null;
            }
        }, new TaskCallback<Void>() {
            @Override
            public void onResult(Void result) {

            }

            @Override
            public void onError(Exception e) {

            }
        });
    }

    @Override
    public void retrieve(final String url, final RemoteRepositoryCallback<FileCacheEntry> callback) {
        ApplicationThreadPool.execute(new Task<FileCacheEntry>() {
            @Override
            public FileCacheEntry execute() {
                return dbHelper.fetchFileCache(url);
            }
        }, new TaskCallback<FileCacheEntry>() {
            @Override
            public void onResult(FileCacheEntry result) {
                callback.onSuccess(result);
            }

            @Override
            public void onError(Exception e) {
                callback.onError(e);
            }
        });
    }

    @Override
    public void retrieveAllSortedByLastRetrieved(final RemoteRepositoryCallback<List<FileCacheEntry>> callback) {
        ApplicationThreadPool.execute(new Task<List<FileCacheEntry>>() {
            @Override
            public List<FileCacheEntry> execute() {
                return dbHelper.fetchFileCaches();
            }
        }, new TaskCallback<List<FileCacheEntry>>() {
            @Override
            public void onResult(List<FileCacheEntry> result) {
                callback.onSuccess(result);
            }

            @Override
            public void onError(Exception e) {
                callback.onError(e);
            }
        });
    }
}
