package com.farmers.underground.ui.utils;

import android.content.Context;
import com.farmers.underground.R;

import java.util.Calendar;

/**
 * Created by omar on 10/14/15.
 */
public class DateHelper {

    private   String[] monthNames;
    public DateHelper(Context context){
        String[] monthNames = {context.getString(R.string.month1),
                context.getString(R.string.month2),
                context.getString(R.string.month3),
                context.getString(R.string.month4),
                context.getString(R.string.month5),
                context.getString(R.string.month6),
                context.getString(R.string.month7),
                context.getString(R.string.month8),
                context.getString(R.string.month9),
                context.getString(R.string.month10),
                context.getString(R.string.month11),
                context.getString(R.string.month12)};
    this.monthNames = monthNames;

    }

    public static DateHelper getInstance(Context context){
        return   new DateHelper(context);
    }

    public String [] getDate(long millis){
        String [] date = new String[3];

        Calendar c = Calendar.getInstance();
        c.setTimeInMillis(millis);
        int mYear = c.get(Calendar.YEAR);
        int mMonth = c.get(Calendar.MONTH);
        int mDay = c.get(Calendar.DAY_OF_MONTH);



        date[0] =String.valueOf( mDay);
        date[1] = monthNames[mMonth];
        date[2] =String.valueOf( mYear);
        return date;
    }

}
