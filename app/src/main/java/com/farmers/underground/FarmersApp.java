package com.farmers.underground;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.os.Bundle;
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
import com.farmers.underground.ui.utils.DateHelper;
import com.farmers.underground.ui.utils.ImageCacheManager;
import com.farmers.underground.ui.utils.TypefaceManager;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;


import org.jetbrains.annotations.NotNull;

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
            if (BuildConfig.DEBUG) {
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

    private ImageLoaders imageCache;

    @Override
    public void onCreate() {
        super.onCreate();
        ourInstance = this;

        // Init Crashlytics
        if (BuildConfig.PRODUCTION)
            Fabric.with(this, new Crashlytics());

        // Init Fonts
        TypefaceManager.init(this);

        // Init the SDK before executing any other operations,
        // especially, if you're using Facebook UI elements.
        FacebookSdk.sdkInitialize(getApplicationContext());


        /*Init ImageCacheManager */
        imageCache = new ImageLoaders(this);

        /**Set Hebrew LOCALE */
        Locale locale = new Locale("iw");
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        getResources().updateConfiguration(config, getResources().getDisplayMetrics());

    }

    public ImageLoaders getImageCache() {
        return imageCache;
    }

    public static final class ImageLoaders implements ImageCacheManager.ImageLoaderCallbacks {

        public static final int CACHE_MAIN = 0;
        public static final int CACHE_ROUND = 1;

        private final Context context;

        private ImageLoaders(Context context) {
            /* Prevent instantiating */
            this.context = context;
        }

        public static DisplayImageOptions.Builder getCacheMain = new DisplayImageOptions.Builder()
                .cacheInMemory(true)
                .showImageForEmptyUri(R.drawable.ic_drawer_crops)
                .showImageOnLoading(R.drawable.ic_drawer_crops)
                .showImageOnFail(R.drawable.ic_drawer_crops)
                .imageScaleType(ImageScaleType.IN_SAMPLE_POWER_OF_2)
                .resetViewBeforeLoading(true)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .cacheOnDisk(true);

        public static DisplayImageOptions.Builder getCacheRoundPic = new DisplayImageOptions.Builder()
                .cacheInMemory(true)
                .displayer( new RoundedBitmapDisplayer(1000))
                .showImageForEmptyUri(R.drawable.ic_drawer_crops)
                .showImageOnLoading(R.drawable.ic_drawer_crops)
                .showImageOnFail(R.drawable.ic_drawer_crops)
                .imageScaleType(ImageScaleType.IN_SAMPLE_POWER_OF_2)
                .resetViewBeforeLoading(false)
                .cacheOnDisk(true);

        @NotNull
        @Override
        public ImageLoader onCreateImageLoader(int loaderId, @org.jetbrains.annotations.Nullable Bundle params) {
            ImageLoader imageLoader;
            switch (loaderId) {
                case CACHE_MAIN:
                    imageLoader = getCacheMain();
                    break;
                case CACHE_ROUND:
                imageLoader = getCacheRoundPic();
                    break;
                //todo paste case here if need some more caches
                default:
                    throw new Error("Bad loader id!");
            }
            return imageLoader;
        }

        private class Im extends ImageLoader {
            public Im() {
                super();
            }
        }

        private void checkInit(ImageLoader imageLoader) {
            if (imageLoader != null && imageLoader.isInited())
                imageLoader.destroy();
        }

        private Im newInstance() {
            return new Im();
        }

        private ImageLoader getCacheRoundPic() {
            ImageLoader
                    imageLoader = newInstance();
            checkInit(imageLoader);
            imageLoader.init(
                    new ImageLoaderConfiguration.Builder(context)
                            .defaultDisplayImageOptions(getCacheRoundPic.build())
                            .denyCacheImageMultipleSizesInMemory()
                            .memoryCacheSizePercentage(5)
                            .build());

            imageLoader.handleSlowNetwork(true);

            return imageLoader;
        }

        private ImageLoader getCacheMain() {
            ImageLoader
                    imageLoader = newInstance();
            checkInit(imageLoader);
            imageLoader.init(
                    new ImageLoaderConfiguration.Builder(context)
                            .defaultDisplayImageOptions(getCacheMain.build())
                            .denyCacheImageMultipleSizesInMemory()
                            .memoryCacheSizePercentage(10)
                            .build());

            imageLoader.handleSlowNetwork(true);

            return imageLoader;
        }
    }


    public void onUserLogin() {
        setLoggedInBefore();
    }

    public void onUserLogOut() {
        wipeUsrPreferences();
        LoginSignUpActivity.startNew(this);
    }

    private UserProfile currentUser;

    public UserProfile getCurrentUser() {
        if (currentUser ==null)
            getCachedUserProfile();
        return currentUser;
    }

    public void setCurrentUser(UserProfile currentUser) {
        this.currentUser = currentUser;
    }

    private static MarketeerBase currentMarketer;

    public MarketeerBase getCurrentMarketer() {
        return currentMarketer;
    }

    public void setCurrentMarketer(MarketeerBase currentMarketer) {
        FarmersApp.currentMarketer = currentMarketer;
    }

    public void getUserProfileAsync(@Nullable final ICallback<UserProfile, ErrorMsg> callback) {
        RetrofitSingleton.getInstance().getUserProfileBySession(new ACallback<UserProfile, ErrorMsg>() {
            @Override
            public void onSuccess(UserProfile result) {
                if (result == null)
                    onError(new ErrorMsg("Profile is not fetched"));

                setCurrentUser(result);

                cacheUserProfile(result);

                getMarketerBySession();

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

    public void getMarketerBySession() {
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

    public boolean isUserAuthenticated() {
        return getUsrPreferences().contains(ProjectConstants.KEY_CURRENT_USER_COOKIES) && !getUsrPreferences().getStringSet(ProjectConstants.KEY_CURRENT_USER_COOKIES, new HashSet<String>(0)).isEmpty();
    }

    public void saveUserCredentials(@NonNull UserCredentials userCredentials) {
        getUsrPreferences().edit()
                .putString(ProjectConstants.KEY_CURRENT_USER_LOGIN, userCredentials.getEmail())
                .putString(ProjectConstants.KEY_CURRENT_USER_PASSWORD, userCredentials.getPass())
                .apply();
    }

    public UserCredentials getUserCredentials() {
        if (getUsrPreferences().contains(ProjectConstants.KEY_CURRENT_USER_LOGIN) && getUsrPreferences().contains(ProjectConstants.KEY_CURRENT_USER_PASSWORD)) {
            return new UserCredentials(getUsrPreferences().getString(ProjectConstants.KEY_CURRENT_USER_LOGIN, ""), getUsrPreferences().getString(ProjectConstants.KEY_CURRENT_USER_PASSWORD, ""));
        } else return null;
    }

    public boolean wasLoggedInBefore() {
        return FarmersApp.getUsrPreferences().contains(ProjectConstants.KEY_CURRENT_USER_LOGIN_SUCCESSFUL) && FarmersApp.getUsrPreferences().getBoolean(ProjectConstants.KEY_CURRENT_USER_LOGIN_SUCCESSFUL, false);
    }

    public void setLoggedInBefore() {
        FarmersApp.getUsrPreferences().edit().putBoolean(ProjectConstants.KEY_CURRENT_USER_LOGIN_SUCCESSFUL, true).apply();
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

    public static void setSkipMode(boolean isSkip) {
        getUsrPreferences().edit()
                .putBoolean(ProjectConstants.KEY_CURRENT_USER_SKIP_MODE, isSkip)
                .apply();
    }

    public static boolean isSkipMode() {
        return getUsrPreferences().getBoolean(ProjectConstants.KEY_CURRENT_USER_SKIP_MODE, true);
    }

    private void cacheUserProfile(UserProfile result) {

        Gson gson = new GsonBuilder().create();
        String json = getUsrPreferences().getString("mUserProfile", "");

        if(!json.isEmpty()){
            UserProfile temp = gson.fromJson(json, UserProfile.class);
            if (temp.getId().equals(result.getId()) && DateHelper.parseToCalendar(temp.getUpdatedAt()).before(DateHelper.parseToCalendar(result.getUpdatedAt()))){
                gson = new GsonBuilder().create();
                json = gson.toJson(result);
                getUsrPreferences().edit().putString("mUserProfile", json).apply();
            }
        } else {
            gson = new GsonBuilder().create();
            json = gson.toJson(result);

            getUsrPreferences().edit().putString("mUserProfile", json).apply();
        }

    }

    /**false if no cached profile*/
    private boolean getCachedUserProfile(){
        Gson gson = new GsonBuilder().create();
        String json = getUsrPreferences().getString("mUserProfile", "");
        if(json.isEmpty())
            return false;

        currentUser = gson.fromJson(json, UserProfile.class);

        return true;
    }
}
