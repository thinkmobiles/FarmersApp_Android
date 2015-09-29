package com.farmers.underground.remote.models;


import android.support.annotation.NonNull;

/**
 * Created by tZpace
 * on 28-Sep-15.
 */
public class UserRegistration {

    private String fullName;

    private String email;

    private String pass;

    public UserRegistration(String fullName,@NonNull String email,@NonNull String pass) {
        this.fullName = fullName;
        this.email = email;
        this.pass = pass;
    }
}
