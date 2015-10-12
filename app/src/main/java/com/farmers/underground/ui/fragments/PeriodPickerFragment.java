package com.farmers.underground.ui.fragments;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.farmers.underground.R;
import com.farmers.underground.ui.activities.AddPriceActivity;
import com.farmers.underground.ui.base.BaseFragment;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by samson on 12.10.15.
 */
public class PeriodPickerFragment extends BaseFragment<AddPriceActivity> {

    @Bind(R.id.tvPeriodFrom)
    protected TextView tvPeriodFrom;

    @Bind(R.id.tvPeriodTo)
    protected TextView tbPeriodTo;

    @Override
    protected int getLayoutResId() {
        return R.layout.fragment_picker_period;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
    }


}
