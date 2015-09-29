package com.farmers.underground.ui.activities;

import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnItemClick;
import com.farmers.underground.R;
import com.farmers.underground.ui.adapters.DrawerAdapter;
import com.farmers.underground.ui.base.BaseActivity;
import com.farmers.underground.ui.models.DrawerItem;
import com.farmers.underground.utils.NotYetHelper;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by omar on 9/28/15.
 */
public class MainActivity
        extends BaseActivity
implements DrawerAdapter.DrawerCallback {

    @Bind(R.id.toolbar)
    protected Toolbar mToolbar;
    @Bind(R.id.drawer_conainer_MainActivity)
    protected DrawerLayout mDrawerlayout;
    @Bind(R.id.lv_DrawerHolder_MainActivity)
    protected ListView lvDrawerContainer;


    private boolean drawerOpened;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        setSupportActionBar(mToolbar);
        defineDrawer();
        setDrawerList();
    }

    private void defineDrawer(){
        mDrawerlayout.setDrawerListener(new DrawerLayout.SimpleDrawerListener(){
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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId())
        {
            case R.id.action_burger:
                openDrawer();
                return true;
        }
      return false;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public void onBackPressed() {
        if(drawerOpened)  mDrawerlayout.closeDrawers();
        else  super.onBackPressed();
    }

    @Override
    public int getLayoutResId() {
        return R.layout.activity_main;
    }

    @Override
    protected int getFragmentContainerId() {
        return R.id.fl_ContainerMainActivity;
    }

    void openDrawer() {
        if (mDrawerlayout.isDrawerOpen(lvDrawerContainer)) {
            mDrawerlayout.closeDrawer(lvDrawerContainer);
        }
        mDrawerlayout.openDrawer(lvDrawerContainer);
    }

    public void setDrawerList() {
        List<DrawerItem> drawerItemList = new ArrayList<>();
        drawerItemList.add(new DrawerItem("","אילן עדני"));
        drawerItemList.add(new DrawerItem());
        drawerItemList.add(new DrawerItem(R.drawable.ic_drawer_crops, R.string.drawer_content_0));
        drawerItemList.add(new DrawerItem(R.drawable.ic_drawer_marketer_price, R.string.drawer_content_1));
        drawerItemList.add(new DrawerItem(R.drawable.ic_drawer_invite_friends, R.string.drawer_content_2));
        drawerItemList.add(new DrawerItem(R.drawable.ic_drawer_favourites, R.string.drawer_content_3));
        drawerItemList.add(new DrawerItem());
        lvDrawerContainer.setAdapter(new DrawerAdapter(drawerItemList, this));
    }

    @OnItemClick(R.id.lv_DrawerHolder_MainActivity)
    void onItemClick(int pos){
        NotYetHelper. notYetImplmented(this, "drawer items");
        mDrawerlayout.closeDrawers();
    }

    @Override
    public void onSettingsClicked() {
       NotYetHelper. notYetImplmented(this, "drawer settings");
        mDrawerlayout.closeDrawers();
    }
}
