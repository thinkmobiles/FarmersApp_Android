package com.farmers.underground.remote.models;

/**
 * Created by samson
 * on 28.10.15.
 */
public class UserPriceQualityModel {
    private Double price;
    private String userQuality;

    public UserPriceQualityModel() {
    }

    public UserPriceQualityModel(Double price, String userQuality) {
        this.price = price;
        this.userQuality = userQuality;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public String getUserQuality() {
        return userQuality;
    }

    public void setUserQuality(String userQuality) {
        this.userQuality = userQuality;
    }
}
