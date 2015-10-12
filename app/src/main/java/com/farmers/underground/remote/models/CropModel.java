package com.farmers.underground.remote.models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by omar on 10/2/15.
 */
public class CropModel {

    @SerializedName("image")
    private String imgLink;

    @SerializedName("_crop")
    private String ID;

    @SerializedName("isInFavorites")
    private boolean isInFavorites;

    @SerializedName("englishName")
    private String englishName;

    @SerializedName("englishName")
    private String displayName;

    @SerializedName("prices")
    private List<PriceModel> priceList;



    public String getImgLink() {
        return imgLink;
    }

    public void setImgLink(String imgLink) {
        this.imgLink = imgLink;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public boolean isInFavorites() {
        return isInFavorites;
    }

    public void setIsInFavorites(boolean isInFavorites) {
        this.isInFavorites = isInFavorites;
    }

    public String getEnglishName() {
        return englishName;
    }

    public void setEnglishName(String englishName) {
        this.englishName = englishName;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public List<PriceModel> getPriceList() {
        return priceList;
    }

    public void setPriceList(List<PriceModel> priceList) {
        this.priceList = priceList;
    }
}
