package com.farmers.underground.ui.activities;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

import com.farmers.underground.R;
import com.farmers.underground.ui.base.BaseActivity;
import com.farmers.underground.ui.fragments.AddPriceFragment;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by samson on 09.10.15.
 */
public class AddPriceActivity extends BaseActivity implements DatePickerDialog.OnDateSetListener {

    public static final String KEY_ID_CROP = "id_crop";

    @Bind(R.id.tvNameCrop_tool)
    protected TextView tvNameCrop;

    @Bind(R.id.ivCrop_tool)
    protected TextView ivCrop;

    private Calendar today = Calendar.getInstance();
    private OnChangeDateListener onChangeDateListener;

    @Override
    public int getLayoutResId() {
        return R.layout.activity_add_price;
    }

    @Override
    protected int getFragmentContainerId() {
        return R.id.containerAddPrice;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        switchFragment(new AddPriceFragment(), false);
        setData();
    }

    private void setData(){
        String idCrop = getIntent().getStringExtra(KEY_ID_CROP);
    }

    public void setOnChangeDateListener(OnChangeDateListener onChangeDateListener) {
        this.onChangeDateListener = onChangeDateListener;
    }

    public void showDatePicker() {
        Calendar today = Calendar.getInstance();
        DatePickerDialog datePicker = new DatePickerDialog(this, this, today.get(Calendar.YEAR), today.get(Calendar.MONTH), today.get(Calendar.DAY_OF_MONTH));
        datePicker.show();
    }

    @Override
    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
        Calendar selectedDay = Calendar.getInstance();
        selectedDay.set(Calendar.YEAR, year);
        selectedDay.set(Calendar.MONTH, monthOfYear);
        selectedDay.set(Calendar.DAY_OF_MONTH, dayOfMonth);

        if (selectedDay.before(today)) {
            today = selectedDay;
            onChangeDateListener.onChangeDate();
        } else {
            showToast("Please select day before today", Toast.LENGTH_SHORT);
        }
    }

    public String getDate(){
        return new SimpleDateFormat("ccc dd.L.yy").format(today.getTime());
    }

    public interface OnChangeDateListener{
        void onChangeDate();
    }
}
