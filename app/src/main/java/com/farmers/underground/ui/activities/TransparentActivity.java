package com.farmers.underground.ui.activities;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.widget.DatePicker;

import com.farmers.underground.R;
import com.farmers.underground.ui.base.BaseActivity;
import com.farmers.underground.ui.base.BaseFragment;

import java.util.Calendar;

/**
 * Created by tZpace
 * on 09-Oct-15.
 */
public class TransparentActivity extends BaseActivity implements DatePickerDialog.OnDateSetListener {

    private static BaseFragment<TransparentActivity> temp;
    private OnPickDateListener onPickDateListener;

    @Override
    public int getLayoutResId() {
        return R.layout.activity_transtparent;
    }

    @Override
    protected int getFragmentContainerId() {
        return R.id.fr_container_TransActivity;
    }

    public static void start(@NonNull Context context) {
        Intent intent = new Intent(context, TransparentActivity.class);
        context.startActivity(intent);
    }

    public static  <F extends BaseFragment<TransparentActivity>> void startWithFragment(@NonNull Context context, F fragment) {
        temp = fragment;
        Intent intent = new Intent(context, TransparentActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onStart() {
        super.onStart();
        if(temp!=null){
            switchFragment(temp,false);
        }
    }

    @Override
    protected void onDestroy() {
        temp=null;
        super.onDestroy();
    }

    public void close(){
        popBackStackUpTo((Class<? extends BaseFragment<?>>) temp.getClass());
    }

    public void setOnPickDateListener(OnPickDateListener onPickDateListener) {
        this.onPickDateListener = onPickDateListener;
    }

    public void showDatePicker(){
        Calendar today = Calendar.getInstance();
        new DatePickerDialog(this, this, today.get(Calendar.YEAR), today.get(Calendar.MONTH), today.get(Calendar.DAY_OF_MONTH))
                .show();
    }

    @Override
    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
        Calendar date = Calendar.getInstance();
        date.set(Calendar.YEAR, year);
        date.set(Calendar.MONTH, monthOfYear);
        date.set(Calendar.DAY_OF_MONTH, dayOfMonth);
        onPickDateListener.onPickDate(date);
    }

    public interface OnPickDateListener{
        void onPickDate(Calendar date);
    }
}
