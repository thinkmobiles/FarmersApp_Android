package com.farmers.underground.ui.utils;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.Nullable;

import com.farmers.underground.BuildConfig;
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

    public enum TypeEvent{
        //track enter in app
        EnterApp,
        //in registration choose marketer or skip
        MarketerChoosed,
        MarketerSkip,
        //types invite
        InviteSms,
        InviteEmail,
        InviteWhatsApp,
        InviteFacebook,
        //track add price
        AddPrice
    }
    // string constants for tracking events
    private static final String CATEGORY_APP = "Application";
    private static final String CATEGORY_REGISTRATION = "Registration";
    private static final String CATEGORY_INVITE = "Invite";
    private static final String CATEGORY_PRICE = "Price";
    private static final String ACTION_ENTER = "Enter";
    private static final String ACTION_SMS = "SMS";
    private static final String ACTION_EMAIL = "Email";
    private static final String ACTION_WHATSAPP = "WhatsApp";
    private static final String ACTION_FACEBOOK = "Facebook";
    private static final String ACTION_CHOOSE = "Marketer was chosen";
    private static final String ACTION_SKIP = "Skip of marketers";
    private static final String ACTION_ADD = "add";

    private static AnalyticsTrackerUtil sInstance;

    private final Map<TrackerName, Tracker> mTrackers = new HashMap<>();
    private final GoogleAnalytics analytics;
    private Activity currentActivity;
    private String mUserInfo;

    public void setUserInfo(String userInfo) {
        mUserInfo = userInfo;
    }

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
        analytics = GoogleAnalytics.getInstance(context.getApplicationContext());
    }

    private void dispatch(){
        analytics.dispatchLocalHits();
    }

    public void startActivityReport(Activity activity){
        currentActivity = activity;
        analytics.reportActivityStart(currentActivity);
    }

    public void stopActivityReport(){
        analytics.reportActivityStop(currentActivity);
    }

    public synchronized Tracker getTracker(TrackerName target) {
        if (!mTrackers.containsKey(target)) {
            Tracker tracker;
            switch (target) {
                case APP:
                    if (BuildConfig.DEBUG) {
                        tracker = analytics.newTracker(R.xml.app_tracker_debug);
                    } else {
                        tracker = analytics.newTracker(R.xml.app_tracker_reales);
                    }
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
        dispatch();
    }

    public void trackEvent(TypeEvent typeEvent){
        switch (typeEvent){
            case EnterApp:
                trackEvent(CATEGORY_APP, ACTION_ENTER);
                break;
            case MarketerChoosed:
                trackEvent(CATEGORY_REGISTRATION, ACTION_CHOOSE);
                break;
            case MarketerSkip:
                trackEvent(CATEGORY_REGISTRATION, ACTION_SKIP);
                break;
            case InviteSms:
                trackEventInvite(ACTION_SMS);
                break;
            case InviteEmail:
                trackEventInvite(ACTION_EMAIL);
                break;
            case InviteWhatsApp:
                trackEventInvite(ACTION_WHATSAPP);
                break;
            case InviteFacebook:
                trackEventInvite(ACTION_FACEBOOK);
                break;
            case AddPrice:
                trackEvent(CATEGORY_PRICE, ACTION_ADD, mUserInfo);
                break;
        }
    }

    private void trackEventInvite(String action){
        trackEvent(CATEGORY_INVITE, action, mUserInfo);
    }

    private void trackEvent(String category, String action, String lable){
        if(lable != null) {
            getTracker(TrackerName.APP).send(new HitBuilders.EventBuilder()
                            .setCategory(category)
                            .setAction(action)
                            .setLabel(lable)
                            .build()
            );
            dispatch();
        } else {
            trackEvent(category, action);
        }
    }

    private void trackEvent(String category, String action){
        getTracker(TrackerName.APP).send(new HitBuilders.EventBuilder()
                        .setCategory(category)
                        .setAction(action)
                        .build()
        );
        dispatch();
    }

}
