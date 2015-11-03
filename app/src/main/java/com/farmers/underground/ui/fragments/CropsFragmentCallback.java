package com.farmers.underground.ui.fragments;

import com.farmers.underground.remote.models.LastCropPricesModel;
import com.farmers.underground.ui.adapters.CropsListAdapter;

import java.util.List;

/**
 * Created by omar
 * on 10/1/15.
 */
public interface CropsFragmentCallback {
    void setListCallback(CropsListAdapter.CropsAdapterCallback callback);
    void onReceiveCrops(List<LastCropPricesModel> cropsList, String query);
}
