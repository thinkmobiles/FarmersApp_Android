package com.farmers.underground.remote.models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by omar on 10/12/15.
 */
public class MorePriceModel {
    @SerializedName("minPrice")
    private float minPrice;

    @SerializedName("maxPrice")
    private float maxPrice;

    @SerializedName("avgPrice")
    private float avgPrice;

    @SerializedName("site")
    private String site;

    @SerializedName("name")
    private String name;

    @SerializedName("date")
    private String date;


    public float getMinPrice() {
        return minPrice;
    }

    public void setMinPrice(float minPrice) {
        this.minPrice = minPrice;
    }

    public float getMaxPrice() {
        return maxPrice;
    }

    public void setMaxPrice(float maxPrice) {
        this.maxPrice = maxPrice;
    }

    public float getAvgPrice() {
        return avgPrice;
    }

    public void setAvgPrice(float avgPrice) {
        this.avgPrice = avgPrice;
    }

    public String getSite() {
        return site;
    }

    public void setSite(String site) {
        this.site = site;
    }

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
