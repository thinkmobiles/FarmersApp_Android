package com.farmers.underground;

import android.app.Application;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.crashlytics.android.Crashlytics;
import com.facebook.FacebookSdk;
import com.farmers.underground.config.ProjectConstants;
import com.farmers.underground.remote.RetrofitSingleton;
import com.farmers.underground.remote.models.ErrorMsg;
import com.farmers.underground.remote.models.UserCredentials;
import com.farmers.underground.remote.models.UserProfile;
import com.farmers.underground.remote.models.base.MarketeerBase;
import com.farmers.underground.remote.util.ACallback;
import com.farmers.underground.remote.util.ICallback;
import com.farmers.underground.ui.activities.LoginSignUpActivity;
import com.farmers.underground.ui.utils.TypefaceManager;


import io.fabric.sdk.android.Fabric;
import java.util.HashSet;
import java.util.Locale;

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

        // Init Crashlytics
        if(BuildConfig.PRODUCTION)
            Fabric.with(this, new Crashlytics());

        // Init Fonts
        TypefaceManager.init(this);

        // Init the SDK before executing any other operations,
        // especially, if you're using Facebook UI elements.
        FacebookSdk.sdkInitialize(getApplicationContext());



        /**Set Hebrew LOCALE */
           Locale locale = new Locale("iw");
           Locale.setDefault(locale);
           Configuration config = new Configuration();
           config.locale = locale;
           getBaseContext().getResources().updateConfiguration(config,
                   getBaseContext().getResources().getDisplayMetrics());

    }

    public void onUserLogin(){
        setLoggedInBefore();
    }

    public void onUserLogOut() {
        wipeUsrPreferences();
        LoginSignUpActivity.startNew(this);
    }

    private UserProfile currentUser;

    public UserProfile getCurrentUser() {
        return currentUser;
    }

    public void setCurrentUser(UserProfile currentUser) {
        this.currentUser = currentUser;
    }

    private MarketeerBase currentMarketer;

    public MarketeerBase getCurrentMarketer() {
        return currentMarketer;
    }

    public void setCurrentMarketer(MarketeerBase currentMarketer) {
        this.currentMarketer = currentMarketer;
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
                    onUserLogOut();
                }

            }
        });
    }

    public void getMarketerBySession(){
        RetrofitSingleton.getInstance().getMarketeerBySession(new ACallback<MarketeerBase, ErrorMsg>() {
            @Override
            public void onSuccess(MarketeerBase result) {
                setCurrentMarketer(result);
            }

            @Override
            public void onError(@NonNull ErrorMsg error) {

            }
        });
    }

    public boolean isUserAuthenticated(){
       return getUsrPreferences().contains(ProjectConstants.KEY_CURRENT_USER_COOKIES) && !getUsrPreferences().getStringSet(ProjectConstants.KEY_CURRENT_USER_COOKIES,new HashSet<String>(0)).isEmpty();
    }

    public void saveUserCredentials(@NonNull UserCredentials userCredentials){
        getUsrPreferences().edit()
                .putString(ProjectConstants.KEY_CURRENT_USER_LOGIN, userCredentials.getEmail())
                .putString(ProjectConstants.KEY_CURRENT_USER_PASSWORD, userCredentials.getPass())
                .apply();
    }

    public UserCredentials getUserCredentials(){
        if (getUsrPreferences().contains(ProjectConstants.KEY_CURRENT_USER_LOGIN) && getUsrPreferences().contains(ProjectConstants.KEY_CURRENT_USER_PASSWORD)){
            return new UserCredentials(getUsrPreferences().getString(ProjectConstants.KEY_CURRENT_USER_LOGIN,""),getUsrPreferences().getString(ProjectConstants.KEY_CURRENT_USER_PASSWORD,""));
        } else return null;
    }

    public boolean wasLoggedInBefore(){
      return FarmersApp.getUsrPreferences().contains(ProjectConstants.KEY_CURRENT_USER_LOGIN_SUCCESSFUL) && FarmersApp.getUsrPreferences().getBoolean(ProjectConstants.KEY_CURRENT_USER_LOGIN_SUCCESSFUL,false);
    }
    public void setLoggedInBefore(){
       FarmersApp.getUsrPreferences().edit().putBoolean(ProjectConstants.KEY_CURRENT_USER_LOGIN_SUCCESSFUL,true).apply();
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
