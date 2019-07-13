package com.gustlogix.rickandmorty.repo.remote.imagedownloader;

import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;

import com.gustlogix.rickandmorty.repo.RepositoryCallback;
import com.gustlogix.rickandmorty.repo.local.downloadcache.CacheEntryNotFoundException;
import com.gustlogix.rickandmorty.repo.local.downloadcache.FileCacheManager;

import java.io.File;
import java.util.HashMap;

import static android.content.Context.DOWNLOAD_SERVICE;

public class DownloadHelperImpl implements DownloadHelper {

    private Context context;
    private String downloadPath;
    private DownloadManager downloadManager;
    FileCacheManager fileCacheManager;
    HashMap<Long, DownloadInfo> mapIdDownloadInfo = new HashMap<>();
    private static DownloadHelperImpl instance;

    private DownloadHelperImpl(Context context, String downloadPath, FileCacheManager fileCacheManager) {
        this.context = context;
        this.downloadPath = downloadPath;
        this.fileCacheManager = fileCacheManager;
        downloadManager = (DownloadManager) context.getSystemService(DOWNLOAD_SERVICE);
    }

    public static DownloadHelperImpl getInstance(Context context, String downloadPath, FileCacheManager fileCacheManager) {
        if (instance == null)
            instance = new DownloadHelperImpl(context, downloadPath, fileCacheManager);
        return instance;
    }

    @Override
    public void download(final String url, final DownloadManagerCallback callback) {
        fileCacheManager.retrieve(url, new RepositoryCallback<String>() {
            @Override
            public void onSuccess(String fileName) {
                callback.onDone(fileName);
            }

            @Override
            public void onError(Exception e) {
                if (e instanceof CacheEntryNotFoundException) {
                    try {
                        String fileName = downloadPath + System.currentTimeMillis();

                        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(url))
                                .setNotificationVisibility(DownloadManager.Request.VISIBILITY_HIDDEN)
                                .setDestinationUri(Uri.fromFile(new File(fileName)));

                        long downloadID = downloadManager.enqueue(request);
                        mapIdDownloadInfo.put(downloadID, new DownloadInfo(downloadID, callback, fileName, url));
                    } catch (Exception ex) {
                        callback.onFail(e);
                    }
                } else {
                    callback.onFail(e);
                }
            }
        });


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
                        String file = cursor.getString(cursor.getColumnIndex(DownloadManager.COLUMN_LOCAL_FILENAME));
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
