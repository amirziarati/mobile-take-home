package com.gustlogix.rickandmorty.dto;

public class Response<T> {

    private boolean isOnline;
    private T result;

    public boolean isOnline() {
        return isOnline;
    }

    public void setOnline(boolean online) {
        isOnline = online;
    }

    public T getResult() {
        return result;
    }

    public void setResult(T result) {
        this.result = result;
    }
}
