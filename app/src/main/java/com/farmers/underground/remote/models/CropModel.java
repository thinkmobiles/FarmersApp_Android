package com.farmers.underground.remote.models;

/**
 * Created by omar on 10/2/15.
 */
public class CropModel {
    private String imgLink;
    private int ID;

    public CropModel(int ID) {
        this.ID = ID;
    }

    public int getID() {
        return ID;
    }

    public String getImgLink() {
        return imgLink;
    }

    public void setImgLink(String imgLink) {
        this.imgLink = imgLink;
    }
}
