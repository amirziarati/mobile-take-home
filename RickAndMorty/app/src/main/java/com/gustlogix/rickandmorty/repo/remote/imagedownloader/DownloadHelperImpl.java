package com.gustlogix.rickandmorty.repo.remote.imagedownloader;

import com.gustlogix.rickandmorty.repo.local.downloadcache.CacheEntryNotFoundException;
import com.gustlogix.rickandmorty.repo.local.downloadcache.FileCacheManager;
import com.gustlogix.rickandmorty.repo.remote.RemoteRepositoryCallback;
import com.gustlogix.rickandmorty.thread.ApplicationThreadPool;
import com.gustlogix.rickandmorty.thread.Task;
import com.gustlogix.rickandmorty.thread.TaskCallback;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

public class DownloadHelperImpl implements DownloadHelper {

    private String downloadPath;
    private FileCacheManager fileCacheManager;
    private HashMap<String, DownloadInfo> mapFileNameDownloadingFileInfo = new HashMap<>();
    private static DownloadHelperImpl instance;

    private DownloadHelperImpl(String downloadPath, FileCacheManager fileCacheManager) {
        this.downloadPath = downloadPath;
        this.fileCacheManager = fileCacheManager;
    }

    public static DownloadHelperImpl getInstance(String downloadPath, FileCacheManager fileCacheManager) {
        if (instance == null)
            instance = new DownloadHelperImpl(downloadPath, fileCacheManager);
        return instance;
    }

    @Override
    public void download(final String urlString, final DownloadManagerCallback callback) {

        if (mapFileNameDownloadingFileInfo.containsKey(urlString)) {
            //file is already downloading
            mapFileNameDownloadingFileInfo.get(urlString).addCallback(callback);
        }

        fileCacheManager.retrieve(urlString, new RemoteRepositoryCallback<String>() {
            @Override
            public void onSuccess(String fileName) {
                callback.onDone(fileName);
            }

            @Override
            public void onError(final Exception e) {
                if (e instanceof CacheEntryNotFoundException) {
                    if (!new File(downloadPath).exists()) {
                        new File(downloadPath).mkdirs();
                    }
                    String fileName = downloadPath + "/" + System.currentTimeMillis() + "_" + urlString.hashCode();
                    final DownloadInfo downloadInfo = new DownloadInfo(callback, fileName, urlString);

                    downloadInBackgroundThreadAndReturnResultToCallback(downloadInfo);

                } else {
                    callback.onFail(e);
                }
            }

        });
    }

    private void downloadInBackgroundThreadAndReturnResultToCallback(final DownloadInfo downloadInfo) {

        ApplicationThreadPool.execute(new Task<DownloadResult>() {
            @Override
            public DownloadResult execute() {
                return downloadFile(downloadInfo);
            }
        }, new TaskCallback<DownloadResult>() {
            @Override
            public void onResult(DownloadResult downloadResult) {
                DownloadInfo downloadInfo = downloadResult.getDownloadInfo();
                if (downloadResult.exception != null) {
                    onError(downloadResult.exception);
                } else {
                    fileCacheManager.cache(downloadInfo.getUrl(), downloadInfo.fileName);
                    for (DownloadManagerCallback callbackEntry : downloadInfo.getCallbacks()) {
                        callbackEntry.onDone(downloadInfo.fileName);
                    }
                    mapFileNameDownloadingFileInfo.remove(downloadInfo.getUrl());
                }
            }

            @Override
            public void onError(Exception e) {
                for (DownloadManagerCallback callbackEntry : downloadInfo.getCallbacks()) {
                    callbackEntry.onFail(e);
                }
                mapFileNameDownloadingFileInfo.remove(downloadInfo.getUrl());
            }
        });
    }


    private DownloadResult downloadFile(DownloadInfo downloadInfo) {
        try {
            URL url = new URL(downloadInfo.getUrl());
            InputStream in;
            in = url.openStream();
            File file = new File(downloadInfo.fileName);
            mapFileNameDownloadingFileInfo.put(downloadInfo.getUrl(), downloadInfo);
            FileOutputStream fos = new FileOutputStream(file);

            System.out.println("reading from resource and writing to file...");
            int length;
            byte[] buffer = new byte[1024];// buffer for portion of data from connection
            while ((length = in.read(buffer)) > -1) {
                fos.write(buffer, 0, length);
            }
            fos.close();
            in.close();
            System.out.println("File downloaded");
            return new DownloadResult(downloadInfo, null);
        } catch (IOException e) {
            e.printStackTrace();
            return new DownloadResult(downloadInfo, e);
        }
    }

    class DownloadResult {
        private DownloadInfo downloadInfo;
        private Exception exception;

        DownloadResult(DownloadInfo downloadInfo, Exception exception) {
            this.downloadInfo = downloadInfo;
            this.exception = exception;
        }

        DownloadInfo getDownloadInfo() {
            return downloadInfo;
        }

        void setDownloadInfo(DownloadInfo downloadInfo) {
            this.downloadInfo = downloadInfo;
        }

        Exception getException() {
            return exception;
        }

        void setException(Exception exception) {
            this.exception = exception;
        }
    }

    class DownloadInfo {
        private ArrayList<DownloadManagerCallback> callbacks = new ArrayList<>();
        private String fileName;
        private String url;

        DownloadInfo(DownloadManagerCallback callback, String fileName, String url) {
            this.callbacks.add(callback);
            this.fileName = fileName;
            this.url = url;
        }

        String getUrl() {
            return url;
        }

        void setUrl(String url) {
            this.url = url;
        }

        ArrayList<DownloadManagerCallback> getCallbacks() {
            return callbacks;
        }

        void addCallback(DownloadManagerCallback callback) {
            this.callbacks.add(callback);
        }

        String getFileName() {
            return fileName;
        }

        void setFileName(String fileName) {
            this.fileName = fileName;
        }
    }
}
