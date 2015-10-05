package com.farmers.underground.ui.fragments;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnItemClick;
import butterknife.OnTextChanged;

import com.farmers.underground.FarmersApp;
import com.farmers.underground.R;
import com.farmers.underground.ui.activities.LoginSignUpActivity;
import com.farmers.underground.ui.activities.MainActivity;
import com.farmers.underground.ui.adapters.PickMarketeerAdapter;
import com.farmers.underground.ui.base.BaseFragment;

import java.io.File;
import java.util.Arrays;
import java.util.List;

/**
 * Created by tZpace
 * on 25-Sep-15.
 */
public class SelectMarketerListFragment extends BaseFragment<LoginSignUpActivity> implements PickMarketeerAdapter.OnFindMarketerListener {

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
        mAdapter = new PickMarketeerAdapter(getHostActivity(), listMarketeers, this);
        lvMarketeers.setAdapter(mAdapter);
    }

    @OnTextChanged(R.id.etMarketeer)
    protected void changeNameMarketer(){
        mAdapter.findMarketeer(etMarketeer.getText().toString());
    }

   @OnItemClick(R.id.lvListMarketeers)
    protected void onListItemClicked(int pos){
       int size = mAdapter.getCount();
       if(pos == size - 1 && size != listMarketeers.size()){
           sendRequestAddNewMarketer();
       } else {
           selectMarketer(pos);
       }
    }

    private void sendRequestAddNewMarketer(){
        // todo requst for create new marketer
        getHostActivity().showToast("send new marketer", Toast.LENGTH_SHORT);
    }

    private void selectMarketer(int posMarketer){
        getHostActivity().setNameMarketeer(mAdapter.getItem(posMarketer));
        getHostActivity().popBackStackUpTo(getClass());
        getHostActivity().switchFragment(SelectMarketerFragment.class.getName(), false);
        getHostActivity().hideSoftKeyboard();
    }

    @Override
    public void onFind(int countMarketer) {
        tvCounter.setText(String.valueOf(countMarketer + File.separator + listMarketeers.size()));
    }

    @OnClick(R.id.tvSkip_FLM)
    protected void onSkip(){
        FarmersApp.setSkipMode(true);
        getHostActivity().finish();
        MainActivity.start(getHostActivity());
    }
}