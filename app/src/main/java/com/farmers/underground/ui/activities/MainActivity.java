package com.farmers.underground.ui.activities;

import android.content.Intent;
import android.os.Bundle;

import butterknife.ButterKnife;
import butterknife.OnClick;
import com.farmers.underground.R;
import com.farmers.underground.ui.base.BaseActivity;

/**
 * Created by tZpace
 * on 24-Sep-15.
 */
public class MainActivity extends BaseActivity {

    Intent intent;

    @Override
    public int getLayoutResId() {
        return R.layout.activity_main;
    }

    @Override
    protected int getFragmentContainerId() {
        return 0;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.tv_tutorialAct)
    void startTutorialActivity(){
        intent= new Intent(this, TutorialActivity.class);
               startActivity(intent);
    }

    @OnClick(R.id.tv_loginAct)
    void startLoginActivity(){
        intent = new Intent(this, LoginSignUpActivity.class);
        startActivity(intent);
    }
}
