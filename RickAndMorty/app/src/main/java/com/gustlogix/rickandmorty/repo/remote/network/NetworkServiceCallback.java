package com.gustlogix.rickandmorty.repo.remote.network;

public interface NetworkServiceCallback<T> {

    void onResponse(T response);

    void onError(Exception e);
}
