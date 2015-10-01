package com.farmers.underground;

import android.app.Application;
import com.facebook.FacebookSdk;

/**
 * Created by tZpace
 * on 24-Sep-15.
 */
public class FarmersApp extends Application {

    private volatile static FarmersApp ourInstance;

    public synchronized static FarmersApp getInstance() {
        if (ourInstance == null) {
            if (BuildConfig.DEBUG)
                throw new Error("WTF! FarmersApp was not created!");
        }
        return ourInstance;
    }

    public FarmersApp() { /* do not modify */}

    @Override
    public void onCreate() {
        super.onCreate();

        ourInstance = this;

        // Initialize the SDK before executing any other operations,
        // especially, if you're using Facebook UI elements.
        FacebookSdk.sdkInitialize(getApplicationContext());

    }
/*
    public void killProcess() {
        android.os.Process.killProcess(android.os.Process.myPid());
    }*/
}
