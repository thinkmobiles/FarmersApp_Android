package com.farmers.underground.remote.models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by omar
 * on 10/12/15.
 */
public class MorePriceModel {

    @SerializedName("name")
    private String name;

    @SerializedName("date")
    private String date;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
