package com.gustlogix.rickandmorty;

import android.app.Application;

import com.facebook.stetho.Stetho;

public class MyApplication extends Application {
    public void onCreate() {
        super.onCreate();
        SimpleServiceLocator.init(this);
        Stetho.initializeWithDefaults(this);
    }
}
