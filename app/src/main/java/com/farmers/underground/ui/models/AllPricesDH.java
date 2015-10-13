package com.farmers.underground.ui.models;

import com.farmers.underground.remote.models.CropModel;
import com.farmers.underground.ui.adapters.AllPricesAdapter;

/**
 * Created by omar
 * on 10/2/15.
 */
public class AllPricesDH {

    private CropModel model;

    private AllPricesAdapter.AllPricesCallback callback;


    public AllPricesDH(CropModel model, AllPricesAdapter.AllPricesCallback callback) {
        this.model = model;
        this.callback = callback;
    }

    public CropModel getModel() {
        return model;
    }

    public void setModel(CropModel model) {
        this.model = model;
    }

    public AllPricesAdapter.AllPricesCallback getCallback() {
        return callback;
    }
}
