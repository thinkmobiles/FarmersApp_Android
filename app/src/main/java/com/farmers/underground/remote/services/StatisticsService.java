package com.farmers.underground.remote.services;

import com.farmers.underground.remote.models.StaticticModel;

import java.util.List;

import retrofit.Call;
import retrofit.http.GET;
import retrofit.http.Query;

/**
 * Created by tZpace
 * on 06-Oct-15.
 */
public interface StatisticsService {

   /**
    * This method for get monthly price statistics Array of objects see
    * [
    *   {                           {                    {
    *   "year": 2012,               "year": 2013,            "year": 2014,
    *   "month": 11,                "month": 0,                 "month": 11,
    *   "pricePc": 11.06,               "pricePc": 13.22,       "pricePc": 10.36,
    *   "priceWs": 11,                  "priceWs": 13,          "priceWs": null,
    *   "priceMk": null                 "priceMk": null         "priceMk": null
    *   },                          },    }
    *   ]
    *
    *       cropName // required string
    *       quality // required string
    * */
    @GET("statistics/price")
    Call<List<StaticticModel>>
    price(@Query("cropName") String cropName, @Query("quality") String quality);

    /**
     * This method for get monthly price statistics Array of objects see see
     * Query: cropName // required string quality // required string month // integer if need monthly statistics see
     *
     * https://projects.invisionapp.com/share/383U0QUUG#/screens/95933323
     * */
    @GET("statistics/price")
    Call<List<StaticticModel>>
    price(@Query("cropName") String cropName, @Query("quality") String quality, @Query("month") Integer month);

}
