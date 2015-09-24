package com.farmers.underground.ui.base;

import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by tZpace
 * on 24-Sep-15.
 */
public abstract class BaseActivity extends AppCompatActivity {

    @LayoutRes
    public abstract int getLayoutResId();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //TODO: maybe use some default layout for activity
        if(getLayoutResId() == 0)
            throw new Error("WTF! add/override getLayoutResId.");

        setContentView(getLayoutResId());
    }

    //TODO: switch fragment methods;
}