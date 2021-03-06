package com.farmers.underground.ui.activities;

import android.app.SearchManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.Toast;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnItemClick;

import com.farmers.underground.FarmersApp;
import com.farmers.underground.Notifier;
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
import com.farmers.underground.ui.dialogs.InviteDialogFragment;
import com.farmers.underground.ui.dialogs.WhyCanIAddThisPriceDialogFragment;
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
        SearchResultProvider.SearchCallback,
        Notifier.Client {

    @Bind(R.id.toolbar)
    protected Toolbar mToolbar;

    @Bind(R.id.drawer_conainer_MainActivity)
    protected DrawerLayout mDrawerLayout;

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


    private SearchHintController searchHintController;
    private ProjectPagerAdapter<CropsListFragment> pagerAdapter;
    private CropsListAdapter.CropsAdapterCallback cropsListCallback;
    private CropsFragmentStateController cropsFragmentStateController;
    private SearchResultProvider searchResultProvider;

    private boolean drawerOpened;
    private static String query = "";

    //private List<LastCropPricesModel> mCropList = new ArrayList<>();
    private List<LastCropPricesModel> cropListSearch = new ArrayList<>();

    public static void start(@NonNull Context context) {
        Intent intent = new Intent(context, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
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

        updateLastCrops();

        if(!FarmersApp.getInstance().isConnected()){
            showConnectionLostDialog();
        } else {
            hideConnectionLostDialog();
        }


        Notifier.registerClient(this);
    }

    @Override
    public void handleMessage(Message msg) {
        Log.d("handleMessage", getClass().getSimpleName() + " " + msg.toString());

        Bundle bundle = msg.getData();
        if (bundle != null && bundle.containsKey("isConnected")) {
            boolean isConnected = bundle.getBoolean("isConnected");
            if (isConnected){
                hideConnectionLostDialog();
                FarmersApp.getInstance().setShouldUpdateLastCropsNextTime(true);
                updateLastCrops();
            } else {
                showConnectionLostDialog();
            }
        }
    }


    @Override
    protected void onPause() {
        super.onPause();

        if (cropListSearch != null) {
            cropListSearch.clear();
        }

        Notifier.unregisterClient(this);
    }

    @Override
    public int getLayoutResId() {
        return R.layout.activity_main;
    }

    @Override
    protected int getFragmentContainerId() {
        return 0;
    }

    private void updateLastCrops() {
        if (FarmersApp.getInstance().shouldUpdateLastCropsNextTime() || FarmersApp.getInstance().shouldUpdateLastCrops()) {
            RetrofitSingleton.getInstance().getLastCropPricesList(new ACallback<List<LastCropPricesModel>, ErrorMsg>() {
                @Override
                public void onSuccess(List<LastCropPricesModel> result) {

                    if (result == null || result.isEmpty()){
                        onError(new ErrorMsg("Empty"));
                        return;
                    }

                    FarmersApp.updateCropList(result);

                    updateFragments(FarmersApp.getCropList(), query);
                    FarmersApp.getInstance().setShouldUpdateLastCropsNextTime(false);
                    FarmersApp.getInstance().setLastCopsUpdateTime();
                }

                @Override
                public void onError(@NonNull ErrorMsg error) {
                    showReloadDialogOnError(error);
                }
            });
        } else {
            if (isInitedFragAdap)
                updateFragments(FarmersApp.getCropList(), query);
        }
    }

    //crops list control
    private void getLastCrops() {
        if(FarmersApp.getCropList().isEmpty()){
            showProgressDialog();
            RetrofitSingleton.getInstance().getLastCropPricesList(new ACallback<List<LastCropPricesModel>, ErrorMsg>() {
                @Override
                public void onSuccess(List<LastCropPricesModel> result) {
                    if (result == null || result.isEmpty()){
                        onError(new ErrorMsg("Empty"));
                        return;
                    }

                    FarmersApp.updateCropList(result);

                    updateFragments(FarmersApp.getCropList(), query);
                    FarmersApp.getInstance().setLastCopsUpdateTime();
                    hideProgressDialog();
                }

                @Override
                public void onError(@NonNull ErrorMsg error) {
                    hideProgressDialog();
                    showReloadDialogOnError(error);
                }
            });
        } else {
            updateFragments(FarmersApp.getCropList(), query);
        }
    }

    private void showReloadDialogOnError(ErrorMsg error) {
        //TODO hebrew
        new AlertDialog.Builder(this)
                .setTitle((TextUtils.isEmpty(error.getErrorMsg()) ? "Error" : error.getErrorMsg()))
                .setMessage("Crops wasn't fetched.\nDo you want to reload list?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        getLastCrops();
                    }
                })
                .setNegativeButton("No", null)
                .create().show();
    }


    private void updateFragments(List<LastCropPricesModel> cropList, String query) {
        for (BaseFragment f : pagerAdapter.getFragmentList()) {
            ((CropsFragmentCallback) f).onReceiveCrops(cropList, query);
        }
    }


    private boolean isInitedFragAdap;

    private void initCropFragmentAdapters() {
        for (BaseFragment f : pagerAdapter.getFragmentList()) {
            ((CropsFragmentCallback) f).setListCallback(cropsListCallback);
        }
        isInitedFragAdap=true;
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
        isInitedFragAdap = false;
    }


    private void setCropsListCallback() {
        cropsListCallback = new CropsListAdapter.CropsAdapterCallback() {

            @Override
            public void onItemClicked(LastCropPricesModel cropModel) {
                query = "";

              /*  if(!TextUtils.isEmpty(searchView.getQuery())) {
                   searchView.setQuery(query, false);
                }*/

                PricesActivity.start(MainActivity.this, cropModel);
            }

            @Override
            public void onFavChecked(final LastCropPricesModel cropModel,final CheckBox checkBox) {
                if (checkBox.isChecked()) {
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
                            checkBox.setChecked(checkBox.isChecked()); // do un-check
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
                            checkBox.setChecked(checkBox.isChecked()); // do un-check
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

                if (!TextUtils.isEmpty(FarmersApp.getInstance().getCurrentMarketer().getFullName()) || (FarmersApp.getInstance().getCurrentUser().hasMarketer() && !FarmersApp.getInstance().getCurrentUser().isNewMarketeer())){
                    AddPriceActivity.start(MainActivity.this, cropModel);
                } else {
                    WhyCanIAddThisPriceDialogFragment fragment =  new WhyCanIAddThisPriceDialogFragment();
                    Bundle bundle = new Bundle();
                    bundle.putString("Date", cropModel.prices.get(0).data);
                    fragment.setArguments(bundle);
                    TransparentActivity.startWithFragmentForResult(MainActivity.this, fragment, ProjectConstants.REQUEST_CODE_NO_MARKETIER);
                }

            }
        };
    }

    private void updateAfterFavsClick(SuccessMsg result, LastCropPricesModel cropModel, boolean infavs) {
        viewPager.requestFocus();
        if (result != null) showToast(result.getSuccessMsg(), Toast.LENGTH_SHORT);
        for (LastCropPricesModel item : FarmersApp.getCropList())
            if (item.displayName.equals(cropModel.displayName))
                cropModel.isInFavorites = infavs;

        if (cropListSearch != null && !cropListSearch.isEmpty() && !query.isEmpty()) {
            for (LastCropPricesModel item : cropListSearch)
                if (item.displayName.equals(cropModel.displayName))
                    cropModel.isInFavorites = infavs;
            updateFragments(cropListSearch, query);
        } else
            updateFragments(FarmersApp.getCropList(), query);
    }


    //search
    private void setSearchViewListeners() {
        searchHintController = new SearchHintController(lv_SearchHint) {
            @Override
            public void searchByHint(SearchHint query) {
                SharedPrefHelper.saveSearchHint(query);
                searchView.setQuery(query.getName(), true);
                forceHideSearchList();
                searchHintController.setQuery(query);
            }
        };
        final SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
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
                searchHintController.setQuery(hint);
                SharedPrefHelper.saveSearchHint(hint);
                forceHideSearchList();
                updateFragmentsOnSearch(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(final String newText) {

                String newQuery = "";
                if (newText.length() > 0)
                    newQuery = newText.trim();

                if (newQuery.isEmpty()) {
                    searchView.setGravityRight();
                    searchHintController.setHintsList(SharedPrefHelper.getSearchHints());
                    showHintList();
                    updateFragments(FarmersApp.getCropList(), query);
                    return false;
                } else if (newQuery.length() < 1) {
                    searchView.setGravityLeft();
                    searchHintController.setHintsList(SharedPrefHelper.getSearchHints());
                    showHintList();
                    updateFragments(FarmersApp.getCropList(), query);
                    return false;
                } else {
                    searchView.setGravityLeft();
                    generateQueryList(newQuery);
                    return false;
                }

            }
        });
        searchView.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                query = "";
                forceHideSearchList();

                //updateFragments(FarmersApp.getCropList(), query);

                return false;
            }
        });
    }

    private void showHintList() {
        if (!searchHintController.isShowing())
            searchHintController.show();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);

        setSearchViewFocus(intent.getBooleanExtra(ProjectConstants.KEY_FOCUS_SEARCH_VIEW, false));

        if (intent.hasExtra(ProjectConstants.KEY_START_MAIN_ACTIVITY_PAGE)) {
            setViewPager(intent.getStringExtra(ProjectConstants.KEY_START_MAIN_ACTIVITY_PAGE));
        } else {
            setViewPager(null);
        }

        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            query = intent.getStringExtra(SearchManager.QUERY);
            if(!TextUtils.isEmpty(query))
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

    private void openDrawer() {
        if (mDrawerLayout.isDrawerOpen(fl_DrawerContainer)) {
            mDrawerLayout.closeDrawer(fl_DrawerContainer);
        }
        mDrawerLayout.openDrawer(fl_DrawerContainer);
    }

    public void setDrawerList() {
        List<DrawerItem> drawerItemList = new ArrayList<>();

        drawerItemList.add(new DrawerItem(FarmersApp.getInstance().getCurrentUser().getAvatar(), FarmersApp
                .getInstance().getCurrentUser().getFullName()));

        drawerItemList.add(new DrawerItem());
        drawerItemList.add(new DrawerItem(R.drawable.ic_drawer_crops, R.string.drawer_content_0));
        drawerItemList.add(new DrawerItem(R.drawable.ic_drawer_invite_friends, R.string.drawer_content_2));
        drawerItemList.add(new DrawerItem(R.drawable.ic_drawer_favourites, R.string.drawer_content_3));

        // (user cant be null here)
        final UserProfile user = FarmersApp.getInstance().getCurrentUser();

        if (user != null && !(user.hasMarketer() || user.isNewMarketeer())) {
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

        if (pagerAdapter == null || viewPager.getAdapter() == null) {
            pagerAdapter = new ProjectPagerAdapter<>(getFragmentManager());
            viewPager.setAdapter(pagerAdapter);
        }

        pagerAdapter.setTitles(getTitlesList());
        pagerAdapter.setFragments(getFragmentList());

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

    private List<CropsListFragment> fragmentList = new ArrayList<>();

    private List<CropsListFragment> getFragmentList() {

        if (fragmentList.isEmpty()) {
            fragmentList.add(createFaaFragment()); //MAIN_ACTIVITY_PAGE_FAV
            fragmentList.add(createCropFragment());//MAIN_ACTIVITY_PAGE_ALL
        }

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
                if (searchHintController.isShowing()) {
                    query="";
                    searchView.setQuery(query,false);
                    updateFragmentsOnSearch(query);
                    forceHideSearchList();
                } else {
                    invalidateOptionsMenu();
                }
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
    @SuppressWarnings("unused")
    @OnItemClick(R.id.lv_DrawerHolder_MainActivity)
    void onItemClick(int pos) {
        switch (pos) {
            case 2:
                viewPager.setCurrentItem(1);
                pagerAdapter.getItem(1).scrollToTop();
                break;
            case 3:
                TransparentActivity.startWithFragmentForResult(this, new InviteDialogFragment(), ProjectConstants.REQUEST_CODE_INVITE);
                break;
            case 4:
                viewPager.setCurrentItem(0);
                pagerAdapter.getItem(0).scrollToTop();
                break;
            case 5:
                if (FarmersApp.isSkipMode())
                    LoginSignUpActivity.startAddMarketier(this);
                else
                    LoginSignUpActivity.startChooseMarketier(this);
                break;
        }
        mDrawerLayout.closeDrawers();
    }

    @OnClick(R.id.searchView)
    protected void onSearchClicked() {
        searchHintController.setHintsList(SharedPrefHelper.getSearchHints());
        searchHintController.show();
        invalidateOptionsMenu();
    }

    @Override
    public void onSettingsClicked() {

        //DELETE ACC
      /*  if (!BuildConfig.PRODUCTION){
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
        }*/

        mDrawerLayout.closeDrawers();
    }

    @Override
    public void onBackPressed() {
        query="";
        if (drawerOpened)
            mDrawerLayout.closeDrawers();
        else if (searchHintController.isShowing()) {
            hideSoftKeyboard();
            searchHintController.hide();
        } else super.onBackPressed();

    }

    @SuppressWarnings("unused")
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

    private void updateFragmentsOnSearch(String newQuery) {
        searchResultProvider.loadSearchResults(newQuery, FarmersApp.getCropList());
    }

    private void generateQueryList(String newQuery) {
        searchResultProvider.loadSearchHints(newQuery, FarmersApp.getCropList());
    }

    @Override
    public void onSearchHintLoadFinished(List<SearchHint> searchHintList) {
        searchHintController.setHintsList(searchHintList);
    }

    @Override
    public void onSearchResultLoadFinished(List<LastCropPricesModel> crops) {
        updateFragments(crops, query);

        cropListSearch = crops;
    }



    @Override
    protected void onStop() {

        if(searchView != null && !TextUtils.isEmpty(searchView.getQuery())) {
            query="";
            searchView.setQuery(query, false);
        }

        if (drawerOpened)
            mDrawerLayout.closeDrawers();
        else if (searchHintController.isShowing()) {
            forceHideSearchList();
        }

        super.onStop();
    }
}
