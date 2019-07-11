package com.gustlogix.rickandmorty.repo.remote.network;

public interface NetworkService<T> {

    void call(NetworkRequest networkRequest, NetworkServiceCallback<T> networkServiceCallback);
}
