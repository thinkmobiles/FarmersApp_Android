package com.farmers.underground.ui.activities;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnItemClick;
import com.farmers.underground.R;
import com.farmers.underground.config.ProjectConstants;
import com.farmers.underground.remote.models.CropModel;
import com.farmers.underground.ui.adapters.CropsListAdapter;
import com.farmers.underground.ui.adapters.DrawerAdapter;
import com.farmers.underground.ui.adapters.ProjectPagerAdapter;
import com.farmers.underground.ui.base.BaseActivity;
import com.farmers.underground.ui.base.BaseFragment;
import com.farmers.underground.ui.custom_views.CustomSearchView;
import com.farmers.underground.ui.fragments.CropsListFragment;
import com.farmers.underground.ui.fragments.SearchQueryFragmentCallback;
import com.farmers.underground.ui.models.CropsListFragmentModel;
import com.farmers.underground.ui.models.DrawerItem;
import com.farmers.underground.ui.utils.CropsFragmentStateController;
import com.farmers.underground.ui.utils.NotYetHelper;
import com.farmers.underground.ui.utils.SearchController;
import com.farmers.underground.ui.utils.SharedPrefHelper;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by omar on 9/28/15.
 */
public class MainActivity extends BaseActivity implements DrawerAdapter.DrawerCallback, FragmentViewsCreatedCallback {

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


    private SearchManager searchManager;
    private SearchController searchController;
    private ProjectPagerAdapter<BaseFragment> pagerAdapter;
    private CropsListAdapter.CropsAdapterCallback cropsListCallback;
    private CropsFragmentStateController cropsFragmentStateController;


    private boolean drawerOpened;
    private static String query = "";

    public static void start(@NonNull Context context){
        Intent intent = new Intent(context, MainActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) query = savedInstanceState.getString(ProjectConstants.KEY_DATA);

        ButterKnife.bind(this);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);


        setCropsListCallback();
        setDrawer();
        setDrawerList();
        setViewPager();
        setTabs();
        setSearchViewListeners();
        setFragmentStateController();


