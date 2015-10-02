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

    public void killAppProcess() {
        android.os.Process.killProcess(android.os.Process.myPid());
    }

    @Override
    public void onCreate() {
        super.onCreate();
        TypefaceManager.init(this);
        ourInstance = this;

        // Initialize the SDK before executing any other operations,
        // especially, if you're using Facebook UI elements.
        FacebookSdk.sdkInitialize(getApplicationContext());

        /**next*/

        resetFirstLaunch();

    }


    public static SharedPreferences getAppPreferences() {
        return getInstance().getSharedPreferences(ProjectConstants.PREFERENCES_FILE_NAME_APP, MODE_PRIVATE);
    }

    /** wipe on log-out */
    public static SharedPreferences getUsrPreferences() {
        return getInstance().getSharedPreferences(ProjectConstants.PREFERENCES_FILE_NAME_USR, MODE_PRIVATE);
    }

    /** for testing */
    public static void wipeUsrPreferences() {
        getUsrPreferences().edit().clear().apply();
    }

    /** for testing */
    public static void wipeAppPreferences() {
        getUsrPreferences().edit().clear().apply();
    }

    public static boolean isFirstLaunch() {
        return !getAppPreferences().contains(ProjectConstants.KEY_APP_LAUNCHED_BEFORE);
    }

    public static void resetFirstLaunch() {
        getAppPreferences().edit()
                .putBoolean(ProjectConstants.KEY_APP_LAUNCHED_BEFORE, true)
                .apply();
    }

    public static boolean showTutorial() {
        return !getAppPreferences().contains(ProjectConstants.KEY_APP_SHOW_SKIP_TUTORIAL)
                || getAppPreferences().getBoolean(ProjectConstants.KEY_APP_SHOW_SKIP_TUTORIAL, true);
    }

    public static void skipTutorialNextTime() {
        getAppPreferences().edit()
                .putBoolean(ProjectConstants.KEY_APP_SHOW_SKIP_TUTORIAL, true)
                .apply();
    }

}
