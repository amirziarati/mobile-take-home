package com.gustlogix.rickandmorty.repo.local.downloadcache;

public class CacheEntryNotFoundException extends Exception {
    public CacheEntryNotFoundException()
    {
        super("Cache Entry Was Not Found");
    }
}
