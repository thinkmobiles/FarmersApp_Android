package com.farmers.underground.ui.activities;

import android.os.Bundle;
import android.widget.FrameLayout;

import com.farmers.underground.R;
import com.farmers.underground.ui.base.BaseActivity;
import com.farmers.underground.ui.fragments.LoginFragment;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by tZpace
 * on 25-Sep-15.
 */
public class LoginSignUpActivity extends BaseActivity {

    /*@Bind(R.id.fr_containrer_LSA)
    protected FrameLayout container;*/

    @Override
    public int getLayoutResId() {
        return R.layout.activity_login_signup;
    }

    @Override
    protected int getFragmentContainerId() {
        return R.id.fr_containrer_LSA;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);

        getSupportFragmentManager().beginTransaction()
                .replace(getFragmentContainerId(), new LoginFragment())
                .commit();
    }

}
