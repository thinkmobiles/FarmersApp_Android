package com.farmers.underground.ui.utils;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.widget.Toast;

import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.widget.ShareDialog;
import com.farmers.underground.ui.base.BaseActivity;

/**
 * Created by tZpace
 * on 09-Nov-15.
 */
public class FacebookInviteUtil {

    public static <A extends BaseActivity> void inviteFBpeopleMessage(A activity,String message){
        if (ShareDialog.canShow(ShareLinkContent.class)) {
            try {
                PackageManager pm= activity.getPackageManager();
                PackageInfo info = pm.getPackageInfo("com.facebook.katana", PackageManager.GET_META_DATA);

                ShareLinkContent content = new ShareLinkContent.Builder()
                        .setContentUrl(Uri.parse("https://developers.facebook.com")) //todo
                        .setContentTitle("Invitation to Farmers App")
                        .setContentDescription(message)
                        .build();
                ShareDialog.show(activity, content);
            } catch (PackageManager.NameNotFoundException e) {
                activity.showToast("Facebook app is not installed", Toast.LENGTH_SHORT);

            }
        } else {
            activity.showToast("Facebook app is not installed", Toast.LENGTH_SHORT);
        }
    }

    public static <A extends BaseActivity> void inviteFBpeopleMessenger(A activity,String message){
        if(hasFBMessenger(activity)) {
            Intent sendIntent = new Intent();
            sendIntent.setAction(Intent.ACTION_SEND);
            sendIntent.putExtra(Intent.EXTRA_TEXT, message);
            sendIntent.setType("text/plain");
            sendIntent.setPackage("com.facebook.orca");
            try {
                activity.startActivity(sendIntent);
            } catch (android.content.ActivityNotFoundException ex) {
                activity.showToast("Please Install Facebook Messenger", Toast.LENGTH_SHORT);
            }
        } else {
            activity.showToast("Please Install Facebook Messenger", Toast.LENGTH_SHORT);
        }
    }

    private static <A extends BaseActivity> boolean hasFBMessenger(A activity){
        try {
            activity.getPackageManager().getPackageInfo("com.facebook.orca", 0);
            return true;
        } catch (PackageManager.NameNotFoundException e) {
            return false;
        }
    }

}
