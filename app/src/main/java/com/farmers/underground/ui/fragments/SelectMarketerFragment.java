package com.farmers.underground.ui.fragments;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

import com.farmers.underground.FarmersApp;
import com.farmers.underground.R;
import com.farmers.underground.ui.activities.LoginSignUpActivity;
import com.farmers.underground.ui.activities.MainActivity;
import com.farmers.underground.ui.base.BaseFragment;

/**
 * Created by tZpace
 * on 25-Sep-15.
 */
public class SelectMarketerFragment extends BaseFragment<LoginSignUpActivity>   {

    @Bind(R.id.tvEnterMarketeer)
    protected TextView tvNameMarketer;

    @Override
    protected int getLayoutResId() {
        return R.layout.fragment_after_reg_a;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
        setNameMarketer();
    }

    private void setNameMarketer(){
        String name = getHostActivity().getNameMarketeer();
        if(name != null){
            tvNameMarketer.setText(name);
        }
    }

    @OnClick(R.id.tvEnterMarketeer)
    protected void enterMarketeer(){
        getHostActivity().switchFragment(SelectMarketerListFragment.class.getName(), true);
    }

    @OnClick(R.id.tvSkip)
    protected void skip(){
        FarmersApp.setSkipMode(true);
        getHostActivity().finish();
        MainActivity.start(getHostActivity());
    }

    @OnClick(R.id.btnStart)
    protected void start(){
        String name = getHostActivity().getNameMarketeer();
        if(name == null){
            getHostActivity().showToast("", Toast.LENGTH_SHORT);
        } else {
            // todo request and marketer in profile
            FarmersApp.setSkipMode(false);
            getHostActivity().finish();
            MainActivity.start(getHostActivity());
        }
    }


}
