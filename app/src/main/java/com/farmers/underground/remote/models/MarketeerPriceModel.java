package com.farmers.underground.remote.models;

/**
 * Created by omar
 * on 10/12/15.
 */
public class MarketeerPriceModel {

    private String date;
    private String name;
    private String location;

    private PriceModel price;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public PriceModel getPrice() {
        return price;
    }

    public void setPrice(PriceModel price) {
        this.price = price;
    }
}
