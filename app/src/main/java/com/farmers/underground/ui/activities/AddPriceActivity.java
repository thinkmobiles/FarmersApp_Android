package com.farmers.underground.ui.activities;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import butterknife.Bind;
import butterknife.ButterKnife;

import com.farmers.underground.FarmersApp;
import com.farmers.underground.R;
import com.farmers.underground.config.ApiConstants;
import com.farmers.underground.config.ProjectConstants;
import com.farmers.underground.remote.RetrofitSingleton;
import com.farmers.underground.remote.models.ErrorMsg;
import com.farmers.underground.remote.models.FarmerPricesModel;
import com.farmers.underground.remote.models.LastCropPricesModel;
import com.farmers.underground.remote.models.SuccessMsg;
import com.farmers.underground.remote.util.ACallback;
import com.farmers.underground.ui.base.BaseActivity;
import com.farmers.underground.ui.fragments.AddPriceFragment;
import com.farmers.underground.ui.utils.DateHelper;
import com.farmers.underground.ui.utils.ImageCacheManager;
import com.farmers.underground.ui.utils.PicassoHelper;
import com.farmers.underground.ui.utils.ResUtil;
import com.farmers.underground.ui.utils.StringFormaterUtil;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import butterknife.OnClick;
import jp.wasabeef.picasso.transformations.CropCircleTransformation;

import java.util.Calendar;

/**
 *
 * Created by samson on 09.10.15.
 */
public class AddPriceActivity extends BaseActivity implements DatePickerDialog.OnDateSetListener {

    @Bind(R.id.action_done)
    protected ImageView buttonDone;

    @Bind(R.id.toolbar)
    protected Toolbar toolbar;

    private Calendar today = Calendar.getInstance();
    private Calendar selectedDate = Calendar.getInstance();
    private OnChangeDateListener onChangeDateListener;
    private LastCropPricesModel mCropModel;
    private AddPriceFragment childFragment;
    private boolean enableDone = false;

    public static void start(@NonNull Context context, LastCropPricesModel cropModel) {
        Gson gson = new GsonBuilder().create();
        String s = gson.toJson(cropModel);
        Intent intent = new Intent(context, AddPriceActivity.class);
        intent.putExtra(ProjectConstants.KEY_DATA, s);
        context.startActivity(intent);
    }

    public static void start(@NonNull Context context, LastCropPricesModel cropModel, String date) {
        Gson gson = new GsonBuilder().create();
        String s = gson.toJson(cropModel);
        Intent intent = new Intent(context, AddPriceActivity.class);
        intent.putExtra(ProjectConstants.KEY_DATA, s);
        intent.putExtra(ProjectConstants.KEY_DATE, date);
        context.startActivity(intent);
    }

    public void setEnableDone(boolean isEnableDone) {
        this.enableDone = isEnableDone;
        if(enableDone){
            buttonDone.setImageDrawable(ResUtil.getDrawable(getResources(),R.drawable.ic_done_active));
        } else {
            buttonDone.setImageDrawable(ResUtil.getDrawable(getResources(), R.drawable.ic_done_noactive));
        }
    }

    public void setChildFragment(AddPriceFragment childFragment) {
        this.childFragment = childFragment;
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
        mCropModel = gson.fromJson(getIntent().getStringExtra(ProjectConstants.KEY_DATA), LastCropPricesModel.class);
        if (mCropModel == null)
            throw new IllegalAccessError("Create this activity with start(Context, CropModel) " + "method only!");
        else {
            ((TextView) toolbar.findViewById(R.id.toolbar_title)).setText(mCropModel.displayName);
        }
        if(getIntent().getStringExtra(ProjectConstants.KEY_DATE) != null){
            selectedDate = DateHelper.parseToCalendar(getIntent().getStringExtra(ProjectConstants.KEY_DATE));
        }
    }

    public void setOnChangeDateListener(OnChangeDateListener onChangeDateListener) {
        this.onChangeDateListener = onChangeDateListener;
    }

    public void showDatePicker() {
        Calendar today = Calendar.getInstance();
        DatePickerDialog dpd;
        if (Build.VERSION.SDK_INT >=21){
            dpd = new DatePickerDialog(this,
                    R.style.MyAlertDialogStyle,
                    this, today.get(Calendar.YEAR), today.get(Calendar.MONTH), today.get(Calendar.DAY_OF_MONTH));

        } else {
            dpd = new DatePickerDialog(this,
                    this, today.get(Calendar.YEAR), today.get(Calendar.MONTH), today.get(Calendar.DAY_OF_MONTH));
        }
        dpd.show();

    }

    @Override
    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
        Calendar selectedDay = Calendar.getInstance();
        selectedDay.set(Calendar.YEAR, year);
        selectedDay.set(Calendar.MONTH, monthOfYear);
        selectedDay.set(Calendar.DAY_OF_MONTH, dayOfMonth);

        if (selectedDay.before(today)) {
            selectedDate = selectedDay;
            onChangeDateListener.onChangeDate();
        } else {
            showToast("Please select day before today", Toast.LENGTH_SHORT);
        }
    }

    public String getDate(){
        return StringFormaterUtil.convertDate(selectedDate);
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
                icon.setIcon(new BitmapDrawable(getResources(),bitmap));

            }

            @Override
            public void onBitmapFailed(Drawable errorDrawable) {

            }

            @Override
            public void onPrepareLoad(Drawable placeHolderDrawable) {

            }
        };

        final String url = !TextUtils.isEmpty(mCropModel.image) ? ApiConstants.BASE_URL + mCropModel.image : null;
        if (url != null)

            PicassoHelper.getPicasso(this)
                    .load(url)
                    .config(Bitmap.Config.RGB_565)
                    .transform(new CropCircleTransformation())
                    .placeholder(R.drawable.ic_drawer_crops)
                    .error(R.drawable.ic_drawer_crops)
                    .into(target);

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

    @SuppressWarnings("unused")
    @OnClick(R.id.action_done)
    protected void donePrice(){
        if(enableDone){
            showCorrectDialog();
        } else {
            showToast("Please fill field correctly", Toast.LENGTH_SHORT);
        }
    }

    private void showCorrectDialog(){
        new AlertDialog.Builder(this)
                .setCancelable(false)
                .setMessage(getString(R.string.dialog_please_correct))
                .setPositiveButton(getString(R.string.dialog_accept), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        sendPrices(); //request
//                        showToast("todo request", Toast.LENGTH_SHORT);
                        hideSoftKeyboard();
                        childFragment.refresh();
                        dialog.dismiss();
                    }
                })
                .setNeutralButton(getString(R.string.dialog_correct), null)
                .create()
                .show();
    }

    private void sendPrices(){
        FarmerPricesModel farmerPricesModel = new FarmerPricesModel();
        farmerPricesModel.cropName = mCropModel.displayName;
        farmerPricesModel.date = StringFormaterUtil.parseToServerResponse(selectedDate);
        RetrofitSingleton.getInstance().addFarmerPriceForCrop(
                new FarmerPricesModel(
                        mCropModel.displayName,
                        StringFormaterUtil.parseToServerResponse(selectedDate),
                        childFragment.getPriceList()),
                new ACallback<SuccessMsg, ErrorMsg>() {

            @Override
            public void onSuccess(SuccessMsg result) {
                showToast(getString(R.string.alert_message_after_add_price), Toast.LENGTH_SHORT);
                anyway();
            }

            @Override
            public void onError(@NonNull ErrorMsg error) {
                showToast("BAD", Toast.LENGTH_SHORT);
                anyway();
            }

            @Override
            public void anyway() {
                hideProgressDialog();
            }
        });
    }
}
