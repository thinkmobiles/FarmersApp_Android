package com.farmers.underground.remote.models;

import com.farmers.underground.remote.models.base.PriceBase;

import java.util.ArrayList;

/**
 * Created by tZpace
 * on 28-Oct-15.
 */

/**
 * List<PricesByDateModel> - will be prices by period
 * and PricesByDateModel - is one day item with prices for Marketer, PC, WH
 * */
public class PricesByDateModel {
    public ArrayList<PriceBase> prices;
}
