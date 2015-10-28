package com.farmers.underground.remote.models;

/**
 * Created by samson on 28.10.15.
 */
public class UserPriceQualityModel {
    private float price;
    private String userQuality;

    public UserPriceQualityModel() {
    }

    public UserPriceQualityModel(float price, String userQuality) {
        this.price = price;
        this.userQuality = userQuality;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public String getUserQuality() {
        return userQuality;
    }

    public void setUserQuality(String userQuality) {
        this.userQuality = userQuality;
    }
}
