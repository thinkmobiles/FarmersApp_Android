package com.farmers.underground.remote.services;

import com.farmers.underground.remote.models.LastCropPricesModel;
import retrofit.Call;
import retrofit.http.GET;

import java.util.ArrayList;

/**
 * Created by tZpace
 * on 06-Oct-15.
 */
public interface PricesService {

    @GET("prices/getLast")
    Call<ArrayList<LastCropPricesModel>> getLast(); //todo

}
