package com.farmers.underground.ui.fragments;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.farmers.underground.R;
import com.farmers.underground.ui.activities.LoginSignUpActivity;
import com.farmers.underground.ui.base.BaseFragment;

/**
 * Created by tZpace
 * on 25-Sep-15.
 */
public class LoginAfterRegistrationAFragment extends BaseFragment<LoginSignUpActivity> implements View.OnClickListener {

    private TextView tvEnterMarketeer, tvSkip;
    private LinearLayout llStart;

    @Override
    protected int getLayoutResId() {
        return R.layout.fragment_after_reg_a;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        findViews(view);
        setListeners();
    }

    private void findViews(View view){
        tvEnterMarketeer = (TextView) view.findViewById(R.id.tvEnterMarketeer);
        tvSkip = (TextView) view.findViewById(R.id.tvSkip);
        llStart = (LinearLayout) view.findViewById(R.id.btnStart);
    }

    private void setListeners(){
        tvEnterMarketeer.setOnClickListener(this);
        tvSkip.setOnClickListener(this);
        llStart.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tvEnterMarketeer:
                break;
            case R.id.tvSkip:
                break;
            case R.id.btnStart:
                break;
        }
    }
}
