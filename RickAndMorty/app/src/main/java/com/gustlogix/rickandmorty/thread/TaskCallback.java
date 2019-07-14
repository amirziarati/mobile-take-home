package com.gustlogix.rickandmorty.thread;

public interface TaskCallback<T> {

    void onResult(T result);

    void onError(Exception e);
}
