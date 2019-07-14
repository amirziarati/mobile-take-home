package com.gustlogix.rickandmorty.repo.local;

public interface LocalRepositoryCallback<T> {

    void onSuccess(T data);

    void onError(Exception e);
}
