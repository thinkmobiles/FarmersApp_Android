package com.farmers.underground.remote.models;

import java.util.List;

/**
 * Created by omar on 10/2/15.
 */
public class CropModel {

    private String imgLink;
    private String ID;
    private List<PriceModel> priceList;


    public String getID() {
        return ID;
    }

    public String getImgLink() {
        return imgLink;
    }

    public void setImgLink(String imgLink) {
        this.imgLink = imgLink;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public List<PriceModel> getPriceList() {
        return priceList;
    }

    public void setPriceList(List<PriceModel> priceList) {
        this.priceList = priceList;
    }
}
