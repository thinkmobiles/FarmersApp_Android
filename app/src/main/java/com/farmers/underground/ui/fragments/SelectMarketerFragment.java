package com.farmers.underground.ui.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

import com.farmers.underground.FarmersApp;
import com.farmers.underground.R;
import com.farmers.underground.remote.RetrofitSingleton;
import com.farmers.underground.remote.models.ErrorMsg;
import com.farmers.underground.remote.models.SuccessMsg;
import com.farmers.underground.remote.util.ACallback;
import com.farmers.underground.ui.activities.LoginSignUpActivity;
import com.farmers.underground.ui.activities.MainActivity;
import com.farmers.underground.ui.base.BaseFragment;
import com.farmers.underground.ui.utils.AnalyticsTrackerUtil;

/**
 * Created by tZpace
 * on 25-Sep-15.
 */
public class SelectMarketerFragment extends BaseFragment<LoginSignUpActivity> {
    @Bind(R.id.tvEnterMarketeer)
    protected TextView tvNameMarketer;

    @Bind(R.id.btnStart)
    protected LinearLayout llGoOrSave;

    @Bind(R.id.tvLetsGo)
    protected TextView tvGoOrSave;

    public static final String KEY_REASON = "position_reason";
    public static final String KEY_AFTER_REG = "after_registration";

    public enum Reason{FirstAddition, Add, Change, Accept}

    private Reason reason;

    private boolean afterReg = false;

    public static SelectMarketerFragment newInstance(Reason reason){
        SelectMarketerFragment fragment = new SelectMarketerFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(KEY_REASON, reason.ordinal());
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.fragment_after_reg_a;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getFlag();
        ButterKnife.bind(this, view);
        setNameMarketer2();
    }

    private void getFlag(){
        reason = Reason.values()[getArguments().getInt(KEY_REASON)];
    }

    private void setNameMarketer2() {
        getHostActivity().setIsChanging(false);
        switch (reason){
            case FirstAddition:
                afterReg = true;
                break;
            case Add:
                llGoOrSave.setVisibility(View.INVISIBLE);
                break;
            case Change:
                llGoOrSave.setVisibility(View.INVISIBLE);
                getHostActivity().setNameMarketeer(FarmersApp.getInstance().getCurrentMarketer().getFullName());
                getHostActivity().setIsChanging(true);
                break;
            case Accept:
                tvGoOrSave.setText(getString(R.string.dialog_accept));
                break;
        }
        String name = getHostActivity().getNameMarketeer();
        if(name != null) {
            tvNameMarketer.setText(name);
        }
    }

    @SuppressWarnings("unused")
    @OnClick(R.id.tvEnterMarketeer)
    protected void enterMarketeer() {
        Bundle args = getArguments();
        args.putBoolean(KEY_AFTER_REG, afterReg);
        getHostActivity().showChangingMarketerDialog(args);
    }

    @SuppressWarnings("unused")
    @OnClick(R.id.tvSkip)
    protected void skip() {
        //track skip after registration on GoogleAnalitycs
        if(afterReg)
            AnalyticsTrackerUtil.getInstance().trackEvent(AnalyticsTrackerUtil.TypeEvent.MarketerSkip);

        FarmersApp.setSkipMode(false/*FarmersApp.isSkipMode()*/);
        MainActivity.start(getHostActivity());
        getHostActivity().finish();
    }

    @SuppressWarnings("unused")
    @OnClick(R.id.btnStart)
    protected void start() {
        String name = getHostActivity().getNameMarketeer();
        if (TextUtils.isEmpty(name)) {
            getHostActivity().showDialogAboutAddMarketer();
        } else {
            //  request and marketer in profile
            RetrofitSingleton.getInstance().addMarketeer(name, new ACallback<SuccessMsg, ErrorMsg>() {
                @Override
                public void onSuccess(SuccessMsg result) {
                    getHostActivity().showToast(result.getSuccessMsg(), Toast.LENGTH_SHORT);

                    //track selection of marketer after registration on GoogleAnalitycs
                    if(afterReg)
                        AnalyticsTrackerUtil.getInstance().trackEvent(AnalyticsTrackerUtil.TypeEvent.MarketerChoosed);

                    FarmersApp.setSkipMode(false);
                    MainActivity.start(getHostActivity());
                    getHostActivity().finish();
                }

                @Override
                public void onError(@NonNull ErrorMsg error) {
                    getHostActivity().showToast(error.getErrorMsg(), Toast.LENGTH_SHORT);
                }
            });
            getHostActivity().getUserProfileAsync(); //for refresh state of having of marketer
        }

    }
}
