package com.farmers.underground.ui.fragments;

import android.os.Bundle;
import android.view.View;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.farmers.underground.R;
import com.farmers.underground.ui.activities.LoginSignUpActivity;
import com.farmers.underground.ui.base.BaseFragment;

/**
 * Created by tZpace
 * on 25-Sep-15.
 */
public class LoginAfterRegistrationAFragment extends BaseFragment<LoginSignUpActivity>   {

    @Override
    protected int getLayoutResId() {
        return R.layout.fragment_after_reg_a;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);

    }

    @OnClick(R.id.tvEnterMarketeer)
    protected void enterMarketeer(){
        getHostActivity().getSupportFragmentManager().beginTransaction()
                .replace(getHostActivity().getFragmentContainerId(), new LoginAfterRegistrationBFragment())
                .commit();
    }

    @OnClick(R.id.tvSkip)
    protected void skip(){}

    @OnClick(R.id.btnStart)
    protected void start(){}


}
