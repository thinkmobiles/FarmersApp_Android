package com.farmers.underground.ui.utils;

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
}
