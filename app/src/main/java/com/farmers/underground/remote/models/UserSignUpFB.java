package com.farmers.underground.remote.models;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

/**
 * Created by tZpace
 * on 02-Oct-15.
 */
public class UserSignUpFB {

    private String email = "email";
    private String fullName = "fullName";
    private String avatar = "avatar";
    private String fbId = "fbId";
    private String fbAccessToken = "fbAccessToken";

    public UserSignUpFB(@NonNull String fbId, @NonNull String fbAccessToken, @Nullable String avatar, @Nullable String fullName, @Nullable String email) {
        this.email = email;
        this.fullName = fullName;
        this.avatar = avatar;
        this.fbId = fbId;
        this.fbAccessToken = fbAccessToken;
    }
}
