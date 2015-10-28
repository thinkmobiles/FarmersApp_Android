package com.farmers.underground.remote.services;

import com.farmers.underground.remote.models.LastCropPricesModel;
import com.farmers.underground.remote.models.PricesByDateModel;
import com.farmers.underground.remote.models.base.PriceBase;

import retrofit.Call;
import retrofit.http.GET;
import retrofit.http.Query;

import java.util.List;

/**
 * Created by tZpace
 * on 06-Oct-15.
 */
public interface PricesService {

    @GET("prices/getLast")
    Call<List<LastCropPricesModel>> getLast();

   /**
    * localhost:7792/prices/getDataForPeriod?startDate=2015-10-27T12:09:12.000Z&endDate=2015-10-24T12:09:12.000Z&name=אנונה
    *
    * this is for fetch prices for cor for period
    * */
    @GET("prices/getCropPricesForPeriod")
    Call<List<PricesByDateModel>> getCropPricesForPeriod(@Query("cropName") String cropName, @Query("startDate") String startDate, @Query("endDate") String endDate);

}
