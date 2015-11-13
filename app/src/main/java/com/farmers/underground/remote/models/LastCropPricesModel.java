package com.farmers.underground.remote.models;

import com.farmers.underground.remote.models.base.PriceBase;

import java.util.ArrayList;

/**
 * Created by tZpace
 * on 14-Oct-15.
 */
public class LastCropPricesModel {
    public String englishName; //will be remover in release
    public String displayName;
    public Boolean isInFavorites;
    public String image;
    public ArrayList<PriceBase> prices;
}
