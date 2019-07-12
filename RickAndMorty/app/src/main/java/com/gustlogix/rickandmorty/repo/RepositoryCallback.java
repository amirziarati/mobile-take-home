package com.gustlogix.rickandmorty.repo;

public interface RepositoryCallback<T> {

    void onSuccess(T data);

    void onError(Exception e);
}
