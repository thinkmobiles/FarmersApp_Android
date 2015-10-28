package com.farmers.underground.ui.activities;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

import com.farmers.underground.BuildConfig;
import com.farmers.underground.FarmersApp;
import com.farmers.underground.R;
import com.farmers.underground.config.ApiConstants;
import com.farmers.underground.config.ProjectConstants;
import com.farmers.underground.remote.RetrofitSingleton;
import com.farmers.underground.remote.models.ErrorMsg;
import com.farmers.underground.remote.models.LastCropPricesModel;
import com.farmers.underground.remote.models.PricesByDateModel;
import com.farmers.underground.remote.models.SuccessMsg;
import com.farmers.underground.remote.models.UserProfile;
import com.farmers.underground.remote.util.ACallback;
import com.farmers.underground.ui.adapters.AllPricesAdapter;
import com.farmers.underground.ui.adapters.DrawerAdapter;
import com.farmers.underground.ui.adapters.ProjectPagerAdapter;
import com.farmers.underground.ui.adapters.ToolbarSpinnerAdapter;
import com.farmers.underground.ui.base.BaseActivity;
import com.farmers.underground.ui.base.BaseFragment;
import com.farmers.underground.ui.custom_views.CustomSearchView;
import com.farmers.underground.ui.dialogs.MorePriecesDialogFragment;
import com.farmers.underground.ui.fragments.AllPricesFragment;
import com.farmers.underground.ui.fragments.MarketeerPricesFragment;
import com.farmers.underground.ui.fragments.PeriodPickerFragment;
import com.farmers.underground.ui.fragments.StatisticsFragment;
import com.farmers.underground.ui.models.DateRange;
import com.farmers.underground.ui.models.DrawerItem;
import com.farmers.underground.ui.utils.NotYetHelper;
import com.farmers.underground.ui.utils.PicassoHelper;
import com.farmers.underground.ui.utils.StringFormaterUtil;
import com.farmers.underground.ui.utils.WhatsAppUtil;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import butterknife.OnItemClick;
import jp.wasabeef.picasso.transformations.CropCircleTransformation;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

/**
 * Created by omar
 * on 10/9/15.
 */
public class PricesActivity extends BaseActivity implements DrawerAdapter.DrawerCallback, AllPricesAdapter.AllPricesCallback {

    public static final int REQUEST_CODE_PERIOD_PICKER = 5;
    public static final int REQUEST_CODE_DIALOG_WHY = 2;

    @Bind(R.id.drawer_conainer_PriceActivity)
    protected DrawerLayout mDrawerlayout;
//    @Bind(R.id.fl_DrawerHolder_PricesActivity)
//    protected FrameLayout fl_DrawerContainer;
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

    @Bind(R.id.spinner_TB)
    protected Spinner spinner;

    private LastCropPricesModel mCropModel;
    private ProjectPagerAdapter<BaseFragment<PricesActivity>> pagerAdapter;

    public static void start(@NonNull Context context, LastCropPricesModel cropModel) {
        Gson gson = new GsonBuilder().create();
        String s = gson.toJson(cropModel);
        Intent intent = new Intent(context, PricesActivity.class);
        intent.putExtra(ProjectConstants.KEY_DATA, s);
        context.startActivity(intent);
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

        setSupportActionBar(mToolbar);
        if (getSupportActionBar() != null) getSupportActionBar().setDisplayShowTitleEnabled(false);

        searchView.setVisibility(View.VISIBLE);
        calendar.setVisibility(View.VISIBLE);
        setDrawer();
        setViewPager();
        setTabs();
        setUPSpinner(spinnerTestData(), 5);


        searchView.setOnSearchClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                searchView.setIconified(true);
                MainActivity.startWithSearchFocused(PricesActivity.this);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        setDrawerList();
        viewPager.addOnPageChangeListener(pageChangeListener);
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
        Gson gson = new GsonBuilder().create();
        mCropModel = gson.fromJson(intent.getStringExtra(ProjectConstants.KEY_DATA), LastCropPricesModel.class);
        if (mCropModel == null)
            throw new IllegalAccessError("Create this activity with start(Context, CropModel) " + "method only!");
        else
            ((TextView) mToolbar.findViewById(R.id.toolbar_title)).setText(mCropModel.displayName);
    }

    //ViewPager

