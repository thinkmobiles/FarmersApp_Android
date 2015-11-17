package com.farmers.underground.remote.services;

import com.farmers.underground.remote.models.FarmerPricesModel;
import com.farmers.underground.remote.models.LastCropPricesModel;
import com.farmers.underground.remote.models.CropPricesByDateModel;
import com.farmers.underground.remote.models.MarketeerPricesByDateModel;
import com.farmers.underground.remote.models.SuccessMsg;

import retrofit.Call;
import retrofit.http.Body;
import retrofit.http.GET;
import retrofit.http.POST;
import retrofit.http.Query;

import java.util.List;

/**
 * Created by tZpace
 * on 06-Oct-15.
 */
public interface PricesService {

    @GET("prices/getLast")
    Call<List<LastCropPricesModel>>
    getLast();

   /**
    * this is for fetch prices for crop by period
    * */
    @GET("prices/getCropPricesForPeriod")
    Call<List<CropPricesByDateModel>>
    getCropPricesForPeriod(@Query("cropName") String cropName, @Query("startDate") String startDate, @Query("endDate") String endDate);

    /**
     * this is for fetch prices from marketeers by period
     * */
    @GET("prices/getMarketeerCropPricesForPeriod")
    Call<List<MarketeerPricesByDateModel>>
    getMarketeerCropPricesForPeriod(@Query("cropName") String cropName, @Query("startDate") String startDate, @Query("endDate") String endDate);

    @POST("prices/addFarmerPrice")
    Call<SuccessMsg>
    addFarmerPrice(@Body FarmerPricesModel farmerPrices);

}
