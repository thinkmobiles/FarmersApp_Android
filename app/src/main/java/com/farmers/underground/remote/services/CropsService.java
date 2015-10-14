package com.farmers.underground.remote.services;

import com.farmers.underground.remote.models.base.CropBase;
import retrofit.Call;
import retrofit.http.GET;
/**
 * Created by tZpace
 * on 06-Oct-15.
 */
public interface CropsService {

    @GET("crops")
    Call<CropBase> getCropList(); //todo

}
