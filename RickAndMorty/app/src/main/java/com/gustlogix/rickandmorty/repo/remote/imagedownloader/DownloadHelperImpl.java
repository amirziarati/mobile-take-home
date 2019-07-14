package com.gustlogix.rickandmorty.repo.remote.imagedownloader;

import android.content.Context;
import android.os.AsyncTask;

import com.gustlogix.rickandmorty.repo.RepositoryCallback;
import com.gustlogix.rickandmorty.repo.local.downloadcache.CacheEntryNotFoundException;
import com.gustlogix.rickandmorty.repo.local.downloadcache.FileCacheManager;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

public class DownloadHelperImpl implements DownloadHelper {

    private String downloadPath;
    FileCacheManager fileCacheManager;
    HashMap<String, DownloadInfo> mapFileNameDownloadingFileInfo = new HashMap<>();
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
    public void download(Context context, final String urlString, final DownloadManagerCallback callback) {

        if (mapFileNameDownloadingFileInfo.containsKey(urlString)) {
            //file is already downloading
            mapFileNameDownloadingFileInfo.get(urlString).addCallback(callback);
        }

        fileCacheManager.retrieve(urlString, new RepositoryCallback<String>() {
            @Override
            public void onSuccess(String fileName) {
                callback.onDone(fileName);
            }

            @Override
            public void onError(Exception e) {
                if (e instanceof CacheEntryNotFoundException) {
                    String fileName = downloadPath + "/" + System.currentTimeMillis();
                    DownloadInfo downloadInfo = new DownloadInfo(callback, fileName, urlString);
                    new DownloadTask().execute(downloadInfo);
                } else {
                    callback.onFail(e);
                }
            }
        });
    }

    private class DownloadTask extends AsyncTask<DownloadInfo, Void, DownloadResult> {
        @Override
        protected DownloadResult doInBackground(DownloadInfo... downloadInfos) {
            DownloadInfo downloadInfo = downloadInfos[0];
            try {
                URL url = new URL(downloadInfo.getUrl());
                InputStream in = null;
                in = url.openStream();
                File file = new File(downloadInfo.fileName);
                mapFileNameDownloadingFileInfo.put(downloadInfo.getUrl(), downloadInfo);
                FileOutputStream fos = new FileOutputStream(file);

                System.out.println("reading from resource and writing to file...");
                int length = -1;
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

        @Override
        protected void onPostExecute(DownloadResult downloadResult) {
            super.onPostExecute(downloadResult);
            DownloadInfo downloadInfo = downloadResult.getDownloadInfo();
            if (downloadResult.exception != null) {
                for (DownloadManagerCallback callbackEntry : downloadInfo.getCallbacks()) {
                    callbackEntry.onFail(downloadResult.getException());
                }
            } else {
                fileCacheManager.cache(downloadInfo.getUrl(), downloadInfo.fileName);
                for (DownloadManagerCallback callbackEntry : downloadInfo.getCallbacks()) {
                    callbackEntry.onDone(downloadInfo.fileName);
                }
            }
            mapFileNameDownloadingFileInfo.remove(downloadInfo.getUrl());
        }
    }

    class DownloadResult {
        private DownloadInfo downloadInfo;
        private Exception exception;

        public DownloadResult(DownloadInfo downloadInfo, Exception exception) {
            this.downloadInfo = downloadInfo;
            this.exception = exception;
        }

        public DownloadInfo getDownloadInfo() {
            return downloadInfo;
        }

        public void setDownloadInfo(DownloadInfo downloadInfo) {
            this.downloadInfo = downloadInfo;
        }

        public Exception getException() {
            return exception;
        }

        public void setException(Exception exception) {
            this.exception = exception;
        }
    }

    class DownloadInfo {
        private ArrayList<DownloadManagerCallback> callbacks = new ArrayList<>();
        private String fileName;
        private String url;

        public DownloadInfo(DownloadManagerCallback callback, String fileName, String url) {
            this.callbacks.add(callback);
            this.fileName = fileName;
            this.url = url;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public ArrayList<DownloadManagerCallback> getCallbacks() {
            return callbacks;
        }

        public void addCallback(DownloadManagerCallback callback) {
            this.callbacks.add(callback);
        }

        public String getFileName() {
            return fileName;
        }

        public void setFileName(String fileName) {
            this.fileName = fileName;
        }
    }
}
