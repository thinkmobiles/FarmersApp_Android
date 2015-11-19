package com.farmers.underground.remote.models.base;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by tZpace
 * on 14-Oct-15.
 */
public class PriceBase implements Serializable {

    public Double price;
    public String quality;
    public ArrayList<More> more;

    public class More implements Serializable {
        public Double price;
        public String quality;
    }
}
