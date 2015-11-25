package com.farmers.underground.ui.utils;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.widget.Toast;

import com.farmers.underground.FarmersApp;

/**
 * Created by omar
 * on 10/27/15.
 */
public class WhatsAppUtil {
    private static WhatsAppUtil sWhatsAppUtil;
    private Context context;

    public WhatsAppUtil(Context context) {
        this.context = context;
    }

    public static WhatsAppUtil getInstance (Context context){
        if(sWhatsAppUtil == null)
            sWhatsAppUtil = new WhatsAppUtil(context);
        return sWhatsAppUtil;
    }

    public void sendInvitation(){
        PackageManager pm= context.getPackageManager();
        try {

            Intent waIntent = new Intent(Intent.ACTION_SEND);
            waIntent.setType("text/plain");

            String text = "YOUR TEXT HERE";

            PackageInfo info = pm.getPackageInfo("com.whatsapp", PackageManager.GET_META_DATA);
            waIntent.setPackage("com.whatsapp");
            waIntent.putExtra(Intent.EXTRA_TEXT, text);
            context.startActivity(Intent.createChooser(waIntent, "Share with"));

            //track action on Google Analytics
            AnalyticsTrackerUtil.getInstance().trackEvent(AnalyticsTrackerUtil.TypeEvent.InviteWhatsApp);

        } catch (PackageManager.NameNotFoundException e) {
            Toast.makeText(context, "WhatsApp not Installed", Toast.LENGTH_SHORT)
                    .show();
        }
    }
}
