package com.farmers.underground.remote.services;

import com.farmers.underground.remote.models.base.MarketeerBase;
import com.farmers.underground.remote.models.SuccessMsg;

import java.util.ArrayList;

import retrofit.Call;
import retrofit.http.Field;
import retrofit.http.FormUrlEncoded;
import retrofit.http.GET;
import retrofit.http.POST;

/**
 * Created by tZpace
 * on 06-Oct-15.
 */
public interface MarketeerService {

    @GET("marketeers")
    Call<ArrayList<String>> getMarketterList();

    @GET("marketeers")
    Call<MarketeerBase> getMarketeerBySession(); //todo

    @FormUrlEncoded
    @POST("marketeers")
    Call<SuccessMsg> addMarketeer(@Field("fullName") String fullName);

}
