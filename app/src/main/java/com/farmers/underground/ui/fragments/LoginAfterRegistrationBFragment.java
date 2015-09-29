package com.farmers.underground.ui.fragments;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.farmers.underground.R;
import com.farmers.underground.ui.activities.LoginSignUpActivity;
import com.farmers.underground.ui.adapters.PickMarketeerAdapter;
import com.farmers.underground.ui.base.BaseFragment;

import java.util.ArrayList;

/**
 * Created by tZpace
 * on 25-Sep-15.
 */
public class LoginAfterRegistrationBFragment extends BaseFragment<LoginSignUpActivity> implements AdapterView.OnItemClickListener {

    private TextView tvCounter;
    private EditText etMarketeer;
    private ListView lvMarketeers;
    private PickMarketeerAdapter mAdapter;

    @Override
    protected int getLayoutResId() {
        return R.layout.fragment_after_reg_b;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        findViews(view);
        setListeners();
        setAdapter();
    }

    private void findViews(View view) {
        tvCounter = (TextView) view.findViewById(R.id.tvCounter);
        etMarketeer = (EditText) view.findViewById(R.id.etMarketeer);
        lvMarketeers = (ListView) view.findViewById(R.id.lvListMarketeers);
    }

    private void setListeners(){
        lvMarketeers.setOnItemClickListener(this);
    }

    private void setAdapter(){
        mAdapter = new PickMarketeerAdapter(getHostActivity(), new ArrayList<String>());
        lvMarketeers.setAdapter(mAdapter);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }
}