    public void setViewPager() {

        pagerAdapter = new ProjectPagerAdapter<>(getSupportFragmentManager());
        viewPager.setAdapter(pagerAdapter);
        pagerAdapter.setFragments(getFragmentList());
        pagerAdapter.setTitles(getTitlesList());
        pagerAdapter.notifyDataSetChanged();
        viewPager.setCurrentItem(pagerAdapter.getCount() - 1);

        //done in onResume
        //viewPager.addOnPageChangeListener(pageChangeListener);
    }

    private ViewPager.OnPageChangeListener pageChangeListener  = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            switch (position) {
                case 0:
                    spinner.setVisibility(View.VISIBLE);
                    searchView.setVisibility(View.GONE);
                    calendar.setVisibility(View.GONE);
                    spinner.bringToFront();
                    mDrawerlayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
                    break;
                case 1:
                    spinner.setVisibility(View.GONE);
                    searchView.setVisibility(View.VISIBLE);
                    calendar.setVisibility(View.VISIBLE);
                    mDrawerlayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
                    break;
                case 2:
                    spinner.setVisibility(View.GONE);
                    searchView.setVisibility(View.VISIBLE);
                    calendar.setVisibility(View.VISIBLE);
                    mDrawerlayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
                    break;
            }
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };

    private List<String> getTitlesList() {
        List<String> titles = new ArrayList<>();
        titles.add(getString(R.string.prices_activity_tab1));
        titles.add(getString(R.string.prices_activity_tab2));
        titles.add(getString(R.string.prices_activity_tab3));
        return titles;
    }

    private List<BaseFragment<PricesActivity>> getFragmentList() {
        List<BaseFragment<PricesActivity>> fragmentList = new ArrayList<>();

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

    // all prices fragment callbacks
    @Override
    public void onAllPricesItemClicked(LastCropPricesModel cropModel) {
        //hz
    }

    @Override
    public void onAddPricesClicked(LastCropPricesModel cropModel) {
        AddPriceActivity.start(this, mCropModel);
    }

    @Override
    public void onMorePricesClicked(LastCropPricesModel cropModel) {
        TransparentActivity.startWithFragment(this, new MorePriecesDialogFragment());
    }

    @Override
    public void onSettingsClicked() {
        NotYetHelper.notYetImplmented(this, "drawer settings");

        mDrawerlayout.closeDrawers();
    }


    public interface DateRangeSetter {
        void setDateRange(DateRange dateRange);
    }


    /**
     * for
     * StatisticsFragment -  has two pages;
     */
    public interface PageListener {
        void onPageSelected(int page);
    }

    // options menu
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        menu.findItem(R.id.action_back).setVisible(true);
        menu.findItem(R.id.action_burger).setVisible(false);
        menu.findItem(R.id.action_icon).setVisible(true);
        final MenuItem icon = menu.findItem(R.id.action_icon);

        final Target target = new Target() {
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
        viewPager.setTag(target);

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

    //clicks events

    @OnClick(R.id.action_calendar)
    protected void onCalendarClick() {

        TransparentActivity.startWithFragmentForResult(this, new PeriodPickerFragment(), REQUEST_CODE_PERIOD_PICKER);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case REQUEST_CODE_PERIOD_PICKER:
                    Bundle bundle = data.getExtras();
                    Calendar dateFrom = (Calendar) bundle.getSerializable(PeriodPickerFragment.KEY_DATE_FROM);
                    Calendar dateTo = (Calendar) bundle.getSerializable(PeriodPickerFragment.KEY_DATE_TO);
                    DateRange dateRange = new DateRange();
                    dateRange.setDateFrom(StringFormaterUtil.parseToServerResponse(dateFrom));
                    dateRange.setDateTo(StringFormaterUtil.parseToServerResponse(dateTo));
                    ((PricesActivity.DateRangeSetter) pagerAdapter.getItem(viewPager.getCurrentItem())).setDateRange(dateRange);
                    //todo search request
                    break;
                case REQUEST_CODE_DIALOG_WHY:
                    onAddPricesClicked(mCropModel);
                    break;
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }


    private void setUPSpinner(ArrayList<String> spinnerData, int selection) {
        final ToolbarSpinnerAdapter spinnerAdatper = new ToolbarSpinnerAdapter(this,
                spinnerData);

        spinner.setAdapter(spinnerAdatper);
        spinner.setSelection(selection);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                for (BaseFragment item : pagerAdapter.getFragmentList()) {
                    if (item instanceof ToolbarSpinnerAdapter.SpinnerCallback) {
                        ((ToolbarSpinnerAdapter.SpinnerCallback) item).onSpinnerItemSelected(spinnerAdatper.getItem(i));
                        break;
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

    }


    //todo remove later
    private ArrayList<String> spinnerTestData() {
        return new ArrayList<>(Arrays.asList(getResources().getStringArray(R.array.all_month)));
    }


    //drawer (from main Activity)
    private void setDrawer() {
        mDrawerlayout.setDrawerListener(new DrawerLayout.SimpleDrawerListener() {
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                /*lvDrawerContainer.bringToFront();*/

                mDrawerlayout.requestLayout();
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

//    private void openDrawer() {
//        if (mDrawerlayout.isDrawerOpen(fl_DrawerContainer)) {
//            mDrawerlayout.closeDrawer(fl_DrawerContainer);
//        }
//        mDrawerlayout.openDrawer(fl_DrawerContainer);
//    }

    public void setDrawerList() {
        List<DrawerItem> drawerItemList = new ArrayList<>();
        if (BuildConfig.PRODUCTION) {
            drawerItemList.add(new DrawerItem(FarmersApp.getInstance().getCurrentUser().getAvatar(), FarmersApp
                    .getInstance().getCurrentUser().getFullName()));
        } else
            drawerItemList.add(new DrawerItem("http://s2.turbopic.org/img/2007_03/i4603058af2b30.jpg", "Bela  " +
                    "Lugosie"));
        drawerItemList.add(new DrawerItem());
        drawerItemList.add(new DrawerItem(R.drawable.ic_drawer_crops, R.string.drawer_content_0));
        drawerItemList.add(new DrawerItem(R.drawable.ic_drawer_invite_friends, R.string.drawer_content_2));
        drawerItemList.add(new DrawerItem(R.drawable.ic_drawer_favourites, R.string.drawer_content_3));

        final UserProfile user = FarmersApp.getInstance().getCurrentUser();

        if (user != null && !(user.hasMarketir() || user.isNewMarketeer())) {
            drawerItemList.add(new DrawerItem(R.drawable.ic_drawer_plus, R.string.drawer_content_5));
        } else {
            drawerItemList.add(new DrawerItem(R.drawable.ic_drawer_plus, R.string.drawer_content_6));
        }

        drawerItemList.add(new DrawerItem());
        lvDrawerContainer.setAdapter(new DrawerAdapter(drawerItemList, this));
    }

    @OnItemClick(R.id.lv_DrawerHolder_PricesActivity)
    void onItemClick(int pos) {
        switch (pos) {
            case 2:
                MainActivity.startWithPageSelected(this, ProjectConstants.MAIN_ACTIVITY_PAGE_ALL);
                break;
            case 3:
                WhatsAppUtil.getInstance(this).sendInvitation();
                break;
            case 4:
                MainActivity.startWithPageSelected(this, ProjectConstants.MAIN_ACTIVITY_PAGE_FAV);
                break;
            case 5:
                if(FarmersApp.isSkipMode())
                    LoginSignUpActivity.startAddMarketier(this);
                else
                    LoginSignUpActivity.startChooseMarketier(this);
                finish();
                break;
        }
        mDrawerlayout.closeDrawers();
    }

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
        if (drawerOpened && mDrawerlayout != null){
            mDrawerlayout.closeDrawers();
        }
        /*else if ( ) {
           //todo
        }*/ else super.onBackPressed();

    }

    public void makeRequestGetPriceForPeriod(final CropAllPricesCallback callback){
        Calendar prevMonth = Calendar.getInstance();
        prevMonth.set(Calendar.MONTH, prevMonth.get(Calendar.MONTH) - 1);
        RetrofitSingleton.getInstance().getCropPricesForPeriod(
                StringFormaterUtil.parseToServerResponse(Calendar.getInstance()),
                StringFormaterUtil.parseToServerResponse(prevMonth),
                mCropModel.displayName,
                new ACallback<List<PricesByDateModel>, ErrorMsg>() {
                    @Override
                    public void onSuccess(List<PricesByDateModel> result) {
                        callback.onGetResult(result);
                    }

                    @Override
                    public void onError(@NonNull ErrorMsg error) {

                    }
                }
        );
    }

    public interface CropAllPricesCallback{
        void onGetResult(List<PricesByDateModel> result);
    }
}