        searchView.setVisibility(View.VISIBLE);
    }

    @Override
    public int getLayoutResId() {
        return R.layout.activity_main;
    }

    @Override
    protected int getFragmentContainerId() {
        return 0;
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(ProjectConstants.KEY_DATA, query);
    }


    //crops list control
    private void showTestCropItems() {
        List<CropModel> cropsList = new ArrayList<>();


        for (int i = 0; i < 10; i++) {
            CropModel basquiatCropModel = new CropModel(i);
            basquiatCropModel.setImgLink("http://www.potomitan.info/ki_nov/images/basquiat_brownspots.jpg");
            cropsList.add(basquiatCropModel);
        }

        for (BaseFragment f : pagerAdapter.getFragmentList()) {
            ((SearchQueryFragmentCallback) f).onReceiveCrops(cropsList);
        }

    }



    private void initCropFragmentAdapters() {
        for (BaseFragment f : pagerAdapter.getFragmentList()) {
            ((SearchQueryFragmentCallback) f).setListCallback(cropsListCallback);

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
            showTestCropItems();
        }

    }

    @Override
    public void onFragmentViewDestroyed() {
        cropsFragmentStateController.removeCreated();
    }




    private void setCropsListCallback() {
        cropsListCallback = new CropsListAdapter.CropsAdapterCallback() {
            @Override
            public void onItemClicked(int pos) {

            }

            @Override
            public void onFavChecked(int pos, boolean isChecked) {

            }

            @Override
            public void onPriceRefreshClicked(int pos) {

            }
        };
    }


    //search
    private void setSearchViewListeners() {
        searchController = new SearchController(lv_SearchHint) {
            @Override
            public void searchByHint(String query) {
                SharedPrefHelper.saveSearchHint(MainActivity.this, query);
                searchView.setQuery(query, true);
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
                SharedPrefHelper.saveSearchHint(MainActivity.this, query);
                searchController.hide();
                return false;
            }

            @Override
            public boolean onQueryTextChange(final String newText) {
                String newQuerry = "";
                if (newText.length() > 0) newQuerry = newText.trim();
                if (newQuerry.isEmpty()) {
                    query = "";
                    updateFragments();
                    return false;
                } else if (newQuerry.length() > 1 && newQuerry.length() < 2) return true;
                else {
                    query = newQuerry;
                    return false;
                }

            }
        });
        searchView.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                searchController.hide();
                return false;
            }
        });
    }

    @Override
    protected void onNewIntent(Intent intent) {
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            query = intent.getStringExtra(SearchManager.QUERY);
            updateFragments();

        }
    }

    private void forceHideSearchList() {
        hideSoftKeyboard();
        lv_SearchHint.setVisibility(View.GONE);
    }

    private boolean forceHideSearch() {

        if (searchView.isIconified()) return false;
        else {
            searchView.setIconified(true);
            hideSoftKeyboard();
            return true;
        }
    }


    //drawer
    private void setDrawer() {
        mDrawerlayout.setDrawerListener(new DrawerLayout.SimpleDrawerListener() {
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                lvDrawerContainer.bringToFront();
                mDrawerlayout.requestLayout();
                drawerOpened = true;
                forceHideSearchList();
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                drawerOpened = false;
            }
        });
    }

    private void openDrawer() {
        if (mDrawerlayout.isDrawerOpen(lvDrawerContainer)) {
            mDrawerlayout.closeDrawer(lvDrawerContainer);
        }
        mDrawerlayout.openDrawer(lvDrawerContainer);
    }

    public void setDrawerList() {
        List<DrawerItem> drawerItemList = new ArrayList<>();
        drawerItemList.add(new DrawerItem("", "אילן עדני"));
        drawerItemList.add(new DrawerItem());
        drawerItemList.add(new DrawerItem(R.drawable.ic_drawer_crops, R.string.drawer_content_0));
        drawerItemList.add(new DrawerItem(R.drawable.ic_drawer_marketer_price, R.string.drawer_content_1));
        drawerItemList.add(new DrawerItem(R.drawable.ic_drawer_invite_friends, R.string.drawer_content_2));
        drawerItemList.add(new DrawerItem(R.drawable.ic_drawer_favourites, R.string.drawer_content_3));
        drawerItemList.add(new DrawerItem());
        lvDrawerContainer.setAdapter(new DrawerAdapter(drawerItemList, this));
    }

    public void setViewPager() {

        pagerAdapter = new ProjectPagerAdapter<>(getSupportFragmentManager());
        viewPager.setAdapter(pagerAdapter);
        pagerAdapter.setFragments(getFragmentList());
        pagerAdapter.setTitles(getTitlesList());
        pagerAdapter.notifyDataSetChanged();
        viewPager.setCurrentItem(pagerAdapter.getCount() - 1);
    }

    private void updateFragments() {
        for (BaseFragment f : pagerAdapter.getFragmentList()) {
            ((SearchQueryFragmentCallback) f).onReceiveStringQuerry(query);
        }
    }


    //tabs
    private void setTabs() {
        tabLayout.setupWithViewPager(viewPager);
    }


    //view pager
    private List<String> getTitlesList() {
        List<String> titles = new ArrayList<>();
        titles.add(getString(R.string.main_activity_tab_favourites));
        titles.add(getString(R.string.main_activity_tab_all_crops));
        return titles;
    }

    private List<BaseFragment> getFragmentList() {
        List<BaseFragment> fragmentList = new ArrayList<>();
        fragmentList.add(createFavFragemnt());
        fragmentList.add(createCropFragemnt());
        return fragmentList;
    }

    private BaseFragment createFavFragemnt() {
        return CropsListFragment.getInstance(CropsListFragmentModel.TYPE.FAVORITIES, query);
    }

    private BaseFragment createCropFragemnt() {
        return CropsListFragment.getInstance(CropsListFragmentModel.TYPE.ALL_CROPS, query);
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
        }
        return false;
    }


    //click events

    @OnItemClick(R.id.lv_DrawerHolder_MainActivity)
    void onItemClick(int pos) {
        NotYetHelper.notYetImplmented(this, "drawer items");
        mDrawerlayout.closeDrawers();
    }

    @OnClick(R.id.searchView)
    protected void onSearchClicked() {
        searchController.setHinsList(SharedPrefHelper.getSearchHints(MainActivity.this));
        searchController.show();
    }

    @Override
    public void onSettingsClicked() {
        NotYetHelper.notYetImplmented(this, "drawer settings");
        mDrawerlayout.closeDrawers();
    }

    @Override
    public void onBackPressed() {
        if (drawerOpened) mDrawerlayout.closeDrawers();
        else if (!forceHideSearch()) super.onBackPressed();
    }


}
