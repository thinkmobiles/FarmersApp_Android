package com.farmers.underground.ui.utils;

import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.ColorInt;
import android.support.annotation.ColorRes;
import android.support.annotation.DrawableRes;

import com.farmers.underground.FarmersApp;

/**
 * Created by tZpace
 * on 19-Oct-15.
 */
public final class ResUtil {

    @ColorInt
    @SuppressWarnings("deprecation")
    public static int getColor(final Resources res,@ColorRes int id){
        if(Build.VERSION.SDK_INT >= 23) {
            return res.getColor(id, FarmersApp.getInstance().getTheme());
        } else {
            return res.getColor(id);
        }
    }

    @SuppressWarnings("deprecation")
    public static Drawable getDrawable(final Resources res,@DrawableRes int id){
        if(Build.VERSION.SDK_INT >= 21) {
            return res.getDrawable(id, FarmersApp.getInstance().getTheme());
        } else {
            return res.getDrawable(id);
        }
    }
}
