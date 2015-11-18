package com.farmers.underground.ui.dialogs;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.LinearLayout;

import com.farmers.underground.R;
import com.farmers.underground.config.ProjectConstants;
import com.farmers.underground.ui.activities.LoginSignUpActivity;
import com.farmers.underground.ui.activities.TransparentActivity;
import com.farmers.underground.ui.base.BaseFragment;
import com.farmers.underground.ui.custom_views.CustomTextView;
import com.farmers.underground.ui.utils.DateHelper;

import java.text.SimpleDateFormat;
import java.util.Locale;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by tZpace
 * on 12-Oct-15.
 */
public class WhyCanIAddThisPriceDialogFragment extends BaseFragment<TransparentActivity> {

    @Bind(R.id.llDateAdd)
    protected LinearLayout dateContainer;

    @Bind(R.id.tv_DayCropsItem_D)
    protected CustomTextView day;

    @Bind(R.id.tv_MonthCropsCalendar)
    protected CustomTextView month;

    @Bind(R.id.tv_YearCropsItem)
    protected CustomTextView year;

    @Override
    protected int getLayoutResId() {
        return R.layout.fragment_dialog_why_can_i_add_this_price;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);

        final String dateF = getArguments().getString("Date", null);
        if(dateF == null){
            dateContainer.setVisibility(View.GONE);
        } else {
            SimpleDateFormat format = new SimpleDateFormat(ProjectConstants.SERVER_DATE_FORMAT, Locale.getDefault());
            try {
                long time = format.parse(dateF).getTime();
                String[] date = DateHelper.getInstance(getHostActivity()).getDate(time);
                day.setText(date[0]);
                month.setText(date[1]);
                year.setText(date[2]);

            } catch (Exception e) {
                e.printStackTrace();
                dateContainer.setVisibility(View.GONE);
            }
        }

    }

    @OnClick(R.id.tvCancel_WhyDiagADD)
    protected void onCancel(){
        getHostActivity().finish();
    }

    @OnClick(R.id.tvAddPrice_WhyDiagADD)
    protected void addPrice(){
        LoginSignUpActivity.startAddMarketier(getHostActivity());
        getHostActivity().finish();
    }
}
