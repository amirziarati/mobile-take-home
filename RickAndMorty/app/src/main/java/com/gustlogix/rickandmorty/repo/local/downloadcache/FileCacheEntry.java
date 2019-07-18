package com.gustlogix.rickandmorty.repo.local.downloadcache;

public class FileCacheEntry {

    public FileCacheEntry(String url,
                          String fileName,
                          long lastRetrievedTimeStamp) {
        this.url = url;
        this.fileName = fileName;
        this.lastRetrievedTimeStamp = lastRetrievedTimeStamp;
    }

    private String url;
    private String fileName;
    private long lastRetrievedTimeStamp;

    public String getUrl() {
        return url;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public long getLastRetrievedTimeStamp() {
        return lastRetrievedTimeStamp;
    }

    void setLastRetrievedTimeStamp(long lastRetrievedTimeStamp) {
        this.lastRetrievedTimeStamp = lastRetrievedTimeStamp;
    }
}
