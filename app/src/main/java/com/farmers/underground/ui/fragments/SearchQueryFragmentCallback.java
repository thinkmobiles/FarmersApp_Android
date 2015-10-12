package com.farmers.underground.ui.fragments;

import com.farmers.underground.remote.models.CropModel;
import com.farmers.underground.ui.adapters.CropsListAdapter;

import java.util.List;

/**
 * Created by omar
 * on 10/1/15.
 */
public interface SearchQueryFragmentCallback {
    void setListCallback(CropsListAdapter.CropsAdapterCallback callback);
    void onReceiveStringQuery(String query);
    void onReceiveCrops(List<CropModel> cropsList);
    void notifyFavsRefresh();
}
