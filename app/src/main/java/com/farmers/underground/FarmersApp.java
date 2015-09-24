package com.farmers.underground;

import android.app.Application;

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

        /** put your code below */

        // here!
    }
}
