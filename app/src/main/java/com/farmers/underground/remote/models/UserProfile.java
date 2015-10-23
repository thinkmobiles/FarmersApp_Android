package com.farmers.underground.remote.models;

import android.text.TextUtils;

import java.util.ArrayList;

/**
 * Created by tZpace
 * on 02-Oct-15.
 */
public class UserProfile {

    private String _id;
    private String email;
    private String pass;
    private String fullName;
    private String confirmToken;
    private String fbId;
    private String avatar;

    private String updatedAt; //"2015-10-02T09:32:53.979Z"
    private String createdAt; //"2015-10-02T09:32:53.979Z"
    public String marketeer;  //"561270af9c8fd4643656abd2"  or null if user not select marketeer.  If (marketeer == null) AND (newMarketeer == false) SHOW ADD  marketeer screen

    private boolean newMarketeer;

    private ArrayList<String> favorites;

    /**
     *  if true - > show Change Marketeer Button
     * */
    private boolean canChangeMarketeer;

    public boolean hasMarketir() {
        return !TextUtils.isEmpty(marketeer) && !newMarketeer;
    }

    public String getFullName() {
        return fullName;
    }

    public String getAvatar() {
        return avatar;
    }

    public String getEmail() {
        return email;
    }

    /**
     * if true - don't show ADD  marketeer screen, marketeer is added but NOT approved
     */
    public boolean isNewMarketeer() {
        return newMarketeer;
    }
}
