package com.farmers.underground.ui.activities;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnItemClick;
import com.farmers.underground.R;
import com.farmers.underground.config.ProjectConstants;
import com.farmers.underground.ui.adapters.DrawerAdapter;
import com.farmers.underground.ui.adapters.ProjectPagerAdapter;
import com.farmers.underground.ui.base.BaseActivity;
import com.farmers.underground.ui.base.BaseFragment;
import com.farmers.underground.ui.fragments.CropsListFragment;
import com.farmers.underground.ui.models.DrawerItem;
import com.farmers.underground.utils.NotYetHelper;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by omar on 9/28/15.
 */
public class MainActivity extends BaseActivity implements DrawerAdapter.DrawerCallback {

    @Bind(R.id.toolbar)
    protected Toolbar mToolbar;

    @Bind(R.id.drawer_conainer_MainActivity)
    protected DrawerLayout mDrawerlayout;

    @Bind(R.id.lv_DrawerHolder_MainActivity)
    protected ListView lvDrawerContainer;

    @Bind(R.id.searchView)
    protected SearchView searchView;

    @Bind(R.id.tabs_MainActivity)
    protected TabLayout tabLayout;

    @Bind(R.id.vp_MainActivity)
    protected ViewPager viewPager;


    private SearchManager searchManager;
    private ProjectPagerAdapter<BaseFragment> adapter;
    private boolean drawerOpened;
    private String query = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) query = savedInstanceState.getString(ProjectConstants.KEY_DATA);

        ButterKnife.bind(this);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        setDrawer();
        setDrawerList();
        setViewPager();
        setTabs();
        setSearchViewListeners();

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


    //search

    private void setSearchViewListeners() {
        searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setOnSearchClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("search","clicked");
            }
        });
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                if (!query.isEmpty()) {
                    searchView.setIconified(false);
                    searchView.setQuery(query, false);
                    searchView.clearFocus();
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
        searchView.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                return false;
            }
        });

    }

    @Override
    protected void onNewIntent(Intent intent) {
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            query = intent.getStringExtra(SearchManager.QUERY);
            setViewPager();

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
        adapter = new ProjectPagerAdapter<>(getSupportFragmentManager());
        viewPager.setAdapter(adapter);
        adapter.setFragments(getFragmentList());
        adapter.setTitles(getTitlesList());
        adapter.notifyDataSetChanged();
        viewPager.setCurrentItem(adapter.getCount() - 1);
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
        return CropsListFragment.getInstance(CropsListFragment.TYPE.FAVORITIES, query);
    }

    private BaseFragment createCropFragemnt() {
        return CropsListFragment.getInstance(CropsListFragment.TYPE.ALL_CROPS, query);
    }


    //click events

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

    @OnItemClick(R.id.lv_DrawerHolder_MainActivity)
    void onItemClick(int pos) {
        NotYetHelper.notYetImplmented(this, "drawer items");
        mDrawerlayout.closeDrawers();
    }

    @Override
    public void onSettingsClicked() {
        NotYetHelper.notYetImplmented(this, "drawer settings");
        mDrawerlayout.closeDrawers();
    }

    @Override
    public void onBackPressed() {
        if (drawerOpened) mDrawerlayout.closeDrawers();
        else super.onBackPressed();
    }


}
