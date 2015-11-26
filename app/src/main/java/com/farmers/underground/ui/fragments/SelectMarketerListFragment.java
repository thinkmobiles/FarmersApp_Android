package com.farmers.underground.ui.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.TextUtils;
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
import com.farmers.underground.remote.RetrofitSingleton;
import com.farmers.underground.remote.models.ErrorMsg;
import com.farmers.underground.remote.models.SuccessMsg;
import com.farmers.underground.remote.util.ACallback;
import com.farmers.underground.ui.activities.LoginSignUpActivity;
import com.farmers.underground.ui.activities.MainActivity;
import com.farmers.underground.ui.adapters.PickMarketeerAdapter;
import com.farmers.underground.ui.base.BaseFragment;
import com.farmers.underground.ui.utils.AnalyticsTrackerUtil;

import java.io.File;
import java.util.ArrayList;
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

    List<String> listMarketeers = new ArrayList<String>(0);


    public static SelectMarketerListFragment newInstance(Bundle args){
        SelectMarketerListFragment fragment = new SelectMarketerListFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.fragment_after_reg_b;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);

        etMarketeer.requestFocus();
        getHostActivity().showSoftKeyboard(etMarketeer);
        RetrofitSingleton.getInstance().getMarketterList(
                new ACallback<ArrayList<String>, ErrorMsg>() {
                    @Override
                    public void onSuccess(ArrayList<String> result) {
                        if (result != null) {
                            setAdapter(result);
                        } else
                            onError(new ErrorMsg("No marketeers fetched"));
                    }

                    @Override
                    public void onError(@NonNull ErrorMsg error) {
                        getHostActivity().showToast(error.getErrorMsg(), Toast.LENGTH_SHORT);
                    }
                });


    }

    private void setAdapter(ArrayList<String> listMarketeers){
        this.listMarketeers = listMarketeers;
        mAdapter = new PickMarketeerAdapter(getHostActivity(), this.listMarketeers, this);
        lvMarketeers.setAdapter(mAdapter);
    }

    @SuppressWarnings("unused")
    @OnTextChanged(R.id.etMarketeer)
    protected void changeNameMarketer(){
        if (mAdapter!=null)
            mAdapter.findMarketeer(etMarketeer.getText().toString());
    }

    @SuppressWarnings("unused")
    @OnItemClick(R.id.lvListMarketeers)
    protected void onListItemClicked(int pos){
       if(mAdapter.isAddItem(pos)){
           sendRequestAddNewMarketer();
       } else {
           selectMarketer(pos);
       }
    }

    @SuppressWarnings("unused")
    @OnClick(R.id.tvSkip_FLM)
    protected void onSkip(){
        //track skip after registration on GoogleAnalitycs
        if(getArguments().getBoolean(SelectMarketerFragment.KEY_AFTER_REG))
            AnalyticsTrackerUtil.getInstance().trackEvent(AnalyticsTrackerUtil.TypeEvent.MarketerSkip);

        FarmersApp.setSkipMode(false/*FarmersApp.isSkipMode()*/);
        getHostActivity().finish();
        MainActivity.start(getHostActivity());
    }

    private void sendRequestAddNewMarketer(){
        //   request for create new marketer

        if (!TextUtils.isEmpty(etMarketeer.getText().toString())){
            RetrofitSingleton.getInstance().addMarketeer(etMarketeer.getText().toString(), new ACallback<SuccessMsg, ErrorMsg>() {
                @Override
                public void onSuccess(SuccessMsg result) {

                    FarmersApp.setSkipMode(true);
                    getHostActivity().finish();
                    MainActivity.start(getHostActivity());

                    getHostActivity().showToast(result.getSuccessMsg(), Toast.LENGTH_SHORT);
                }

                @Override
                public void onError(@NonNull ErrorMsg error) {
                    getHostActivity().showToast(error.getErrorMsg(), Toast.LENGTH_SHORT);
                }
            });
        }

      //  getHostActivity().showToast("send new marketer", Toast.LENGTH_SHORT);
    }

    private void selectMarketer(int posMarketer){
        getHostActivity().setNameMarketeer(mAdapter.getItem(posMarketer));
        getHostActivity().popBackStackUpTo(getClass());
        getHostActivity().hideSoftKeyboard();
        if(SelectMarketerFragment.Reason.values()[getArguments().getInt(SelectMarketerFragment.KEY_REASON)] == SelectMarketerFragment.Reason.FirstAddition){
            getHostActivity().switchFragment(SelectMarketerFragment.newInstance(SelectMarketerFragment.Reason.FirstAddition), false);
        } else {
            getHostActivity().switchFragment(SelectMarketerFragment.newInstance(SelectMarketerFragment.Reason.Accept), false);
        }
    }

    @Override
    public void onFind(int countMarketer) {
        if(countMarketer != listMarketeers.size())
            --countMarketer;
        tvCounter.setText(String.valueOf(countMarketer + File.separator + listMarketeers.size()));
    }

}