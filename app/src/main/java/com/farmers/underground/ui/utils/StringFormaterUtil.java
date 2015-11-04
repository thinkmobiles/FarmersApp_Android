package com.farmers.underground.ui.utils;

import com.farmers.underground.config.ProjectConstants;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

/**
 * Created by samson
 * on 14.10.15.
 */
public class StringFormaterUtil {

    public static String convertDate(Calendar date){
        return new SimpleDateFormat("ccc dd.M.yy", Locale.getDefault()).format(date.getTime());
    }

    public static String parseToServerResponse(Calendar date){
        return new SimpleDateFormat(ProjectConstants.SERVER_DATE_FORMAT, Locale.getDefault()).format(date.getTime());
    }

    public static String getLettersForLogo(String name){
        return name.substring(0, 2);
    }

    public static String parsePrice(Double price){
        return price != 0 ? String.format("%.2f", price) : "- -";
    }
}
