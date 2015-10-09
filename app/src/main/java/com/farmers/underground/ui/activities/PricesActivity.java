package com.farmers.underground.ui.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.widget.FrameLayout;
import butterknife.Bind;
import butterknife.ButterKnife;
import com.farmers.underground.R;
import com.farmers.underground.config.ProjectConstants;
import com.farmers.underground.remote.models.CropModel;
import com.farmers.underground.ui.base.BaseActivity;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * Created by omar on 10/9/15.
 */
public class PricesActivity extends BaseActivity {

    @Bind(R.id.drawer_conainer_MainActivity)
    protected FrameLayout mainCOntainer;

    @Bind(R.id.toolbar)
    protected Toolbar mToolbar;

    @Bind(R.id.tabs_PricesActivity)
    protected TabLayout tabLayout;

    @Bind(R.id.vp_PricesActivity)
    protected ViewPager viewPager;

    private CropModel cropModel;

    public static void start(@NonNull Context context, CropModel cropModel) {
        Gson gson = new GsonBuilder().create();
        String s = gson.toJson(cropModel);
        Intent intent = new Intent(context, PricesActivity.class);
        intent.putExtra(ProjectConstants.KEY_DATA, s);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getDataOnStart(getIntent());
        ButterKnife.bind(this);
        setSupportActionBar(mToolbar);
        if( getSupportActionBar()!=null)
            getSupportActionBar().setDisplayShowTitleEnabled(false);

    }

    @Override
    public int getLayoutResId() {
        return R.layout.activity_prices;
    }

    @Override
    protected int getFragmentContainerId() {
        return 0;
    }

    private void getDataOnStart(Intent intent) {
        Gson gson = new GsonBuilder().create();
        cropModel = gson.fromJson(intent.getStringExtra(ProjectConstants.KEY_DATA), CropModel.class);
        if (cropModel == null)
            throw new IllegalAccessError("Create this activity with start(Context, CropModel) " + "method only!");
    }
}
