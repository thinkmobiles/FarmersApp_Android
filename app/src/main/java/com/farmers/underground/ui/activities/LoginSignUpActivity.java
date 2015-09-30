package com.farmers.underground.ui.activities;

import android.os.Bundle;

import com.farmers.underground.R;
import com.farmers.underground.ui.base.BaseActivity;
import com.farmers.underground.ui.fragments.LoginFragment;

/**
 * Created by tZpace
 * on 25-Sep-15.
 */
public class LoginSignUpActivity extends BaseActivity {

    @Override
    public int getLayoutResId() {
        return R.layout.activity_login_signup;
    }

    @Override
    public int getFragmentContainerId() {
        return R.id.fr_containrer_LSA;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        switchFragment(LoginFragment.class.getName(), false);
    }

}
