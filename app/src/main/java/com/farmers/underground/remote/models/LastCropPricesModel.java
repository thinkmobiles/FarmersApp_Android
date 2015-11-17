package com.farmers.underground.remote.models;

import java.util.ArrayList;

/**
 * Created by tZpace
 * on 14-Oct-15.
 */
public class LastCropPricesModel {
    public String englishName; //will be removed in release
    public String displayName;
    public Boolean isInFavorites;
    public String image;
    public ArrayList<CropPrices> prices;
}
