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

        Intent intent;
        //run Tutorial test
//        intent= new Intent(this, TutorialActivity.class);
//        startActivity(intent);

        //run LoginSignUp test
        intent = new Intent(this, LoginSignUpActivity.class);
        startActivity(intent);
    }
}
