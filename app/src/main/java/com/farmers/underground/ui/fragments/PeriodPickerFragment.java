package com.farmers.underground.ui.fragments;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.farmers.underground.R;
import com.farmers.underground.ui.activities.TransparentActivity;
import com.farmers.underground.ui.base.BaseFragment;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by samson on 12.10.15.
 */
public class PeriodPickerFragment extends BaseFragment<TransparentActivity> implements TransparentActivity.OnPickDateListener {

    @Bind(R.id.tvPeriodFrom)
    protected TextView tvPeriodFrom;

    @Bind(R.id.tvPeriodTo)
    protected TextView tvPeriodTo;

    private enum Period{From, To};
    private Period dayFromTo;
    private Calendar selectedDay;

    @Override
    protected int getLayoutResId() {
        return R.layout.fragment_picker_period;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);

        getHostActivity().setOnPickDateListener(this);
    }

    @OnClick(R.id.tvPeriodFrom)
    protected void pickFromDate(){
        pickDate(Period.From);
    }

    @OnClick(R.id.tvPeriodTo)
    protected void pickToDate(){
        pickDate(Period.To);
    }

    private void pickDate(Period period){
        dayFromTo = period;
        getHostActivity().showDatePicker();
    }

    @Override
    public void onPickDate(Calendar date) {
        if(selectedDay == null){
            selectedDay = date;
            setDate();
        } else{
            if((dayFromTo == Period.To && selectedDay.before(date))
                    || (dayFromTo == Period.From && selectedDay.after(date))){
                selectedDay = date;
                setDate();
                getHostActivity().close();
            } else {
                getHostActivity().showToast("Incorrect period", Toast.LENGTH_SHORT);
            }
        }
    }

    private void setDate(){
        if(dayFromTo == Period.From)
            tvPeriodFrom.setText(convertDate(selectedDay));
        else
            tvPeriodTo.setText(convertDate(selectedDay));
    }

    private String convertDate(Calendar date){
        return new SimpleDateFormat("ccc dd.M.yy").format(date.getTime());
    }
}
