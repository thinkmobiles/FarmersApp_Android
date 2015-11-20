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

    private MarketeerPrices marketeerPrices;

    public String getPriceDisplay() {
        NumberFormat formatter = new DecimalFormat("#0.00");
        return formatter.format(getPrice());
    }

    public String getDisplayDate() {
        return DateHelper.parseToStickyHeaderFormat(getDate());
    }

    public ArrayList<PriceBase.More> getMore() {
        return this.marketeerPrices.more;
    }

    public void setMore(ArrayList<PriceBase.More> more) {
        this.marketeerPrices.more = more;
    }

    public String getDate() {
        return this.marketeerPrices.data;
    }

    public void setDate(String date) {
        this.marketeerPrices.data = date;
    }

    public String getName() {
        return this.marketeerPrices.name;
    }

    public void setName(String name) {
        this.marketeerPrices.name = name;
    }

    public Double getPrice() {
        return this.marketeerPrices.price;
    }

    public void setPrice(Double price) {
        this.marketeerPrices.price = price;
    }

    public String getLocation() {
        return this.marketeerPrices.location;
    }

    public void setLocation(String location) {
        this.marketeerPrices.location = location;
    }

    public MarketeerPrices getMarketeerPrices() {
        return marketeerPrices;
    }

    public void setMarketeerPrices(MarketeerPrices marketeerPrices) {
        this.marketeerPrices = marketeerPrices;
    }

    public String getQuality() {
        return this.marketeerPrices.quality;
    }

    public void setQuality(String quality) {
        this.marketeerPrices.quality = quality;
    }
}
