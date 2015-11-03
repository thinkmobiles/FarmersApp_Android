package com.farmers.underground.ui.models;

/**
 * Created by samson
 * on 28.10.15.
 */
public class MorePriceItemModel {
    private String price;
    private String cropName;
    private String quality;

    public MorePriceItemModel() {
    }

    public MorePriceItemModel(String price, String cropName, String quality) {
        this.price = price;
        this.cropName = cropName;
        this.quality = quality;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getCropName() {
        return cropName;
    }

    public void setCropName(String cropName) {
        this.cropName = cropName;
    }

    public String getQuality() {
        return quality;
    }

    public void setQuality(String quality) {
        this.quality = quality;
    }
}
