package com.gustlogix.rickandmorty.repo.local.downloadcache;

public class CacheEntryNotFoundException extends Exception {
    CacheEntryNotFoundException()
    {
        super("Cache Entry Was Not Found");
    }
}
