package com.gustlogix.rickandmorty.repo.remote.network;

public class HttpException extends Exception {

    private int statusCode;

    public HttpException(int statusCode) {
        this.statusCode = statusCode;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }
}
