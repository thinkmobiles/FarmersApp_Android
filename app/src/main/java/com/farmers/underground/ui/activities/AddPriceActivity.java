package com.farmers.underground.ui.activities;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.Toast;

import com.farmers.underground.R;
import com.farmers.underground.config.ProjectConstants;
import com.farmers.underground.remote.models.CropModel;
import com.farmers.underground.ui.base.BaseActivity;
import com.farmers.underground.ui.fragments.AddPriceFragment;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import butterknife.Bind;
import butterknife.ButterKnife;
import jp.wasabeef.picasso.transformations.CropCircleTransformation;

/**
 *
 * Created by samson on 09.10.15.
 */
public class AddPriceActivity extends BaseActivity implements DatePickerDialog.OnDateSetListener {

    public static final String KEY_ID_CROP = "id_crop";

    @Bind(R.id.action_done)
    protected ImageView buttonDone;

    @Bind(R.id.toolbar)
    protected Toolbar toolbar;

    private Calendar today = Calendar.getInstance();
    private OnChangeDateListener onChangeDateListener;
    private CropModel mCropModel;


    public static void start(@NonNull Context context, CropModel cropModel) {
        Gson gson = new GsonBuilder().create();
        String s = gson.toJson(cropModel);
        Intent intent = new Intent(context, AddPriceActivity.class);
        intent.putExtra(ProjectConstants.KEY_DATA, s);
        context.startActivity(intent);
    }

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
        setData();

        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) getSupportActionBar().setDisplayShowTitleEnabled(false);

        buttonDone.setVisibility(View.VISIBLE);

        switchFragment(new AddPriceFragment(), false);
    }

    private void setData(){
        Gson gson = new GsonBuilder().create();
        mCropModel = gson.fromJson(getIntent().getStringExtra(ProjectConstants.KEY_DATA), CropModel.class);
        if (mCropModel == null)
            throw new IllegalAccessError("Create this activity with start(Context, CropModel) " + "method only!");
    }

    public void setOnChangeDateListener(OnChangeDateListener onChangeDateListener) {
        this.onChangeDateListener = onChangeDateListener;
    }

    public void showDatePicker() {
        Calendar today = Calendar.getInstance();
        new DatePickerDialog(this, this, today.get(Calendar.YEAR), today.get(Calendar.MONTH), today.get(Calendar.DAY_OF_MONTH))
                .show();
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
        return new SimpleDateFormat("ccc dd.M.yy").format(today.getTime());
    }

    public interface OnChangeDateListener{
        void onChangeDate();
    }


    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        menu.findItem(R.id.action_back).setVisible(true);
        menu.findItem(R.id.action_burger).setVisible(false);
        menu.findItem(R.id.action_icon).setVisible(true);
        final MenuItem icon = menu.findItem(R.id.action_icon);

        Target target = new Target() {
            @Override
            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                icon.setIcon(new BitmapDrawable(bitmap));

            }

            @Override
            public void onBitmapFailed(Drawable errorDrawable) {

            }

            @Override
            public void onPrepareLoad(Drawable placeHolderDrawable) {

            }
        };
        Picasso.with(this).load(mCropModel.getImgLink()).transform(new CropCircleTransformation()).into(target);
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_back:
                onBackPressed();
                return true;
        }

        return false;
    }
}
