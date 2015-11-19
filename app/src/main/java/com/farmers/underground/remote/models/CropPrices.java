package com.farmers.underground.remote.models;

import com.farmers.underground.remote.models.base.PriceBase;

import java.io.Serializable;

/**
 * Created by tZpace
 * on 17-Nov-15.
 */
public class CropPrices extends PriceBase {

    public Source source;
    public String data;

    public class Source implements Serializable {
        public String type;
        public String name;
    }
}
