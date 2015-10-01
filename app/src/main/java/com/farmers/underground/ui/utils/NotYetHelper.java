package com.farmers.underground.ui.utils;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by omar on 9/29/15.
 */
public class NotYetHelper {

    public static void notYetImplmented(Context context, String feature){
        Toast.makeText(context, feature + " not yet implemented", Toast.LENGTH_SHORT).show();
    }

}
