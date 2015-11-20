package com.farmers.underground.remote.models;

import com.farmers.underground.remote.models.base.PriceBase;
import com.farmers.underground.ui.utils.DateHelper;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;

/**
 * Created by omar
 * on 10/12/15.
 */
public class MarketeerPriceModel {

    private String date;
    private String name;
    private String location;
    private Double price;

    private MarketeerPrices marketeerPrices;

    public String getPriceDisplay() {
        NumberFormat formatter = new DecimalFormat("#0.00");
        return formatter.format(getPrice());
    }

    public ArrayList<PriceBase.More> getMore() {
        return more;
    }

    public void setMore(ArrayList<PriceBase.More> more) {
        this.more = more;
    }

    public ArrayList<PriceBase.More> more;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getName() {
        return name;
    }

    public String getDisplayDate(){
       return DateHelper.parseToStickyHeaderFormat(getDate());
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public MarketeerPrices getMarketeerPrices() {
        return marketeerPrices;
    }

    public void setMarketeerPrices(MarketeerPrices marketeerPrices) {
        this.marketeerPrices = marketeerPrices;
    }
}
