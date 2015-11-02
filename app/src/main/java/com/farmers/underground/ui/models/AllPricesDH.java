package com.farmers.underground.ui.models;

import com.farmers.underground.remote.models.LastCropPricesModel;
import com.farmers.underground.remote.models.PricesByDateModel;
import com.farmers.underground.ui.adapters.AllPricesAdapter;

/**
 * Created by omar
 * on 10/2/15.
 */
public class AllPricesDH {

    private PricesByDateModel model2;
    private AllPricesAdapter.AllPricesCallback callback;

    public AllPricesDH(PricesByDateModel model2, AllPricesAdapter.AllPricesCallback callback) {
        this.model2 = model2;
        this.callback = callback;
    }

    public AllPricesAdapter.AllPricesCallback getCallback() {
        return callback;
    }

    public PricesByDateModel getModel2() {
        return model2;
    }
}
