package com.farmers.underground.remote.services;

import com.farmers.underground.remote.models.LastCropPricesModel;
import retrofit.Call;
import retrofit.http.GET;

import java.util.ArrayList;
import java.util.LinkedList;

/**
 * Created by tZpace
 * on 06-Oct-15.
 */
public interface PricesService {

    @GET("prices/getLast")
    Call<LinkedList<LastCropPricesModel>> getLast(); //todo

}
