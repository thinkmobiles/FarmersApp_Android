package com.farmers.underground.ui.activities;

import android.content.Intent;
import android.os.Bundle;

import com.farmers.underground.R;
import com.farmers.underground.ui.base.BaseActivity;

/**
 * Created by tZpace
 * on 24-Sep-15.
 */
public class MainActivity extends BaseActivity {

    @Override
    public int getLayoutResId() {
        return R.layout.activity_main;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        //run TUTORIAL test
        Intent intent = new Intent(this, TutorialActivity.class);
        startActivity(intent);
    }
}
