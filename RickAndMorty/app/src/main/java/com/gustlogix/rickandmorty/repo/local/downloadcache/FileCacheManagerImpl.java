package com.gustlogix.rickandmorty.repo.local.downloadcache;

import android.util.Log;

import com.gustlogix.rickandmorty.repo.RepositoryCallback;

import java.io.File;
import java.util.List;

public class FileCacheManagerImpl implements FileCacheManager {

    FileCacheLocalRepo fileCacheLocalRepo;
    String cacheDirectoryPath;
    int cacheSizeInMegaBytes;

    public FileCacheManagerImpl(FileCacheLocalRepo fileCacheLocalRepo, String cacheDirectoryPath, int cacheSizeInMegaBytes) {
        this.fileCacheLocalRepo = fileCacheLocalRepo;
        this.cacheDirectoryPath = cacheDirectoryPath;
        this.cacheSizeInMegaBytes = cacheSizeInMegaBytes;
    }

    public void cache(String url, String fileName) {
        if (new File(fileName).exists()) {
            fileCacheLocalRepo.insert(new FileCacheEntry(url, fileName, System.currentTimeMillis()));
            if (getFolderSizeInBytes(new File(cacheDirectoryPath)) > cacheSizeInMegaBytes * 1_000_000) {
                removeCacheByPercentageOf(30);
            }
        }
    }

    public void retrieve(String url, final RepositoryCallback<String> callback) {
        fileCacheLocalRepo.retrieve(url, new RepositoryCallback<FileCacheEntry>() {
            @Override
            public void onSuccess(FileCacheEntry fileCacheEntry) {
                fileCacheEntry.setLastRetrievedTimeStamp(System.currentTimeMillis());
                fileCacheLocalRepo.update(fileCacheEntry);
                callback.onSuccess(fileCacheEntry.getFileName());
            }

            @Override
            public void onError(Exception e) {
                callback.onError(e);
            }
        });
    }


    private void removeCacheByPercentageOf(final int percent) {
        fileCacheLocalRepo.retrieveAllSortedByLastRetrieved(new RepositoryCallback<List<FileCacheEntry>>() {
            @Override
            public void onSuccess(List<FileCacheEntry> fileCacheEntries) {
                try {
                    int cacheSizeAfterRemove = cacheSizeInMegaBytes * 1_000_000 * ((100 - percent) / 100);
                    while (getFolderSizeInBytes(new File(cacheDirectoryPath)) > cacheSizeAfterRemove) {
                        removeFile(fileCacheEntries.get(fileCacheEntries.size() - 1).getFileName());
                        fileCacheLocalRepo.remove(fileCacheEntries.get(fileCacheEntries.size() - 1));
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
