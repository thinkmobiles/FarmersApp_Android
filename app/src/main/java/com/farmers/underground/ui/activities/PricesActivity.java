package com.farmers.underground.ui.activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.*;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

import com.farmers.underground.FarmersApp;
import com.farmers.underground.R;
import com.farmers.underground.config.ApiConstants;
import com.farmers.underground.config.ProjectConstants;
import com.farmers.underground.remote.RetrofitSingleton;
import com.farmers.underground.remote.models.CropPricesByDateModel;
import com.farmers.underground.remote.models.ErrorMsg;
import com.farmers.underground.remote.models.LastCropPricesModel;
import com.farmers.underground.remote.models.MarketeerPricesByDateModel;
import com.farmers.underground.remote.models.StaticticModel;
import com.farmers.underground.remote.models.SuccessMsg;
import com.farmers.underground.remote.models.UserProfile;
import com.farmers.underground.remote.util.ACallback;
import com.farmers.underground.ui.adapters.DrawerAdapter;
import com.farmers.underground.ui.adapters.ProjectPagerAdapter;
import com.farmers.underground.ui.adapters.ToolbarSpinnerAdapter;
import com.farmers.underground.ui.base.BaseActivity;
import com.farmers.underground.ui.base.BaseFragment;
import com.farmers.underground.ui.base.BasePagerPricesFragment;
import com.farmers.underground.ui.custom_views.CustomSearchView;
import com.farmers.underground.ui.dialogs.InviteDialogFragment;
import com.farmers.underground.ui.dialogs.MonthPickerFragment;
import com.farmers.underground.ui.dialogs.WhyCanIAddThisPriceDialogFragment;
import com.farmers.underground.ui.fragments.AllPricesFragment;
import com.farmers.underground.ui.fragments.MarketeerPricesFragment;
import com.farmers.underground.ui.dialogs.PeriodPickerFragment;
import com.farmers.underground.ui.fragments.StatisticsFragment;
import com.farmers.underground.ui.models.DateRange;
import com.farmers.underground.ui.models.DrawerItem;
import com.farmers.underground.ui.utils.DateHelper;
import com.farmers.underground.ui.utils.ImageCacheManager;
import com.farmers.underground.ui.utils.NotYetHelper;
import com.farmers.underground.ui.utils.StringFormatterUtil;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

import butterknife.OnItemClick;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by omar
 * on 10/9/15.
 */
public class PricesActivity extends BaseActivity implements DrawerAdapter.DrawerCallback {

    @Bind(R.id.drawer_conainer_PriceActivity)
    protected DrawerLayout mDrawerLayout;
    @Bind(R.id.fl_DrawerHolder_PricesActivity)
    protected FrameLayout fl_DrawerContainer;
    @Bind(R.id.lv_DrawerHolder_PricesActivity)
    protected ListView lvDrawerContainer;

    @Bind(R.id.ll_logoutPricesActivity)
    protected View logoutView;
    private boolean drawerOpened;

    @Bind(R.id.toolbar)
    protected Toolbar mToolbar;

    @Bind(R.id.tabs_PricesActivity)
    protected TabLayout tabLayout;

    @Bind(R.id.vp_PricesActivity)
    protected ViewPager viewPager;

    @Bind(R.id.searchView)
    protected CustomSearchView searchView;

    @Bind(R.id.action_calendar)
    protected ImageView calendar;

    @Bind(R.id.toolbar_title)
    protected TextView toolbarTitle;

    @Bind(R.id.action_icon)
    protected ImageView iconCrop;

    @Bind(R.id.spinner_TB)
    protected Spinner spinner;

    public LastCropPricesModel getCropModel() {
        return mCropModel;
    }

    private LastCropPricesModel mCropModel;
    private ProjectPagerAdapter<BasePagerPricesFragment<?>> pagerAdapter;

    private boolean isVisibleBurger;
    private DateRange mDateRangeMarketeers, mDateRangeCrop, mFullRangeCrop, mFullRangeMarketeers;

    private MonthPickerCallback mMonthPickerCallback;
    private int numMonth;
    private ToolbarSpinnerAdapter spinnerAdapter;
    private StatisticCallback mStatisticCallback;

    private static final ImageLoader imageLoaderRound = ImageCacheManager.getImageLoader(FarmersApp.ImageLoaders.CACHE_ROUND);

