package com.example.quanlysach;

import android.app.Application;

import com.facebook.stetho.Stetho;

public class MainApplication extends Application {
    public void onCreate() {
        super.onCreate();
        Stetho.initializeWithDefaults(this);
    }
}