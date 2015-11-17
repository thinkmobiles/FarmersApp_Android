package com.farmers.underground.ui.models;

import com.farmers.underground.remote.models.CropPricesByDateModel;
import com.farmers.underground.ui.adapters.AllPricesAdapter;

/**
 * Created by omar
 * on 10/2/15.
 */
public class AllPricesDH {

    private CropPricesByDateModel model;
    private AllPricesAdapter.AllPricesCallback callback;

    public AllPricesDH(CropPricesByDateModel model, AllPricesAdapter.AllPricesCallback callback) {
        this.model = model;
        this.callback = callback;
    }

    public AllPricesAdapter.AllPricesCallback getCallback() {
        return callback;
    }

    public CropPricesByDateModel getModel() {
        return model;
    }
}
