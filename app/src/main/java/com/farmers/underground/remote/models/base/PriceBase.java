package com.farmers.underground.remote.models.base;

import java.util.ArrayList;

/**
 * Created by tZpace
 * on 14-Oct-15.
 */
public class PriceBase {

    public Source source;
    public String price;
    public String quality;
    public String data;
    public ArrayList<More> more;

    public class Source {
        public String type;
        public String name;
    }

    public class More {
        public String price;
        public String quality;
    }
}
