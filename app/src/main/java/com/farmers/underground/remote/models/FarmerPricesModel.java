package com.farmers.underground.remote.models;

import java.util.ArrayList;

/**
 * Created by tZpace
 * on 28-Oct-15.
 */
public class FarmerPricesModel {
    /** the date chosen by the user
     * */
    public String date;

    /** display crop name
     * */
    public String cropName;

    public ArrayList<UserPriceQualityModel> prices;

}
