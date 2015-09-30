package com.farmers.underground.ui.fragments;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnItemClick;
import com.farmers.underground.R;
import com.farmers.underground.ui.activities.LoginSignUpActivity;
import com.farmers.underground.ui.adapters.PickMarketeerAdapter;
import com.farmers.underground.ui.base.BaseFragment;

import java.util.ArrayList;

/**
 * Created by tZpace
 * on 25-Sep-15.
 */
public class LoginAfterRegistrationBFragment extends BaseFragment<LoginSignUpActivity>   {

    @Bind(R.id.tvCounter)
    protected TextView tvCounter;
    @Bind(R.id.etMarketeer)
    protected EditText etMarketeer;
    @Bind(R.id.lvListMarketeers)

    protected ListView lvMarketeers;
    private PickMarketeerAdapter mAdapter;

    @Override
    protected int getLayoutResId() {
        return R.layout.fragment_after_reg_b;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this,view);
        setAdapter();
    }

    private void setAdapter(){
        mAdapter = new PickMarketeerAdapter(getHostActivity(), new ArrayList<String>());
        lvMarketeers.setAdapter(mAdapter);
    }

   @OnItemClick(R.id.lvListMarketeers)
    protected void onListItemClicked(int pos){}
}