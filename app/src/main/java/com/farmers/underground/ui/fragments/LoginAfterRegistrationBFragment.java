package com.farmers.underground.ui.fragments;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnItemClick;
import butterknife.OnTextChanged;

import com.farmers.underground.R;
import com.farmers.underground.ui.activities.LoginSignUpActivity;
import com.farmers.underground.ui.adapters.PickMarketeerAdapter;
import com.farmers.underground.ui.base.BaseFragment;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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

    //test list of marketers
    List<String> listMarketeers = Arrays.asList("first", "second", "third", "forth", "fifth", "sixth", "seventh", "fiftieth");

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
        mAdapter = new PickMarketeerAdapter(getHostActivity(), listMarketeers);
        lvMarketeers.setAdapter(mAdapter);
    }

    @OnTextChanged(R.id.etMarketeer)
    protected void changeNameMarketer(){
        mAdapter.findMarketeer(etMarketeer.getText().toString());
    }

   @OnItemClick(R.id.lvListMarketeers)
    protected void onListItemClicked(int pos){
       getHostActivity().setNameMarketeer(mAdapter.getItem(pos));
       getHostActivity().switchFragment(LoginAfterRegistrationAFragment.class.getName(), false);
   }
}