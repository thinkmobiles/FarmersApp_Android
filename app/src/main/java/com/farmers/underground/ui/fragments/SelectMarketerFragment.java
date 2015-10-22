package com.farmers.underground.ui.fragments;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.View;
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

/**
 * Created by tZpace
 * on 25-Sep-15.
 */
public class SelectMarketerFragment extends BaseFragment<LoginSignUpActivity> {

    private static final String KEY_CHANGE_MARKETER = "change_marketer";

    @Bind(R.id.tvEnterMarketeer)
    protected TextView tvNameMarketer;

    @Bind(R.id.tvLetsGo)
    protected TextView tvGoOrSave;

    private boolean isChangingMarketer;

    public static SelectMarketerFragment newInstance(boolean forChanging){
        SelectMarketerFragment fragment = new SelectMarketerFragment();
        Bundle bundle = new Bundle();
        bundle.putBoolean(KEY_CHANGE_MARKETER, forChanging);
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
        isChangingMarketer = getArguments().getBoolean(KEY_CHANGE_MARKETER);
        ButterKnife.bind(this, view);
        setNameMarketer();
    }

    private void setNameMarketer() {
        if(isChangingMarketer){
            getHostActivity().getUserMarketer();
        }
        String name = getHostActivity().getNameMarketeer();
        if (name != null) {
            tvNameMarketer.setText(name);
        }
    }

    @OnClick(R.id.tvEnterMarketeer)
    protected void enterMarketeer() {
        if(isChangingMarketer){
            getHostActivity().showChangingMarketerDialog();
        } else {
            getHostActivity().switchFragment(SelectMarketerListFragment.class.getName(), true);
        }
    }

    @OnClick(R.id.tvSkip)
    protected void skip() {
        FarmersApp.setSkipMode(FarmersApp.isSkipMode());
        MainActivity.start(getHostActivity());
        getHostActivity().finish();
    }

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

                    FarmersApp.setSkipMode(true);
                    MainActivity.start(getHostActivity());
                    getHostActivity().finish();
                }

                @Override
                public void onError(@NonNull ErrorMsg error) {
                    getHostActivity().showToast(error.getErrorMsg(), Toast.LENGTH_SHORT);
                }
            });
        }

    }
}
