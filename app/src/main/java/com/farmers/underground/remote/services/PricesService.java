package com.farmers.underground.remote.services;

import com.farmers.underground.remote.models.LastCropPricesModel;
import com.farmers.underground.remote.models.base.PriceBase;

import retrofit.Call;
import retrofit.http.GET;
import retrofit.http.Query;

import java.util.ArrayList;
import java.util.LinkedList;

/**
 * Created by tZpace
 * on 06-Oct-15.
 */
public interface PricesService {

    @GET("prices/getLast")
    Call<LinkedList<LastCropPricesModel>> getLast(); //todo - ?

   /**
    * localhost:7792/prices/getDataForPeriod?startDate=2015-10-27T12:09:12.000Z&endDate=2015-10-24T12:09:12.000Z&name=אנונה
    *
    * this is for fetch prices for cor for period
    * */
    @GET("prices/getCropPricesForPeriod")
    Call<ArrayList<PriceBase>> getCropPricesForPeriod(@Query("startDate") String startDate, @Query("endDate") String endDate, @Query("name") String name);

}
