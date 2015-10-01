package com.farmers.underground.ui.dialogs;

import android.content.Context;
import android.support.v7.app.AlertDialog;

/**
 * Created by tZpace
 * on 01-Oct-15.
 */
public class CommonDialog extends AlertDialog {

    protected CommonDialog(Context context) {
        super(context);
    }

    protected CommonDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    protected CommonDialog(Context context, int theme) {
        super(context, theme);
    }
}
