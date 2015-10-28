package com.farmers.underground.remote.models.base;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by tZpace
 * on 14-Oct-15.
 */
public class PriceBase implements Serializable {

    public Source source;
    public Double price;
    public String quality;
    public String data;
    public ArrayList<More> more;

    public class Source {
        public String type;
        public String name;
    }

    public class More {
        public Double price;
        public String quality;
    }
}
