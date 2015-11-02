package com.farmers.underground.ui.models;

import com.farmers.underground.remote.models.PricesByDateModel;
import com.farmers.underground.ui.adapters.AllPricesAdapter;

/**
 * Created by omar
 * on 10/2/15.
 */
public class AllPricesDH {

    private PricesByDateModel model;
    private AllPricesAdapter.AllPricesCallback callback;

    public AllPricesDH(PricesByDateModel model, AllPricesAdapter.AllPricesCallback callback) {
        this.model = model;
        this.callback = callback;
    }

    public AllPricesAdapter.AllPricesCallback getCallback() {
        return callback;
    }

    public PricesByDateModel getModel() {
        return model;
    }
}
