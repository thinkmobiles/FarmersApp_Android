package com.farmers.underground.remote.models.base;

import java.util.ArrayList;

/**
 * Created by tZpace
 * on 14-Oct-15.
 */
public class PieceBase {

    public Source source;
    public String value;
    public String data;
    public ArrayList<More> more;

    private class Source {
        public String type;
        public String name;
    }

    private class More {
         public String minPrice ;
         public String maxPrice ;
         public String avgPrice ;
         public String site ;
         public String name ;
         public String date ;
    }
}
