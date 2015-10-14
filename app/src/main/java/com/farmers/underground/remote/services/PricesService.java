package com.farmers.underground.remote.services;

import com.farmers.underground.remote.models.LastCropPriecesModel;

import java.util.ArrayList;

import retrofit.Call;
import retrofit.http.GET;

/**
 * Created by tZpace
 * on 06-Oct-15.
 */
public interface PricesService {

    @GET("prices/getLast")
    Call<ArrayList<LastCropPriecesModel>> getLast(); //todo

}
