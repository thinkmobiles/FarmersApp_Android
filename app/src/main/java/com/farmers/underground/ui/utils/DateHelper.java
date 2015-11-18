package com.farmers.underground.ui.utils;

import android.content.Context;

import com.farmers.underground.R;
import com.farmers.underground.config.ProjectConstants;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Created by omar
 * on 10/14/15.
 */
public class DateHelper {

    private String[] monthNames;

    public DateHelper(Context context) {
        this.monthNames = context.getResources().getStringArray(R.array.all_month);

    }

    public static DateHelper getInstance(Context context) {
        return new DateHelper(context);
    }

    public String[] getDate(long millis) {
        String[] date = new String[3];

        Calendar c = Calendar.getInstance();
        c.setTimeInMillis(millis);
        int mYear = c.get(Calendar.YEAR);
        int mMonth = c.get(Calendar.MONTH);
        int mDay = c.get(Calendar.DAY_OF_MONTH);


        date[0] = String.valueOf(mDay);
        date[1] = monthNames[mMonth];
        date[2] = String.valueOf(mYear);
        return date;
    }

    public static Calendar parseToCalendar(String date){
        SimpleDateFormat format = new SimpleDateFormat(ProjectConstants.SERVER_DATE_FORMAT, Locale.getDefault());
        Calendar calendar = Calendar.getInstance();
        try {
            calendar.setTime(format.parse(date));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return calendar;
    }

    /* 22.2.15 month*/
    public static String parseToStickyHeaderFromat(String date) {

        SimpleDateFormat formatIN = new SimpleDateFormat(ProjectConstants.SERVER_DATE_FORMAT, Locale.getDefault());
        SimpleDateFormat formatOUT = new SimpleDateFormat(ProjectConstants.STICKY_HEADER_DATE_FORMAT, Locale.getDefault());

        try {
          date = formatOUT.format(formatIN.parse(date));
        } catch (Exception e) {
           return date;
        }
        return date;
    }
}
