package com.farmers.underground.ui.utils;

import android.content.Context;
import android.content.res.Resources;
import android.util.DisplayMetrics;
import android.util.TypedValue;

/**
 * Created by omar
 * on 23.06.15.
 */
public final class ResourceRetriever {

    public static int retrieveDIP(Context context, int resource){
        Resources resources = context.getResources();
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, resources.getDimension(resource), resources.getDisplayMetrics());

    }
    public static int retrieveSP(Context context, int resource){
        Resources resources = context.getResources();
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, resources.getDimension(resource), resources.getDisplayMetrics());
    }

    public static int retrievePX(Context context, int resource) {
        Resources resources = context.getResources();
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_PX, resources.getDimension(resource), resources.getDisplayMetrics());
    }

    public static int dpToPx(Context context, int dp) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        return Math.round(dp * (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT));
    }

    public static int dpToPxRes(Context context, int resource) {
        Resources resources = context.getResources();
        return  resources.getDimensionPixelSize(resource);
    }
}
