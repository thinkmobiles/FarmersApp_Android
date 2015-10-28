package com.farmers.underground.ui.activities;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.Toast;
import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnItemClick;
import com.farmers.underground.BuildConfig;
import com.farmers.underground.FarmersApp;
import com.farmers.underground.R;
import com.farmers.underground.config.ProjectConstants;
import com.farmers.underground.remote.RetrofitSingleton;
import com.farmers.underground.remote.models.*;
import com.farmers.underground.remote.util.ACallback;
import com.farmers.underground.ui.adapters.CropsListAdapter;
import com.farmers.underground.ui.adapters.DrawerAdapter;
import com.farmers.underground.ui.adapters.ProjectPagerAdapter;
import com.farmers.underground.ui.base.BaseActivity;
import com.farmers.underground.ui.base.BaseFragment;
import com.farmers.underground.ui.custom_views.CustomSearchView;
import com.farmers.underground.ui.fragments.CropsFragmentCallback;
import com.farmers.underground.ui.fragments.CropsListFragment;
import com.farmers.underground.ui.models.CropsListFragmentModel;
import com.farmers.underground.ui.models.DrawerItem;
import com.farmers.underground.ui.utils.*;

import org.intellij.lang.annotations.MagicConstant;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by omar
 * on 9/28/15.
 */