    public static <A extends BaseActivity> void start(@NonNull A activity, LastCropPricesModel cropModel) {
        Gson gson = new GsonBuilder().create();
        String s = gson.toJson(cropModel);
        Intent intent = new Intent(activity, PricesActivity.class);
        intent.putExtra(ProjectConstants.KEY_DATA, s);
        activity.startActivityForResult(intent, ProjectConstants.REQUEST_CODE_SEE_ALL_PRIECES);
    }

    @Override
    public int getLayoutResId() {
        return R.layout.activity_prices;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);

        getDataOnStart(getIntent());
        updateToolBarCrop();

        setSupportActionBar(mToolbar);
        if (getSupportActionBar() != null) getSupportActionBar().setDisplayShowTitleEnabled(false);

        searchView.setVisibility(View.VISIBLE);
        calendar.setVisibility(View.VISIBLE);

        setDrawer();
        setViewPager();
        setTabs();
//        setUPSpinner(spinnerTestData(), 5);
        makeRequestGetCropQualityList();

        searchView.setOnSearchClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                searchView.setIconified(true);
                MainActivity.startWithSearchFocused(PricesActivity.this);
            }
        });

        numMonth = Calendar.getInstance().get(Calendar.MONTH);
    }

    private void updateToolBarCrop() {

        toolbarTitle.setText(mCropModel.displayName);

        final String url = !TextUtils.isEmpty(mCropModel.image) ? ApiConstants.BASE_URL + mCropModel.image : null;

        imageLoaderRound.displayImage(url, iconCrop, new SimpleImageLoadingListener() {
            @Override
            public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                super.onLoadingComplete(imageUri, view, loadedImage);
                iconCrop.setVisibility(View.VISIBLE);
            }
        });
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        getDataOnStart(intent);
        updateToolBarCrop();

        for (BasePagerPricesFragment basePagerPricesFragment : pagerAdapter.getFragmentList()) {
            basePagerPricesFragment.setCurrentTypeRequest(BasePagerPricesFragment.TypeRequest.Refresh);
        }

        pagerAdapter.notifyDataSetChanged();
    }

    private BasePagerPricesFragment.TypeRequest temp;

    @Override
    protected void onResume() {
        viewPager.addOnPageChangeListener(pageChangeListener);
        super.onResume();
        setDrawerList();
    }

    @Override
    protected void onPause() {
        super.onPause();
        viewPager.addOnPageChangeListener(null);
    }

    @Override
    protected int getFragmentContainerId() {
        return 0;
    }

    private void getDataOnStart(Intent intent) {
        final Gson gson = new GsonBuilder().create();
        mCropModel = gson.fromJson(intent.getStringExtra(ProjectConstants.KEY_DATA), LastCropPricesModel.class);
        if (mCropModel == null)
            throw new IllegalAccessError("Create this activity with start(Context, CropModel) " + "method only!");
    }

    // ViewPager

    public void setViewPager() {

        pagerAdapter = new ProjectPagerAdapter<>(getFragmentManager());
        pagerAdapter.setTitles(getTitlesList());
        pagerAdapter.setFragments(createFragmentList());
        pagerAdapter.notifyDataSetChanged();

        viewPager.setAdapter(pagerAdapter);
        viewPager.setCurrentItem(pagerAdapter.getCount() - 1);

        isVisibleBurger = true;
    }

    private ViewPager.OnPageChangeListener pageChangeListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            Log.d("onPageSelected", "position = " + position);
            switch (position) {
                case 0:
                    spinner.setVisibility(View.VISIBLE);
                    searchView.setVisibility(View.GONE);
                    calendar.setVisibility(View.GONE);
                    spinner.bringToFront();
                    mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
                    isVisibleBurger = false;
                    break;
                case 1:
                    spinner.setVisibility(View.GONE);
                    searchView.setVisibility(View.VISIBLE);
                    calendar.setVisibility(View.VISIBLE);
                    mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
                    isVisibleBurger = false;
                    break;
                case 2:
                    spinner.setVisibility(View.GONE);
                    searchView.setVisibility(View.VISIBLE);
                    calendar.setVisibility(View.VISIBLE);
                    mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
                    isVisibleBurger = true;
                    break;
            }
        }

        @Override
        public void onPageScrollStateChanged(int state) {
            Log.d("onPageScrollSd", " state = " + state);

        }
    };

    private List<String> getTitlesList() {
        List<String> titles = new ArrayList<>();
        titles.add(getString(R.string.prices_activity_tab1));
        titles.add(getString(R.string.prices_activity_tab2));
        titles.add(getString(R.string.prices_activity_tab3));
        return titles;
    }

    private List<BasePagerPricesFragment<?>> createFragmentList() {
        List<BasePagerPricesFragment<?>> fragmentList = new ArrayList<>();

        fragmentList.add(createStatisticsPricesFragment(mCropModel));
        fragmentList.add(createMarketeerPricesFragment(mCropModel));
        fragmentList.add(createAlLPricesFragment(mCropModel));

        return fragmentList;
    }

    private AllPricesFragment createAlLPricesFragment(LastCropPricesModel cropModel) {
        return AllPricesFragment.getInstance(cropModel);
    }

    private MarketeerPricesFragment createMarketeerPricesFragment(LastCropPricesModel cropModel) {
        return MarketeerPricesFragment.getInstance(cropModel);
    }

    private StatisticsFragment createStatisticsPricesFragment(LastCropPricesModel cropModel) {
        return StatisticsFragment.getInstance(cropModel);
    }

    private void setTabs() {
        tabLayout.setupWithViewPager(viewPager);
    }

    @Override
    public void onSettingsClicked() {
        NotYetHelper.notYetImplemented(this, "drawer settings");

        mDrawerLayout.closeDrawers();
    }

    public BasePagerPricesFragment.TypeRequest getTemp() {
        return temp;
    }

    public void setTemp(BasePagerPricesFragment.TypeRequest temp) {
        this.temp = temp;
    }

    public interface DateRangeSetter {
        void setDateRange(DateRange dateRange, boolean isAllTime);
    }

    /**
     * Used for
     * StatisticsFragment -  has two inner pages;
     */
    public interface PageListener {
        void onPageSelected(int page);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.menu_main, menu);

        menu.findItem(R.id.action_back).setVisible(!isVisibleBurger);
        menu.findItem(R.id.action_burger).setVisible(isVisibleBurger);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_back:
                onBackPressed();
                return true;
            case R.id.action_burger:
                if (mDrawerLayout != null)
                    if (drawerOpened)
                        mDrawerLayout.closeDrawers();
                    else mDrawerLayout.openDrawer(fl_DrawerContainer);
                return true;
        }

        return false;
    }

    //clicks events

    @SuppressWarnings("unused")
    @OnClick(R.id.action_calendar)
    protected void onCalendarClick() {
        TransparentActivity.startWithFragmentForResult(this, new PeriodPickerFragment(), ProjectConstants.REQUEST_CODE_PERIOD_PICKER);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case ProjectConstants.REQUEST_CODE_PERIOD_PICKER:
                    Bundle bundle = data.getExtras();
                    Calendar dateFrom = (Calendar) bundle.getSerializable(PeriodPickerFragment.KEY_DATE_FROM);
                    Calendar dateTo = (Calendar) bundle.getSerializable(PeriodPickerFragment.KEY_DATE_TO);

                    DateRange mDateRange = new DateRange();

                    final boolean isFull = bundle.getBoolean(PeriodPickerFragment.KEY_ALL_TIME, false);

                    if (isFull) {
                        prevMonth = DateHelper.parseToCalendar(mCropModel.prices.get(0).data);
                        prevMonth.set(Calendar.MONTH, prevMonth.get(Calendar.MONTH) - 1);
                        mDateRange.setDateFrom(StringFormatterUtil.parseToServerResponse(DateHelper.parseToCalendar(mCropModel.prices.get(0).data)));
                        mDateRange.setDateTo(StringFormatterUtil.parseToServerResponse(prevMonth));
                        mFullRangeCrop = mDateRange;
                        mFullRangeMarketeers = mDateRange;
                    } else {
                        mDateRange.setDateFrom(StringFormatterUtil.parseToServerResponse(dateFrom));
                        mDateRange.setDateTo(StringFormatterUtil.parseToServerResponse(dateTo));
                    }

                    mDateRangeCrop = mDateRange;
                    mDateRangeMarketeers = mDateRange;

                    for (BasePagerPricesFragment basePagerPricesFragment : pagerAdapter.getFragmentList()) {

                        basePagerPricesFragment.setCurrentTypeRequest(BasePagerPricesFragment.TypeRequest.Search);

                        basePagerPricesFragment.setDateRange(mDateRange, isFull);
                    }
                    break;
                case ProjectConstants.REQUEST_CODE_DIALOG_WHY:
                    String date = data.getStringExtra("Date");
                    showWhyDialogs(date);
                    break;
                case ProjectConstants.REQUEST_CODE_MONTH_PICKER:
                    numMonth = data.getIntExtra(ProjectConstants.KEY_POS, 0);
                    mMonthPickerCallback.onPickMonth(data.getStringExtra(ProjectConstants.KEY_DATA));
                    makeRequestGetStatisticOfQualityAndMont(spinnerAdapter.getItem(spinner.getSelectedItemPosition()));
                    break;
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }


    public void showWhyDialogs(String date) {

        if (date == null)
            date = StringFormatterUtil.parseToServerResponse(Calendar.getInstance());

        pagerAdapter.getItem(viewPager.getCurrentItem()).setCurrentTypeRequest(BasePagerPricesFragment.TypeRequest.Refresh);

        if (!TextUtils.isEmpty(FarmersApp.getInstance().getCurrentMarketer().getFullName()) || (FarmersApp.getInstance().getCurrentUser().hasMarketer() && !FarmersApp.getInstance().getCurrentUser().isNewMarketeer())) {
            AddPriceActivity.start(this, mCropModel, date);
        } else {
            WhyCanIAddThisPriceDialogFragment fragment = new WhyCanIAddThisPriceDialogFragment();
            Bundle bundle_W = new Bundle();
            bundle_W.putString("Date", date);
            fragment.setArguments(bundle_W);
            TransparentActivity.startWithFragmentForResult(PricesActivity.this, fragment, ProjectConstants.REQUEST_CODE_NO_MARKETIER);
        }
    }

    public void setmStatisticCallback(StatisticCallback mStatisticCallback) {
        this.mStatisticCallback = mStatisticCallback;
    }

    private void setUPSpinner(final List<String> spinnerData, int selection) {
        spinnerAdapter = new ToolbarSpinnerAdapter(this,
                spinnerData);

        spinner.setAdapter(spinnerAdapter);
        spinner.setSelection(selection);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                for (BaseFragment item : pagerAdapter.getFragmentList()) {
                    if (item instanceof ToolbarSpinnerAdapter.SpinnerCallback) {
                        ((ToolbarSpinnerAdapter.SpinnerCallback) item).onSpinnerItemSelected(spinnerAdapter.getItem(i));
                        break;
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

    }

    //drawer (from main Activity)
    private void setDrawer() {
        mDrawerLayout.setDrawerListener(new DrawerLayout.SimpleDrawerListener() {
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);

                mDrawerLayout.requestLayout();
                drawerOpened = true;
                logoutView.bringToFront();
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                drawerOpened = false;
            }
        });
    }

    public void setDrawerList() {
        List<DrawerItem> drawerItemList = new ArrayList<>();

        drawerItemList.add(new DrawerItem(FarmersApp.getInstance().getCurrentUser().getAvatar(), FarmersApp
                .getInstance().getCurrentUser().getFullName()));

        drawerItemList.add(new DrawerItem());
        drawerItemList.add(new DrawerItem(R.drawable.ic_drawer_crops, R.string.drawer_content_0));
        drawerItemList.add(new DrawerItem(R.drawable.ic_drawer_invite_friends, R.string.drawer_content_2));
        drawerItemList.add(new DrawerItem(R.drawable.ic_drawer_favourites, R.string.drawer_content_3));

        final UserProfile user = FarmersApp.getInstance().getCurrentUser();

        if (user != null && !(user.hasMarketer() || user.isNewMarketeer())) {
            drawerItemList.add(new DrawerItem(R.drawable.ic_drawer_plus, R.string.drawer_content_5));
        } else {
            drawerItemList.add(new DrawerItem(R.drawable.ic_drawer_plus, R.string.drawer_content_6));
        }

        drawerItemList.add(new DrawerItem());
        lvDrawerContainer.setAdapter(new DrawerAdapter(drawerItemList, this));
    }

    public void showMonthPicker(final MonthPickerCallback callback) {
        this.mMonthPickerCallback = callback;
        TransparentActivity.startWithFragmentForResult(this, MonthPickerFragment.newInstanse("2015", numMonth), ProjectConstants.REQUEST_CODE_MONTH_PICKER);
    }

    @SuppressWarnings("unused")
    @OnItemClick(R.id.lv_DrawerHolder_PricesActivity)
    void onItemClick(int pos) {
        switch (pos) {
            case 2:
                MainActivity.startWithPageSelected(this, ProjectConstants.MAIN_ACTIVITY_PAGE_ALL);
                break;
            case 3:
                TransparentActivity.startWithFragmentForResult(this, new InviteDialogFragment(), ProjectConstants.REQUEST_CODE_INVITE);
                break;
            case 4:
                MainActivity.startWithPageSelected(this, ProjectConstants.MAIN_ACTIVITY_PAGE_FAV);
                break;
            case 5:
                if (FarmersApp.isSkipMode())
                    LoginSignUpActivity.startAddMarketier(this);
                else
                    LoginSignUpActivity.startChooseMarketier(this);
                finish();
                break;
        }
        mDrawerLayout.closeDrawers();
    }

    @SuppressWarnings("unused")
    @OnClick(R.id.ll_logoutPricesActivity)
    protected void logOut() {
        showProgressDialog();
        RetrofitSingleton.getInstance().signOut(new ACallback<SuccessMsg, ErrorMsg>() {
            @Override
            public void onSuccess(SuccessMsg result) {
                showToast(result.getSuccessMsg(), Toast.LENGTH_SHORT);
                finish();
                FarmersApp.getInstance().onUserLogOut();
            }

            @Override
            public void onError(@NonNull ErrorMsg error) {
                showToast(error.getErrorMsg(), Toast.LENGTH_SHORT);
            }

            @Override
            public void anyway() {
                hideProgressDialog();
            }
        });
    }


    @Override
    public void onBackPressed() {
        if (drawerOpened && mDrawerLayout != null) {
            mDrawerLayout.closeDrawers();
        } else {
            super.onBackPressed();
        }
    }

    /**
     * if dateRange == null default date range is used
     */

    private Calendar prevMonth;

    public void makeRequestGetCropPriceForPeriod(boolean isFull, final CropAllPricesCallback callback) {
        if (isFull) {
            makeRequestGetCropPriceForPeriod(null, callback);
        } else {
            makeRequestGetCropPriceForPeriod(mDateRangeCrop, callback);
        }
    }

    public void makeRequestGetPriceForPeriodAddMonth(final CropAllPricesCallback callback) {
        prevMonth.set(Calendar.MONTH, prevMonth.get(Calendar.MONTH) - 1);
        mDateRangeCrop.setDateFrom(mDateRangeCrop.getDateTo());
        mDateRangeCrop.setDateTo(StringFormatterUtil.parseToServerResponse(prevMonth));
        mFullRangeCrop.setDateTo(StringFormatterUtil.parseToServerResponse(prevMonth));

        makeRequestGetCropPriceForPeriod(mDateRangeCrop, callback);
    }

    private void makeRequestGetCropPriceForPeriod(@Nullable DateRange dateRange, final CropAllPricesCallback callback) {

        if (dateRange == null) {
            prevMonth = DateHelper.parseToCalendar(mCropModel.prices.get(0).data);

            prevMonth.set(Calendar.MONTH, prevMonth.get(Calendar.MONTH) - 1);
            dateRange = new DateRange();
            dateRange.setDateFrom(StringFormatterUtil.parseToServerResponse(DateHelper.parseToCalendar(mCropModel.prices.get(0).data)));
            dateRange.setDateTo(StringFormatterUtil.parseToServerResponse(prevMonth));

            mDateRangeCrop = dateRange;
            mFullRangeCrop = dateRange;
        }

        RetrofitSingleton.getInstance().getCropPricesForPeriod(dateRange.getDateFrom(), dateRange.getDateTo(),
                mCropModel.displayName,
                new ACallback<List<CropPricesByDateModel>, ErrorMsg>() {
                    @Override
                    public void onSuccess(List<CropPricesByDateModel> result) {
                        if (result != null && !result.isEmpty()) {
                            callback.onGetResult(result);
                        } else {
                            callback.onError();
                        }
                    }

                    @Override
                    public void onError(@NonNull ErrorMsg error) {
                        showToast(error.getErrorMsg(), Toast.LENGTH_SHORT);
                        callback.onError();
                    }
                }
        );

    }

    public void makeRequestGetMarketeerPriceForPeriod(boolean isFull, final MarketeerAllPricesCallback callback) {
        if (isFull) {
            makeRequestGetMarketeerPricesForPeriod(null, callback);
        } else {
            makeRequestGetMarketeerPricesForPeriod(mDateRangeMarketeers, callback);
        }
    }

    public void makeRequestGetMarketeerPriceForPeriodAddMonth(final MarketeerAllPricesCallback callback) {
        prevMonth.set(Calendar.MONTH, prevMonth.get(Calendar.MONTH) - 1);
        mDateRangeMarketeers.setDateFrom(mDateRangeMarketeers.getDateTo());
        mDateRangeMarketeers.setDateTo(StringFormatterUtil.parseToServerResponse(prevMonth));
        mFullRangeMarketeers.setDateTo(StringFormatterUtil.parseToServerResponse(prevMonth));

        makeRequestGetMarketeerPricesForPeriod(mDateRangeMarketeers, callback);
    }

    public void makeRequestGetMarketeerPricesForPeriod(@Nullable DateRange dateRange, final MarketeerAllPricesCallback callback) {

        if (dateRange == null) {
            prevMonth = DateHelper.parseToCalendar(mCropModel.prices.get(0).data);
            prevMonth.set(Calendar.MONTH, prevMonth.get(Calendar.MONTH) - 1);
            dateRange = new DateRange();
            dateRange.setDateFrom(StringFormatterUtil.parseToServerResponse(DateHelper.parseToCalendar(mCropModel.prices.get(0).data)));
            dateRange.setDateTo(StringFormatterUtil.parseToServerResponse(prevMonth));

            mDateRangeMarketeers = dateRange;
            mFullRangeMarketeers = dateRange;
        }

        RetrofitSingleton.getInstance().getMarketeerCropPricesForPeriod(
                dateRange.getDateFrom(),
                dateRange.getDateTo(),
                mCropModel.displayName,
                new ACallback<List<MarketeerPricesByDateModel>, ErrorMsg>() {
                    @Override
                    public void onSuccess(List<MarketeerPricesByDateModel> result) {
                        if (result != null && !result.isEmpty()) {
                            callback.onGetResult(result);
                        } else {
                            callback.onError();
                        }
                    }

                    @Override
                    public void onError(@NonNull ErrorMsg error) {
                        showToast(error.getErrorMsg(), Toast.LENGTH_SHORT);
                        callback.onError();
                    }
                });
    }

    public void makeRequestGetCropQualityList() {
        RetrofitSingleton.getInstance().getCropQualityList(
                mCropModel.displayName,
                new ACallback<List<String>, ErrorMsg>() {
                    @Override
                    public void onSuccess(List<String> result) {
                        if (result != null && !result.isEmpty()) {
                            setUPSpinner(result, 0);
                        } else {
                            showToast("List is empty", Toast.LENGTH_SHORT);
                        }
                    }

                    @Override
                    public void onError(@NonNull ErrorMsg error) {
                        showToast(error.getErrorMsg(), Toast.LENGTH_SHORT);
                    }
                });
    }

    public void makeRequestGetStatisticOfQuality(String quality){
        RetrofitSingleton.getInstance().getStatisticsOfQuality(
                mCropModel.displayName,
                quality,
                new ACallback<List<StaticticModel>, ErrorMsg>() {
                    @Override
                    public void onSuccess(List<StaticticModel> result) {
                        mStatisticCallback.onGetResult(result);
                    }

                    @Override
                    public void onError(@NonNull ErrorMsg error) {
                        showToast(error.getErrorMsg(), Toast.LENGTH_SHORT);
                    }
                });
    }

    public void makeRequestGetStatisticOfQualityAndMont(String quality){
        RetrofitSingleton.getInstance().getStatisticsOfQualityAndMonth(
                mCropModel.displayName,
                quality,
                numMonth,
                new ACallback<List<StaticticModel>, ErrorMsg>() {
                    @Override
                    public void onSuccess(List<StaticticModel> result) {
                        mStatisticCallback.onGetResult(result);
                    }

                    @Override
                    public void onError(@NonNull ErrorMsg error) {
                        showToast(error.getErrorMsg(), Toast.LENGTH_SHORT);
                    }
                });
    }

    public interface CropAllPricesCallback {
        void onGetResult(List<CropPricesByDateModel> result);

        void onError(); //result is empty or network/server error
    }

    public interface MarketeerAllPricesCallback {
        void onGetResult(List<MarketeerPricesByDateModel> result);

        void onError(); //result is empty or network/server error
    }

    public interface StatisticCallback {
        void onGetResult(List<StaticticModel> result);

        void onError(); //result is empty or network/server error
    }

    public interface MonthPickerCallback {
        void onPickMonth(String month);
    }


}
