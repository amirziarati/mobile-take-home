package com.gustlogix.rickandmorty.repo.remote.network;

public class HttpException extends Exception {

    private int statusCode;

    HttpException(int statusCode) {
        this.statusCode = statusCode;
    }

    int getStatusCode() {
        return statusCode;
    }

    void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }
}
