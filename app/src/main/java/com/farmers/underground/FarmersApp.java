package com.farmers.underground;

import android.app.Application;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.facebook.FacebookSdk;
import com.farmers.underground.config.ProjectConstants;
import com.farmers.underground.remote.RetrofitSingleton;
import com.farmers.underground.remote.models.ErrorMsg;
import com.farmers.underground.remote.models.UserProfile;
import com.farmers.underground.remote.util.ACallback;
import com.farmers.underground.remote.util.ICallback;
import com.farmers.underground.ui.activities.LoginSignUpActivity;
import com.farmers.underground.ui.utils.TypefaceManager;
import com.vincentbrison.openlibraries.android.dualcache.lib.DualCacheContextUtils;
import com.vincentbrison.openlibraries.android.dualcache.lib.DualCacheLogUtils;

/**
 * Created by tZpace
 * on 24-Sep-15.
 */
public class FarmersApp extends Application {

    private volatile static FarmersApp ourInstance;

    public synchronized static FarmersApp getInstance() {
        if (ourInstance == null) {
            if (BuildConfig.DEBUG){
                killAppProcess();
                throw new Error("WTF! FarmersApp was not created!");
            }
        }
        return ourInstance;
    }

    public FarmersApp() { /* do not modify */}

    public static void killAppProcess() {
        android.os.Process.killProcess(android.os.Process.myPid());
    }

    @Override
    public void onCreate() {
        super.onCreate();
        ourInstance = this;

        TypefaceManager.init(this);

        // Initialize the SDK before executing any other operations,
        // especially, if you're using Facebook UI elements.
        FacebookSdk.sdkInitialize(getApplicationContext());


        // Android dualcache
        if (BuildConfig.DEBUG)
            DualCacheLogUtils.enableLog();
        DualCacheContextUtils.setContext(getApplicationContext());

        /**next*/
    }

    public void onUserLogin(){

    }

    public void onUserLogOut(){
        wipeUsrPreferences();
    }

    private UserProfile currentUser;

    public UserProfile getCurrentUser() {
        return currentUser;
    }

    public void setCurrentUser(UserProfile currentUser) {
        this.currentUser = currentUser;
    }

    public void getUserProfileAsync(@Nullable final ICallback<UserProfile, ErrorMsg> callback) {
        RetrofitSingleton.getInstance().getUserProfileBySession(new ACallback<UserProfile, ErrorMsg>() {
            @Override
            public void onSuccess(UserProfile result) {
                setCurrentUser(result);
                if (callback != null) {
                    callback.onSuccess(getCurrentUser());
                    callback.anyway();
                }
            }

            @Override
            public void onError(@NonNull ErrorMsg error) {

                if (callback != null) {
                    callback.onError(error);
                    callback.anyway();
                } else {
                    Intent intent = new Intent(getApplicationContext(), LoginSignUpActivity.class);
                    getApplicationContext().startActivity(intent);
                }

            }
        });
    }


    public static SharedPreferences getAppPreferences() {
        return getInstance().getSharedPreferences(ProjectConstants.PREFERENCES_FILE_NAME_APP, MODE_PRIVATE);
    }

    /**
     * User pass,login,cookies. wipe on log-out
     */
    public static SharedPreferences getUsrPreferences() {
        return getInstance().getSharedPreferences(ProjectConstants.PREFERENCES_FILE_NAME_USR, MODE_PRIVATE);
    }

    /**
     * wipe on log-out
     */
    public static void wipeUsrPreferences() {
        getUsrPreferences().edit().clear().apply();
    }

    /**
     * for testing
     */
    public static void wipeAppPreferences() {
        getUsrPreferences().edit().clear().apply();
    }

   /* public static boolean isFirstLaunch() {
        return !getAppPreferences().contains(ProjectConstants.KEY_APP_LAUNCHED_BEFORE);
    }

    public static void resetFirstLaunch() {
        getAppPreferences().edit()
                .putBoolean(ProjectConstants.KEY_APP_LAUNCHED_BEFORE, true)
                .apply();
    }*/

    public static boolean showTutorial() {
        return !getAppPreferences().contains(ProjectConstants.KEY_APP_SHOW_SKIP_TUTORIAL)
                || getAppPreferences().getBoolean(ProjectConstants.KEY_APP_SHOW_SKIP_TUTORIAL, true);
    }

    public static void skipTutorialNextTime() {
        getAppPreferences().edit()
                .putBoolean(ProjectConstants.KEY_APP_SHOW_SKIP_TUTORIAL, false)
                .apply();
    }

    public static void setSkipMode(boolean isSkip){
        getUsrPreferences().edit()
                .putBoolean(ProjectConstants.KEY_CURRENT_USER_SKIP_MODE, isSkip)
                .apply();
    }

    public static boolean isSkipMode(){
        return getUsrPreferences().getBoolean(ProjectConstants.KEY_CURRENT_USER_SKIP_MODE, true);
    }
}
