package com.farmers.underground.ui.utils;

import android.content.Context;
import com.farmers.underground.config.ProjectConstants;
import com.squareup.picasso.LruCache;
import com.squareup.picasso.Picasso;

/**
 * Created by omar
 * on 10/23/15.
 */
public class PicassoHelper {


    private static Picasso p;


    public static Picasso getPicasso(Context context) {
        if (p == null)
            p = new Picasso.Builder(context)
                    .memoryCache(new LruCache(ProjectConstants.MAX_RAM_CACHE_SIZE))
                    .build();
        return p;
    }
}
