package com.farmers.underground.ui.utils;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.widget.Toast;

import com.farmers.underground.FarmersApp;
import com.farmers.underground.ui.base.BaseActivity;

/**
 * Created by tZpace
 * on 09-Nov-15.
 */
public class FacebookInviteUtil {

    public static <A extends BaseActivity> void invitePeopleByFBMessenger(A activity, String message) {
        if (hasFBMessenger(activity)) {
            Intent sendIntent = new Intent();
            sendIntent.setAction(Intent.ACTION_SEND);
            sendIntent.putExtra(Intent.EXTRA_TEXT, message);
            sendIntent.setType("text/plain");
            sendIntent.setPackage("com.facebook.orca");
            try {
                activity.startActivity(sendIntent);
                //track action on Google Analytics
                AnalyticsTrackerUtil.getInstance().trackEvent(AnalyticsTrackerUtil.TypeEvent.InviteFacebook);
            } catch (android.content.ActivityNotFoundException ex) {
                activity.showToast("Please Install Facebook Messenger", Toast.LENGTH_SHORT);
            }
        } else {
            activity.showToast("Please Install Facebook Messenger", Toast.LENGTH_SHORT);
        }
    }

    private static <A extends BaseActivity> boolean hasFBMessenger(A activity) {
        try {
            activity.getPackageManager().getPackageInfo("com.facebook.orca", 0);
            return true;
        } catch (PackageManager.NameNotFoundException e) {
            return false;
        }
    }

}
