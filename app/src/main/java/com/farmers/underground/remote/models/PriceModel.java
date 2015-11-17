package com.farmers.underground.remote.models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by omar
 * on 10/9/15.
 */
public class PriceModel {

    @SerializedName("value")
    private Double price;

    @SerializedName("data")
    private String date;

    @SerializedName("source")
    private SourceModel source;

    @SerializedName("more")
    private List<MorePriceModel> morePicesList;


    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public SourceModel getSource() {
        return source;
    }

    public void setSource(SourceModel source) {
        this.source = source;
    }

    public List<MorePriceModel> getMorePicesList() {
        return morePicesList;
    }

    public void setMorePicesList(List<MorePriceModel> morePicesList) {
        this.morePicesList = morePicesList;
    }
}
