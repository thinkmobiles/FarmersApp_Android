package com.farmers.underground.ui.models;

import com.farmers.underground.remote.models.MarketeerPriceModel;
import com.farmers.underground.ui.adapters.MarketeerPricesAdapter;

/**
 * Created by omar
 * on 10/2/15.
 */
public class PriceMarketeerPricesDH {

    private MarketeerPriceModel model;
    private MarketeerPricesAdapter.Callback callback;

    public MarketeerPriceModel getModel() {
        return model;
    }

    public void setModel(MarketeerPriceModel model) {
        this.model = model;
    }

    public MarketeerPricesAdapter.Callback getCallback() {
        return callback;
    }

    public void setCallback(MarketeerPricesAdapter.Callback callback) {
        this.callback = callback;
    }

    public int getDateHashCode(){
        return model.getDate().hashCode();
    }
}
