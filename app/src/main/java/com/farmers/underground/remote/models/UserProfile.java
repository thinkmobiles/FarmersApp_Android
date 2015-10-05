package com.farmers.underground.remote.models;

import android.text.TextUtils;

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
    private String avatar;

    private String updatedAt; //"2015-10-02T09:32:53.979Z"
    private String createdAt; //"2015-10-02T09:32:53.979Z"
    public String marketeer;
    private String[] favorites;

    public boolean hasMarketir(){
         return !TextUtils.isEmpty(marketeer);
    }

    public String getFullName() {
        return fullName;
    }

    public String getAvatar() {
        return avatar;
    }
}
