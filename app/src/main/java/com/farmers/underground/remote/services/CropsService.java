package com.farmers.underground.remote.services;

import com.farmers.underground.remote.models.base.CropBase;

import java.util.List;

import retrofit.Call;
import retrofit.http.GET;
import retrofit.http.Query;

/**
 * Created by tZpace
 * on 06-Oct-15.
 */
public interface CropsService {

    @GET("crops")
    Call<CropBase> getCropList(); //todo

    @GET("crops/cropQualitys")
    Call<List<String>> cropQualitys(@Query("cropName") String cropName);
}
