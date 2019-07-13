package com.gustlogix.rickandmorty.repo.remote.imagedownloader;

import android.Manifest;
import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;

import com.gustlogix.rickandmorty.repo.RepositoryCallback;
import com.gustlogix.rickandmorty.repo.local.downloadcache.CacheEntryNotFoundException;
import com.gustlogix.rickandmorty.repo.local.downloadcache.FileCacheManager;

import java.io.File;
import java.util.HashMap;

import static android.content.Context.DOWNLOAD_SERVICE;

public class DownloadHelperImpl implements DownloadHelper {

    private String downloadPath;
    FileCacheManager fileCacheManager;
    HashMap<Long, DownloadInfo> mapIdDownloadInfo = new HashMap<>();
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
    public void download(Context context, final String url, final DownloadManagerCallback callback) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (context.checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                callback.onFail(new SecurityException("write external memory not granted"));
                return;
            }
        }

        final DownloadManager downloadManager = (DownloadManager) context.getSystemService(DOWNLOAD_SERVICE);
        try {
            context.registerReceiver(onDownloadComplete, new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));
        } catch (IllegalArgumentException e) {

            e.printStackTrace();
        }

        fileCacheManager.retrieve(url, new RepositoryCallback<String>() {
            @Override
            public void onSuccess(String fileName) {
                callback.onDone(fileName);
            }

            @Override
            public void onError(Exception e) {
                if (e instanceof CacheEntryNotFoundException) {
                    try {
                        String fileName = new File(downloadPath, System.currentTimeMillis() + "").getAbsolutePath();

                        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(url))
                                .setNotificationVisibility(DownloadManager.Request.VISIBILITY_HIDDEN)
                                .setDestinationUri(Uri.fromFile(new File(fileName)));

                        long downloadID = downloadManager.enqueue(request);
                        mapIdDownloadInfo.put(downloadID, new DownloadInfo(downloadID, callback, fileName, url));
                    } catch (Exception ex) {
                        callback.onFail(ex);
                    }
                } else {
                    callback.onFail(e);
                }
            }
        });


    }

    private void StopDownloadService(Context context) {
        try {
            context.unregisterReceiver(onDownloadComplete);
        } catch (Exception e) {
        }
    }

    private BroadcastReceiver onDownloadComplete = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            long id = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1);
            DownloadInfo downloadInfo = mapIdDownloadInfo.get(id);

            DownloadManager.Query query = new DownloadManager.Query();
            query.setFilterById(id);
            DownloadManager manager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
            Cursor cursor = manager.query(query);
            if (cursor.moveToFirst()) {
                if (cursor.getCount() > 0) {
                    int status = cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_STATUS));
                    if (status == DownloadManager.STATUS_SUCCESSFUL) {
                        fileCacheManager.cache(downloadInfo.getUrl(), downloadInfo.fileName);
                        mapIdDownloadInfo.remove(id);
                        downloadInfo.callback.onDone(downloadInfo.fileName);
                    } else {
                        int message = cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_REASON));
                        downloadInfo.callback.onFail(new Exception("download failed : " + message));
                    }
                }
            }

        }
    };

    class DownloadInfo {
        long id;
        DownloadManagerCallback callback;
        String fileName;
        String url;

        public DownloadInfo(long id, DownloadManagerCallback callback, String fileName, String url) {
            this.id = id;
            this.callback = callback;
            this.fileName = fileName;
            this.url = url;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public long getId() {
            return id;
        }

        public void setId(long id) {
            this.id = id;
        }

        public DownloadManagerCallback getCallback() {
            return callback;
        }

        public void setCallback(DownloadManagerCallback callback) {
            this.callback = callback;
        }

        public String getFileName() {
            return fileName;
        }

        public void setFileName(String fileName) {
            this.fileName = fileName;
        }
    }
}
