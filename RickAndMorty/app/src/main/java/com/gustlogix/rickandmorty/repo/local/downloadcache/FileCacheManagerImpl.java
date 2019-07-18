package com.gustlogix.rickandmorty.repo.local.downloadcache;

import android.util.Log;

import com.gustlogix.rickandmorty.repo.remote.RemoteRepositoryCallback;

import java.io.File;
import java.util.List;

public class FileCacheManagerImpl implements FileCacheManager {

    private FileCacheLocalService fileCacheLocalService;
    private String cacheDirectoryPath;
    private int cacheSizeInMegaBytes;
    private int cacheThresholdPercent;

    public FileCacheManagerImpl(FileCacheLocalService fileCacheLocalService, String cacheDirectoryPath, int cacheSizeInMegaBytes, int cacheThresholdPercent) {
        this.fileCacheLocalService = fileCacheLocalService;
        this.cacheDirectoryPath = cacheDirectoryPath;
        this.cacheSizeInMegaBytes = cacheSizeInMegaBytes;
        this.cacheThresholdPercent = cacheThresholdPercent;
    }

    public void cache(String url, String fileName) {
        if (new File(fileName).exists()) {
            fileCacheLocalService.insertOrUpdate(new FileCacheEntry(url, fileName, System.currentTimeMillis()));
            if (getFolderSizeInBytes(new File(cacheDirectoryPath)) > cacheSizeInMegaBytes * 1_000_000) {
                removeCacheByPercentageOf(cacheThresholdPercent);
            }
        }
    }

    public void retrieve(String url, final RemoteRepositoryCallback<String> callback) {
        fileCacheLocalService.retrieve(url, new RemoteRepositoryCallback<FileCacheEntry>() {
            @Override
            public void onSuccess(FileCacheEntry fileCacheEntry) {
                if (fileCacheEntry == null) {
                    onError(new CacheEntryNotFoundException());
                    return;
                }
                fileCacheEntry.setLastRetrievedTimeStamp(System.currentTimeMillis());
                fileCacheLocalService.insertOrUpdate(fileCacheEntry);
                callback.onSuccess(fileCacheEntry.getFileName());
            }

            @Override
            public void onError(Exception e) {
                callback.onError(e);
            }
        });
    }


    private void removeCacheByPercentageOf(final int percent) {
        fileCacheLocalService.retrieveAllSortedByLastRetrieved(new RemoteRepositoryCallback<List<FileCacheEntry>>() {
            @Override
            public void onSuccess(List<FileCacheEntry> fileCacheEntries) {
                try {
                    int cacheSizeAfterRemove = cacheSizeInMegaBytes * 1_000_000 * ((100 - percent) / 100);
                    while (getFolderSizeInBytes(new File(cacheDirectoryPath)) > cacheSizeAfterRemove) {
                        removeFile(fileCacheEntries.get(fileCacheEntries.size() - 1).getFileName());
                        fileCacheLocalService.remove(fileCacheEntries.get(fileCacheEntries.size() - 1));
                        fileCacheEntries.remove(fileCacheEntries.size() - 1);
                    }
                } catch (Exception ex) {
                    Log.w("R&M", "removing cache files stopped with an error: " + ex.toString());
                }
            }

            @Override
            public void onError(Exception ex) {
                Log.w("R&M", "removing cache files stopped with an error: " + ex.toString());
            }
        });
    }

    private void removeFile(String fileName) {
        File file = new File(fileName);
        if (file.exists()) {
            file.delete();
        }
    }

    private long getFolderSizeInBytes(File directory) {
        long length = 0;
        File[] files = directory.listFiles();
        for (File file : files) {
            if (file.isFile())
                length += file.length();
            else
                length += getFolderSizeInBytes(file);
        }
        return length;
    }
}
