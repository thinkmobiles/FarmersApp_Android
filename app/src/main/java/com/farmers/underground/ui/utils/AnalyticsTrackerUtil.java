package com.farmers.underground.ui.utils;

import android.app.Activity;
import android.content.Context;
import android.util.Log;

import com.farmers.underground.R;
import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by samson
 * on 24.11.15.
 */
public class AnalyticsTrackerUtil {

    public enum TrackerName{APP}

    private static AnalyticsTrackerUtil sInstance;

    private final Map<TrackerName, Tracker> mTrackers = new HashMap<>();
    private final Context mContext;

    public static synchronized void initialize(Context context) {
        if (sInstance != null) {
            throw new IllegalStateException("Extra call to initialize analytics trackers");
        }
        sInstance = new AnalyticsTrackerUtil(context);
    }

    public static synchronized AnalyticsTrackerUtil getInstance() {
        if (sInstance == null) {
            throw new IllegalStateException("Call initialize() before getInstance()");
        }
        return sInstance;
    }

    private AnalyticsTrackerUtil(Context context) {
        mContext = context.getApplicationContext();
    }

    public void enableSendingReports(boolean enable){
        GoogleAnalytics.getInstance(mContext).setDryRun(!enable);
    }

    public void startActivityReport(Activity activity){
        GoogleAnalytics.getInstance(mContext).reportActivityStart(activity);
    }

    public void stopActivityReport(Activity activity){
        GoogleAnalytics.getInstance(mContext).reportActivityStop(activity);
    }

    public void startNewSession(){
        getTracker(TrackerName.APP).send(new HitBuilders.AppViewBuilder().setNewSession().build());
    }

    public synchronized Tracker getTracker(TrackerName target) {
        if (!mTrackers.containsKey(target)) {
            Tracker tracker;
            switch (target) {
                case APP:
                    tracker = GoogleAnalytics.getInstance(mContext).newTracker(R.xml.app_tracker);
                    break;
                default:
                    throw new IllegalArgumentException("Unhandled analytics target " + target);
            }
            mTrackers.put(target, tracker);
        }

        return mTrackers.get(target);
    }

    public void trackScreenView(String screenName){
        Tracker tracker = getTracker(TrackerName.APP);
        tracker.setScreenName(screenName);
        tracker.send(new HitBuilders.ScreenViewBuilder().build());
    }

    public void trackEvent(String category, String action, String label){
        getTracker(TrackerName.APP).send(new HitBuilders.EventBuilder()
                        .setCategory(category)
                        .setAction(action)
                        .setLabel(label)
                        .build()
        );
    }

    public void trackTimeSession(){
        //todo
    }
}
