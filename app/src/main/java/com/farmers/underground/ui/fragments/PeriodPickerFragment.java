package com.farmers.underground.ui.fragments;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

import com.farmers.underground.R;
import com.farmers.underground.ui.activities.PricesActivity;
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
public class PeriodPickerFragment extends BaseFragment<TransparentActivity> implements DatePickerDialog.OnDateSetListener {

    public static final String KEY_DATE_FROM = "date_from";
    public static final String KEY_DATE_TO = "date_to";

    @Bind(R.id.tvPeriodFrom)
    protected TextView tvPeriodFrom;

    @Bind(R.id.tvPeriodTo)
    protected TextView tvPeriodTo;

    private enum Period{From, To};
    private Period dayFromTo;
    private Calendar selectedDay, dateFrom, dateTo;

    @Override
    protected int getLayoutResId() {
        return R.layout.fragment_picker_period;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);

        setData();
    }

    private void setData(){
        dayFromTo = Period.From;
        dateFrom = Calendar.getInstance();
        tvPeriodFrom.setText(convertDate(dateFrom));
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
        showDatePicker();
    }

    public void onPickDate(Calendar date) {
        if(selectedDay == null){
            selectedDay = date;
            setDate();
        } else{
            if((dayFromTo == Period.To && selectedDay.before(date))
                    || (dayFromTo == Period.From && selectedDay.after(date))){
                selectedDay = date;
                setDate();
                sendPeriod();
            } else {
                getHostActivity().showToast("Incorrect period", Toast.LENGTH_SHORT);
            }
        }
    }

    private void setDate(){
        if(dayFromTo == Period.From) {
            tvPeriodFrom.setText(convertDate(selectedDay));
            dateFrom = selectedDay;
        } else {
            tvPeriodTo.setText(convertDate(selectedDay));
            dateTo = selectedDay;
        }
    }

    private void sendPeriod(){
        Bundle bundle = new Bundle();
        bundle.putSerializable(KEY_DATE_FROM, dateFrom);
        bundle.putSerializable(KEY_DATE_TO, dateTo);
        Intent intent = new Intent();
        intent.putExtras(bundle);
        getHostActivity().setResult(Activity.RESULT_OK, intent);
        getHostActivity().finish();
    }

    private String convertDate(Calendar date){
        return new SimpleDateFormat("ccc dd.M.yy").format(date.getTime());
    }

    public void showDatePicker(){
        new DatePickerDialog(getHostActivity(), this, dateFrom.get(Calendar.YEAR), dateFrom.get(Calendar.MONTH), dateFrom.get(Calendar.DAY_OF_MONTH))
                .show();
    }

    @Override
    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
        Calendar date = Calendar.getInstance();
        date.set(Calendar.YEAR, year);
        date.set(Calendar.MONTH, monthOfYear);
        date.set(Calendar.DAY_OF_MONTH, dayOfMonth);
        onPickDate(date);
    }
}
