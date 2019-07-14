package com.gustlogix.rickandmorty.repo.local;

public interface OnLocalDataUpdateListener {

    void onUpdateDone();

    void onError(Exception e);
}
