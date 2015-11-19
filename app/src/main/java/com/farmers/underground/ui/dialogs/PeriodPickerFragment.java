package com.farmers.underground.ui.dialogs;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

import com.farmers.underground.R;
import com.farmers.underground.ui.activities.TransparentActivity;
import com.farmers.underground.ui.base.BaseFragment;
import com.farmers.underground.ui.utils.StringFormaterUtil;
import java.util.Calendar;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by samson
 * on 12.10.15.
 */
public class PeriodPickerFragment extends BaseFragment<TransparentActivity> implements DatePickerDialog.OnDateSetListener {

    public static final String KEY_DATE_FROM = "date_from";
    public static final String KEY_DATE_TO = "date_to";
    public static final String KEY_ALL_TIME = "is_all_time";

    @Bind(R.id.tvPeriodFrom)
    protected TextView tvPeriodFrom;

    @Bind(R.id.tvPeriodTo)
    protected TextView tvPeriodTo;

    private enum Period {
        StartDate,
        EndDate
    }

    private Period dayFromTo;
    private Calendar dateStart, dateEnd, dateMax;
    private boolean isAllTme = false;

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
        dayFromTo = Period.EndDate;
      /*  dateEnd = Calendar.getInstance();*/
        //setDate(Calendar.getInstance());
        dateMax = Calendar.getInstance();
    }

    @SuppressWarnings("unused")
    @OnClick(R.id.tvAllTime)
    protected void pickAllTime(){
        isAllTme = true;
        dateEnd = Calendar.getInstance();
        dateStart = Calendar.getInstance();
        dateStart.set(Calendar.MONTH, dateStart.get(Calendar.MONTH) - 1);
        sendPeriod();
    }

    @SuppressWarnings("unused")
    @OnClick(R.id.tvPeriodFrom)
    protected void pickFromDate(){
        pickDate(Period.StartDate);
    }

    @SuppressWarnings("unused")
    @OnClick(R.id.tvPeriodTo)
    protected void pickToDate(){
        pickDate(Period.EndDate);
    }

    private void pickDate(Period period){
        dayFromTo = period;
        showDatePicker();
    }

    //todo clean after test
    public void onPickDate(Calendar date) {
       /* if(dayFromTo == Period.StartDate) {
            if (dateEnd == null){*/
                setDate(date);
//            } else if (dateEnd.after(date)) {
//                setDate(date);
////                sendPeriod();
//            } else {
//                getHostActivity().showToast("Incorrect period", Toast.LENGTH_SHORT);
//            }
////        } else {
//            setDate(date);
//        }
    }

    private void setDate(Calendar date){
        if(dayFromTo == Period.StartDate) {
            tvPeriodFrom.setText(StringFormaterUtil.convertDate(date));
            dateStart = date;
        } else {
            tvPeriodTo.setText(StringFormaterUtil.convertDate(date));
            dateEnd = date;
        }

        if(dateStart != null && dateEnd!=null) {
            if (dateEnd.after(dateStart)) {
                sendPeriod();
            } else {
                getHostActivity().showToast("Incorrect period", Toast.LENGTH_SHORT);
            }
        }
    }

    private void sendPeriod(){
        Bundle bundle = new Bundle();
        bundle.putSerializable(KEY_DATE_TO, dateStart);
        bundle.putSerializable(KEY_DATE_FROM, dateEnd);
        bundle.putBoolean(KEY_ALL_TIME, isAllTme);
        Intent intent = new Intent();
        intent.putExtras(bundle);
        getHostActivity().setResult(Activity.RESULT_OK, intent);
        getHostActivity().finish();
    }

    public void showDatePicker(){
        DatePickerDialog datePickerDialog =  new DatePickerDialog(
                getHostActivity(),
                this,
                dateMax.get(Calendar.YEAR),
                dateMax.get(Calendar.MONTH),
                dateMax.get(Calendar.DAY_OF_MONTH)
        );



//        if(dateEnd!= null)
//            datePickerDialog.getDatePicker().setMaxDate(dateStart.getTimeInMillis());
//        else
            datePickerDialog.getDatePicker().setMaxDate(dateMax.getTimeInMillis());

//        if(dateStart!= null)
//            datePickerDialog.getDatePicker().setMinDate(dateStart.getTimeInMillis());

        datePickerDialog.show();
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
