package com.farmers.underground.remote.models;

/**
 * Created by omar
 * on 10/9/15.
 */
public class PriceModel {
    private String price;
    private String marketeerName;
    private String qualityName;

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getMarketeerName() {
        return marketeerName;
    }

    public void setMarketeerName(String marketeerName) {
        this.marketeerName = marketeerName;
    }


    public String getQualityName() {
        return qualityName;
    }

    public void setQualityName(String qualityName) {
        this.qualityName = qualityName;
    }
}
