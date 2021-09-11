package com.byl.multiupload;

import android.app.Application;

import com.byl.multiupload.upload.UploadClientManager;

public class App extends Application {

    private static App context;

    @Override
    public void onCreate() {
        super.onCreate();
        context = this;
        UploadClientManager.getInstance().init();
    }

    public static App getContext() {
        return context;
    }
}
