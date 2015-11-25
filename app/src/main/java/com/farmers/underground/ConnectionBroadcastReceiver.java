package com.farmers.underground;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;


/**
 * Created by tZpace
 * on 24-Nov-15.
 */
public class ConnectionBroadcastReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        if(intent!=null && intent.getAction().equals(ConnectivityManager.CONNECTIVITY_ACTION)) {

            Log.d("onReceive", " CONNECTIVITY_ACTION");

            ConnectivityManager conMan = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

            NetworkInfo activeNetwork = conMan.getActiveNetworkInfo();
            boolean isConnected = activeNetwork != null && activeNetwork.isConnected();

            // if(netCallback != null)connectionState(isConnected);

            Log.d("onReceive", " isConnected = " + isConnected);

            Bundle b = new Bundle();
            b.putBoolean("isConnected",isConnected);

            FarmersApp.getInstance().setConnectionStateFromReceiver(isConnected);

            Notifier.sendData(b);
        }

    }

}