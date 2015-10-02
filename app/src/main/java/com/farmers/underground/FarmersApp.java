package com.farmers.underground;

import android.app.Application;
import android.content.SharedPreferences;

import com.facebook.FacebookSdk;
import com.farmers.underground.config.ProjectConstants;
import com.farmers.underground.ui.utils.TypefaceManager;

import java.util.Map;
import java.util.WeakHashMap;

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
        TypefaceManager.init(this);
        ourInstance = this;

        // Initialize the SDK before executing any other operations,
        // especially, if you're using Facebook UI elements.
        FacebookSdk.sdkInitialize(getApplicationContext());

        /**next*/

    }

//    public void killAppProcess() {
//        android.os.Process.killProcess(android.os.Process.myPid());
//    }

    public static SharedPreferences getAppPreferences() {
        return getInstance().getSharedPreferences(ProjectConstants.PREFERENCES_FILE_NAME_APP, MODE_PRIVATE);
    }

    public static SharedPreferences getUsrPreferences() {
        return getInstance().getSharedPreferences(ProjectConstants.PREFERENCES_FILE_NAME_USR, MODE_PRIVATE);
    }

    public static void wipeUsrPreferences() {
        getUsrPreferences().edit().clear().apply();
    }
    public static void wipeAppPreferences() {
        getUsrPreferences().edit().clear().apply();
    }

}
