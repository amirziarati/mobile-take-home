package com.gustlogix.rickandmorty.repo.remote;

public interface RemoteRepositoryCallback<T> {

    void onSuccess(T data);

    void onError(Exception e);
}
