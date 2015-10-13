package com.farmers.underground.ui.dialogs;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.farmers.underground.R;
import com.farmers.underground.ui.activities.TransparentActivity;
import com.farmers.underground.ui.base.BaseFragment;

import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by tZpace
 * on 12-Oct-15.
 */
public class WhyCanISeeThisPriceDialogFragment extends BaseFragment<TransparentActivity> {

    @Override
    protected int getLayoutResId() {
        return R.layout.fragment_dialog_why_can_i_see_this_price;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);

    }

    @OnClick(R.id.tvCancel_WhyDiag)
    protected void onCancel(){
        getHostActivity().finish();
    }

    @OnClick(R.id.tvAddPrice_WhyDiag)
    protected void addPrice(){
        getHostActivity().setResult(Activity.RESULT_OK, new Intent());
        getHostActivity().finish();
    }
}
