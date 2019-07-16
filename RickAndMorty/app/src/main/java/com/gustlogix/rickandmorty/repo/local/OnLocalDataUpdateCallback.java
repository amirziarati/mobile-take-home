package com.gustlogix.rickandmorty.repo.local;

public interface OnLocalDataUpdateCallback {

    void onUpdateDone();

    void onError(Exception e);
}