public class MainActivity extends BaseActivity implements DrawerAdapter.DrawerCallback,
        FragmentViewsCreatedCallback,
        SearchResultProvider.SearchCallback {

    @Bind(R.id.toolbar)
    protected Toolbar mToolbar;

    @Bind(R.id.drawer_conainer_MainActivity)
    protected DrawerLayout mDrawerlayout;

    @Bind(R.id.lv_DrawerHolder_MainActivity)
    protected ListView lvDrawerContainer;

    @Bind(R.id.searchView)
    protected CustomSearchView searchView;

    @Bind(R.id.tabs_MainActivity)
    protected TabLayout tabLayout;

    @Bind(R.id.vp_MainActivity)
    protected ViewPager viewPager;

    @Bind(R.id.lv_SearchHint_MainActivity)
    protected ListView lv_SearchHint;

    @Bind(R.id.fl_DrawerHolder_MainActivity)
    protected FrameLayout fl_DrawerContainer;

    @Bind(R.id.ll_logoutMainActivity)
    protected View logoutView;


    private SearchManager searchManager;
    private SearchHintController searchHintController;
    private ProjectPagerAdapter<CropsListFragment> pagerAdapter;
    private CropsListAdapter.CropsAdapterCallback cropsListCallback;
    private CropsFragmentStateController cropsFragmentStateController;
    private SearchResultProvider searchResultProvider;

    private boolean drawerOpened;
    private static String query = "";

    private List<LastCropPricesModel> mCropList;
    private List<LastCropPricesModel> cropListSearch;

    public static void start(@NonNull Context context) {
        Intent intent = new Intent(context, MainActivity.class);
        context.startActivity(intent);
    }

    public static void startWithPageSelected(@NonNull Context context,
            @MagicConstant(stringValues = {ProjectConstants.MAIN_ACTIVITY_PAGE_FAV, ProjectConstants.MAIN_ACTIVITY_PAGE_ALL}) final String pageSelected) {
        Intent intent = new Intent(context, MainActivity.class);
        intent.putExtra(ProjectConstants.KEY_START_MAIN_ACTIVITY_PAGE, pageSelected);
        context.startActivity(intent);
    }

    public static void startWithSearchFocused(@NonNull Context context) {
        Intent intent = new Intent(context, MainActivity.class);
        intent.putExtra(ProjectConstants.KEY_FOCUS_SEARCH_VIEW, true);
        context.startActivity(intent);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (FarmersApp.isFirstLaunch()) FarmersApp.resetFirstLaunch();

        ButterKnife.bind(this);
        setSupportActionBar(mToolbar);

        if (getSupportActionBar() != null) getSupportActionBar().setDisplayShowTitleEnabled(false);


        setCropsListCallback();
        setDrawer();

        if (getIntent().hasExtra(ProjectConstants.KEY_START_MAIN_ACTIVITY_PAGE)) {
            setViewPager(getIntent().getStringExtra(ProjectConstants.KEY_START_MAIN_ACTIVITY_PAGE));
        } else {
            setViewPager(null);
        }

        setTabs();
        setSearchViewListeners();
        setFragmentStateController();

        searchView.setVisibility(View.VISIBLE);

        if (savedInstanceState == null)
            setSearchViewFocus(getIntent().getBooleanExtra(ProjectConstants.KEY_FOCUS_SEARCH_VIEW, false));

        searchResultProvider = SearchResultProvider.getInstance(this, this);
    }

    private void setSearchViewFocus(boolean isFocus) {
        if (isFocus) {
            searchView.requestFocus();
            searchView.setIconified(false);
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
        setDrawerList();
    }

    @Override
    public int getLayoutResId() {
        return R.layout.activity_main;
    }

    @Override
    protected int getFragmentContainerId() {
        return 0;
    }


    //crops list control
    private void getLastCrops() {
        RetrofitSingleton.getInstance().getLastCropPricesList(new ACallback<List<LastCropPricesModel>, ErrorMsg>
                () {
            @Override
            public void onSuccess(List<LastCropPricesModel> result) {
                mCropList = result;
                updateFragments(mCropList, query);
            }

            @Override
            public void onError(@NonNull ErrorMsg error) {
                showToast("BAD", Toast.LENGTH_SHORT);
            }
        });
    }

    private void updateFragments(List<LastCropPricesModel> cropList, String query) {
        for (BaseFragment f : pagerAdapter.getFragmentList()) {
            ((CropsFragmentCallback) f).onReceiveCrops(cropList, query);
        }
    }


    private void initCropFragmentAdapters() {
        for (BaseFragment f : pagerAdapter.getFragmentList()) {
            ((CropsFragmentCallback) f).setListCallback(cropsListCallback);
        }
    }

    private void setFragmentStateController() {
        cropsFragmentStateController = new CropsFragmentStateController(pagerAdapter.getCount());
    }


    @Override
    public void onFragmentViewCreated() {
        cropsFragmentStateController.addCreated();
        if (cropsFragmentStateController.isAllCreated()) {
            initCropFragmentAdapters();
            getLastCrops();
        }

    }

    @Override
    public void onFragmentViewDestroyed() {
        cropsFragmentStateController.removeCreated();
    }


    private void setCropsListCallback() {
        cropsListCallback = new CropsListAdapter.CropsAdapterCallback() {

            @Override
            public void onItemClicked(LastCropPricesModel cropModel) {
                PricesActivity.start(MainActivity.this, cropModel);
            }

            @Override
            public void onFavChecked(final LastCropPricesModel cropModel, boolean isChecked) {
                if (isChecked) {
                    showProgressDialog();
                    RetrofitSingleton.getInstance().addCropsToFavorites(cropModel.displayName, new ACallback<SuccessMsg,
                            ErrorMsg>() {
                        @Override
                        public void onSuccess(SuccessMsg result) {
                            updateAfterFavsClick(result, cropModel, true);
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
                } else {
                    showProgressDialog();
                    RetrofitSingleton.getInstance().deleteCropsFromFavorites(cropModel.displayName, new ACallback<SuccessMsg,
                            ErrorMsg>() {
                        @Override
                        public void onSuccess(SuccessMsg result) {
                            updateAfterFavsClick(result, cropModel, false);
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
            }

            @Override
            public void onPriceRefreshClicked(LastCropPricesModel cropModel) {
                AddPriceActivity.start(MainActivity.this, cropModel);
            }
        };
    }

    private void updateAfterFavsClick(SuccessMsg result, LastCropPricesModel cropModel, boolean infavs) {
        viewPager.requestFocus();
        if (result != null) showToast(result.getSuccessMsg(), Toast.LENGTH_SHORT);
        for (LastCropPricesModel item : mCropList)
            if (item.displayName.equals(cropModel.displayName))
                cropModel.isInFavorites = infavs;

        if (cropListSearch != null && cropListSearch.size() > 0 && !query.isEmpty()) {
            for (LastCropPricesModel item : cropListSearch)
                if (item.displayName.equals(cropModel.displayName))
                    cropModel.isInFavorites = infavs;
            updateFragments(cropListSearch, query);
        } else
            updateFragments(mCropList, query);
    }


    //search
    private void setSearchViewListeners() {
        searchHintController = new SearchHintController(lv_SearchHint) {
            @Override
            public void searchByHint(SearchHint query) {
                SharedPrefHelper.saveSearchHint(query);
                searchView.setQuery(query.getName(), true);
                forceHideSearchList();
                searchHintController.setQuerry(query);
            }
        };
        searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setOnSearchClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onSearchClicked();
            }
        });

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                SearchHint hint = new SearchHint(query, SearchHint.HintType.FROM_HISTORY);
                searchHintController.setQuerry(hint);
                SharedPrefHelper.saveSearchHint(hint);
                forceHideSearchList();
                updateFragmentsOnSearch(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(final String newText) {

                String newQuerry = "";
                if (newText.length() > 0) newQuerry = newText.trim();
                if (newQuerry.isEmpty()) {
                    searchHintController.setHinsList(SharedPrefHelper.getSearchHints());
                    updateFragments(mCropList, query);
                    return false;
                } else if (newQuerry.length() < 2) {
                    searchHintController.setHinsList(SharedPrefHelper.getSearchHints());
                    updateFragments(mCropList, query);
                    return false;
                } else {
                    generateQuerryList(newQuerry);
                    return false;
                }

            }
        });
        searchView.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                query = "";
                forceHideSearchList();
                return false;
            }
        });
    }

    @Override
    protected void onNewIntent(Intent intent) {
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            query = intent.getStringExtra(SearchManager.QUERY);
            updateFragmentsOnSearch(query);
        }
    }

    private void forceHideSearchList() {

        hideSoftKeyboard();
        searchHintController.hide();
        invalidateOptionsMenu();
    }


    //drawer
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

    private void openDrawer() {
        if (mDrawerlayout.isDrawerOpen(fl_DrawerContainer)) {
            mDrawerlayout.closeDrawer(fl_DrawerContainer);
        }
        mDrawerlayout.openDrawer(fl_DrawerContainer);
    }

    public void setDrawerList() {
        List<DrawerItem> drawerItemList = new ArrayList<>();
        if (BuildConfig.PRODUCTION) {
            String avatar = "";
            try {
                avatar = FarmersApp.getInstance().getCurrentUser().getAvatar();
            } catch (RuntimeException e) {
                e.printStackTrace();
            } finally {
                drawerItemList.add(new DrawerItem(avatar, FarmersApp
                        .getInstance().getCurrentUser().getFullName()));
            }

        } else
            drawerItemList.add(new DrawerItem("http://s2.turbopic.org/img/2007_03/i4603058af2b30.jpg", "Bela  " +
                    "Lugosie"));
        drawerItemList.add(new DrawerItem());
        drawerItemList.add(new DrawerItem(R.drawable.ic_drawer_crops, R.string.drawer_content_0));
        drawerItemList.add(new DrawerItem(R.drawable.ic_drawer_invite_friends, R.string.drawer_content_2));
        drawerItemList.add(new DrawerItem(R.drawable.ic_drawer_favourites, R.string.drawer_content_3));


        //FarmersApp.getInstance().getCurrentUser()!=null todo remove later (user cant be null here)
        FarmersApp.getInstance().getUserProfileAsync(null);
        final UserProfile user = FarmersApp.getInstance().getCurrentUser();

        FarmersApp.getInstance().getMarketerBySession(); //getting of target marketer

        /*if (user != null && (!(user.hasMarketir() || user.isNewMarketeer()))) {
            drawerItemList.add(new DrawerItem(R.drawable.ic_drawer_invite_friends, R.string.drawer_content_5));
        } else {

        }*/


        if (user != null && (!(user.hasMarketir() || user.isNewMarketeer()))) {
            drawerItemList.add(new DrawerItem(R.drawable.ic_drawer_plus, R.string.drawer_content_5));
        } else {
            drawerItemList.add(new DrawerItem(R.drawable.ic_drawer_plus, R.string.drawer_content_6));
        }

        drawerItemList.add(new DrawerItem());
        lvDrawerContainer.setAdapter(new DrawerAdapter(drawerItemList, this));
    }


    //tabs
    private void setTabs() {
        tabLayout.setupWithViewPager(viewPager);
    }


    //view pager
    public void setViewPager(@Nullable String page) {
        pagerAdapter = new ProjectPagerAdapter<>(getSupportFragmentManager());
        viewPager.setAdapter(pagerAdapter);
        pagerAdapter.setFragments(getFragmentList());
        pagerAdapter.setTitles(getTitlesList());
        pagerAdapter.notifyDataSetChanged();

        if (page == null) {
            viewPager.setCurrentItem(pagerAdapter.getCount() - 1);
        } else if (page.equals(ProjectConstants.MAIN_ACTIVITY_PAGE_FAV)) {
            viewPager.setCurrentItem(0);
        } else if (page.equals(ProjectConstants.MAIN_ACTIVITY_PAGE_ALL)) {
            viewPager.setCurrentItem(1);
        }
    }


    private List<String> getTitlesList() {
        List<String> titles = new ArrayList<>();
        titles.add(getString(R.string.main_activity_tab_favourites));
        titles.add(getString(R.string.main_activity_tab_all_crops));
        return titles;
    }

    private List<CropsListFragment> getFragmentList() {
        List<CropsListFragment> fragmentList = new ArrayList<>();
        fragmentList.add(createFaaFragment()); //MAIN_ACTIVITY_PAGE_FAV
        fragmentList.add(createCropFragment());//MAIN_ACTIVITY_PAGE_ALL
        return fragmentList;
    }

    private CropsListFragment createFaaFragment() {
        return CropsListFragment.getInstance(CropsListFragmentModel.TYPE.FAVOURITES);
    }

    private CropsListFragment createCropFragment() {
        return CropsListFragment.getInstance(CropsListFragmentModel.TYPE.ALL_CROPS);
    }


    //options menu

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_burger:
                openDrawer();
                return true;
            case R.id.action_back:
                hideSoftKeyboard();
                searchHintController.hide();
                invalidateOptionsMenu();
                return true;
        }
        return false;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {

        if (searchHintController.isShowing()) {
            menu.findItem(R.id.action_burger).setVisible(false);
            menu.findItem(R.id.action_back).setVisible(true);
        } else {
            menu.findItem(R.id.action_burger).setVisible(true);
            menu.findItem(R.id.action_back).setVisible(false);
        }
        return super.onPrepareOptionsMenu(menu);
    }

    //click events

    @OnItemClick(R.id.lv_DrawerHolder_MainActivity)
    void onItemClick(int pos) {
        switch (pos) {
            case 2:
                viewPager.setCurrentItem(1);
                break;
            case 3:
               WhatsAppUtil.getInstance(this).sendInvitation();
                break;
            case 4:
                viewPager.setCurrentItem(0);
                break;
            case 5:
                if (FarmersApp.isSkipMode())
                    LoginSignUpActivity.startAddMarketier(this);
                else
                    LoginSignUpActivity.startChooseMarketier(this);
                finish();
                break;
        }
        mDrawerlayout.closeDrawers();
    }

    @OnClick(R.id.searchView)
    protected void onSearchClicked() {
        searchHintController.setHinsList(SharedPrefHelper.getSearchHints());
        searchHintController.show();
        invalidateOptionsMenu();
    }

    @Override
    public void onSettingsClicked() {
        NotYetHelper.notYetImplmented(this, "drawer settings");

        //todo remove IT later

        showToast("Your Account will be DELETED, TEST", Toast.LENGTH_SHORT);
        RetrofitSingleton.getInstance().dellAccountBySession(new ACallback<SuccessMsg, ErrorMsg>() {
            @Override
            public void onSuccess(SuccessMsg result) {
                showToast(result.getSuccessMsg(), Toast.LENGTH_SHORT);

                logOut();
            }

            @Override
            public void onError(@NonNull ErrorMsg error) {
                showToast(error.getErrorMsg(), Toast.LENGTH_SHORT);
            }
        });

        mDrawerlayout.closeDrawers();
    }

    @Override
    public void onBackPressed() {
        if (drawerOpened)
            mDrawerlayout.closeDrawers();
        else if (searchHintController.isShowing()) {
            hideSoftKeyboard();
            searchHintController.hide();
        } else super.onBackPressed();

    }

    @OnClick(R.id.ll_logoutMainActivity)
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


    private void updateFragmentsOnSearch(String newQuerry) {
        searchResultProvider.loadSearchResults(newQuerry, mCropList);
    }


    private void generateQuerryList(String newQuerry) {
        searchResultProvider.loadSearchHints(newQuerry, mCropList);
    }

    @Override
    public void onSearchHintLoadFinished(List<SearchHint> searchHintList) {
        searchHintController.setHinsList(searchHintList);
    }

    @Override
    public void onSearchResultLoadFinished(List<LastCropPricesModel> crops) {
        updateFragments(crops, query);
        cropListSearch = crops;
    }
}
