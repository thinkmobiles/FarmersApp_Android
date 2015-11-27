package com.farmers.underground.ui.activities;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
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
import com.farmers.underground.ui.utils.AnalyticsTrackerUtil;
import com.farmers.underground.ui.utils.DateHelper;
import com.farmers.underground.ui.utils.ImageCacheManager;
import com.farmers.underground.ui.utils.ResUtil;
import com.farmers.underground.ui.utils.StringFormatterUtil;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

import butterknife.OnClick;

import java.util.Calendar;

/**
 *
 * Created by samson on 09.10.15.
 */
public class AddPriceActivity extends BaseActivity implements DatePickerDialog.OnDateSetListener {

    @Bind(R.id.action_done)
    protected ImageView buttonDone;

    @Bind(R.id.action_icon)
    protected ImageView iconCrop;

    @Bind(R.id.toolbar)
    protected Toolbar toolbar;

    private static final ImageLoader imageLoaderRound = ImageCacheManager.getImageLoader(FarmersApp.ImageLoaders.CACHE_ROUND);

    private Calendar today = Calendar.getInstance();
    private Calendar selectedDate = Calendar.getInstance();
    private OnChangeDateListener onChangeDateListener;
    private LastCropPricesModel mCropModel;
    private AddPriceFragment childFragment;
    private boolean enableDone = false;

    public static <A extends BaseActivity> void start(@NonNull A activity, LastCropPricesModel cropModel) {
        Gson gson = new GsonBuilder().create();
        String s = gson.toJson(cropModel);
        Intent intent = new Intent(activity, AddPriceActivity.class);
        intent.putExtra(ProjectConstants.KEY_DATA, s);
        activity.startActivityForResult(intent, ProjectConstants.REQUEST_CODE_ADD_PRIECE);
    }

    public static <A extends BaseActivity> void start(@NonNull A activity, LastCropPricesModel cropModel, String date) {
        Gson gson = new GsonBuilder().create();
        String s = gson.toJson(cropModel);
        Intent intent = new Intent(activity, AddPriceActivity.class);
        intent.putExtra(ProjectConstants.KEY_DATA, s);
        intent.putExtra(ProjectConstants.KEY_DATE, date);
        activity.startActivityForResult(intent , ProjectConstants.REQUEST_CODE_ADD_PRIECE);
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

    @Override
    protected void onStart() {
        super.onStart();

        final String url = !TextUtils.isEmpty(mCropModel.image) ? ApiConstants.BASE_URL + mCropModel.image : null;

        imageLoaderRound.displayImage(url, iconCrop, new SimpleImageLoadingListener() {
            @Override
            public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                super.onLoadingComplete(imageUri, view, loadedImage);
                iconCrop.setVisibility(View.VISIBLE);
            }
        });
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
        Calendar focusDate = (selectedDate!=null) ? selectedDate : today;
        DatePickerDialog dpd;
        if (Build.VERSION.SDK_INT >=21){
            dpd = new DatePickerDialog(this,
                    R.style.MyAlertDialogStyle,
                    this, focusDate.get(Calendar.YEAR), focusDate.get(Calendar.MONTH), focusDate.get(Calendar.DAY_OF_MONTH));

        } else {
            dpd = new DatePickerDialog(this,
                    this, focusDate.get(Calendar.YEAR), focusDate.get(Calendar.MONTH), focusDate.get(Calendar.DAY_OF_MONTH));
        }
        dpd.getDatePicker().setMaxDate(today.getTime().getTime()); //today max
        dpd.show();

    }

    @Override
    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
        if (view.isShown()) {
            Calendar selectedDay = Calendar.getInstance();
            selectedDay.set(Calendar.YEAR, year);
            selectedDay.set(Calendar.MONTH, monthOfYear);
            selectedDay.set(Calendar.DAY_OF_MONTH, dayOfMonth);

            if (selectedDay.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY || selectedDay.get(Calendar.DAY_OF_WEEK) == Calendar.FRIDAY) {
                showToast("You can't add prices for Friday or Saturday", Toast.LENGTH_SHORT);
                return;
            } else if (selectedDay.after(Calendar.getInstance())){
                showToast("You can't add prices for future days", Toast.LENGTH_SHORT);
                return;
            }

            selectedDate = selectedDay;
            onChangeDateListener.onChangeDate();
        }
    }

    public String getDate(){
        return StringFormatterUtil.convertDate(selectedDate);
    }

    public interface OnChangeDateListener{
        void onChangeDate();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.menu_main, menu);
        menu.findItem(R.id.action_back).setVisible(true);
        menu.findItem(R.id.action_burger).setVisible(false);
        return true;
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
            showToast(getString(R.string.fill_prices_correctly), Toast.LENGTH_SHORT);
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
        farmerPricesModel.date = StringFormatterUtil.parseToServerResponse(selectedDate);
        showProgressDialog();
        RetrofitSingleton.getInstance().addFarmerPriceForCrop(
                new FarmerPricesModel(
                        mCropModel.displayName,
                        StringFormatterUtil.parseToServerResponse(selectedDate),
                        childFragment.getPriceList()),
                new ACallback<SuccessMsg, ErrorMsg>() {

            @Override
            public void onSuccess(SuccessMsg result) {
                showToast(getString(R.string.alert_message_after_add_price), Toast.LENGTH_SHORT);
                FarmersApp.getInstance().setShouldUpdateLastCropsNextTime(true);
                anyway();

                //track add price on GoogleAnalytics
                AnalyticsTrackerUtil.getInstance().trackEvent(AnalyticsTrackerUtil.TypeEvent.AddPrice);

                finish();
            }

            @Override
            public void onError(@NonNull ErrorMsg error) {
                showToast(error.getErrorMsg(), Toast.LENGTH_SHORT);
                anyway();
            }

            @Override
            public void anyway() {
                hideProgressDialog();
            }
        });
    }
}
