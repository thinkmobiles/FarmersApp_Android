package com.farmers.underground.ui.utils;

import android.app.Activity;
import android.support.v4.widget.DrawerLayout;
import android.view.View;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ListView;

import com.farmers.underground.FarmersApp;
import com.farmers.underground.R;
import com.farmers.underground.config.ProjectConstants;
import com.farmers.underground.remote.models.UserProfile;
import com.farmers.underground.ui.activities.LoginSignUpActivity;
import com.farmers.underground.ui.activities.MainActivity;
import com.farmers.underground.ui.activities.TransparentActivity;
import com.farmers.underground.ui.adapters.DrawerAdapter;
import com.farmers.underground.ui.dialogs.InviteDialogFragment;
import com.farmers.underground.ui.models.DrawerItem;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by samson
 * on 06.11.15.
 */
@Deprecated //TODO
public class DrawerManager implements AdapterView.OnItemClickListener {

    private Activity context;
    private DrawerLayout drawerLayout;
    private ListView listRightMenu;
    private FrameLayout layoutHolder;

    private boolean drawerOpened = false;

    public DrawerManager(Activity context, DrawerLayout drawerLayout, FrameLayout layoutHolder, ListView listRightMenu) {
        this.context = context;
        this.drawerLayout = drawerLayout;
        this.listRightMenu = listRightMenu;
        this.layoutHolder = layoutHolder;

        setDrawer();
        setDrawerList();
    }

    private void setDrawer() {
        drawerLayout.setDrawerListener(new DrawerLayout.SimpleDrawerListener() {
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                /*lvDrawerContainer.bringToFront();*/

                drawerLayout.requestLayout();
                drawerOpened = true;
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                drawerOpened = false;
            }
        });

        listRightMenu.setOnItemClickListener(this);
    }

    public boolean isDrawerOpened() {
        return drawerOpened;
    }

    public void openDrawer(){
        drawerLayout.openDrawer(layoutHolder);
    }

    public void closeDrawer(){
        drawerLayout.closeDrawer(layoutHolder);
    }

    private void setDrawerList() {
        List<DrawerItem> drawerItemList = new ArrayList<>();

        drawerItemList.add(new DrawerItem(FarmersApp.getInstance().getCurrentUser().getAvatar(),
                FarmersApp.getInstance().getCurrentUser().getFullName())
        );

        drawerItemList.add(new DrawerItem());
        drawerItemList.add(new DrawerItem(R.drawable.ic_drawer_crops, R.string.drawer_content_0));
        drawerItemList.add(new DrawerItem(R.drawable.ic_drawer_invite_friends, R.string.drawer_content_2));
        drawerItemList.add(new DrawerItem(R.drawable.ic_drawer_favourites, R.string.drawer_content_3));

         final UserProfile user = FarmersApp.getInstance().getCurrentUser();

        /*  FarmersApp.getInstance().getMarketerBySession(); //getting of target marketer*/

        if (user != null && !(user.hasMarketer() || user.isNewMarketeer())) {
            drawerItemList.add(new DrawerItem(R.drawable.ic_drawer_plus, R.string.drawer_content_5));
        } else {
            drawerItemList.add(new DrawerItem(R.drawable.ic_drawer_plus, R.string.drawer_content_6));
        }

        drawerItemList.add(new DrawerItem());
        listRightMenu.setAdapter(new DrawerAdapter(drawerItemList, context));
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//        String name = context.getClass().getName();
        switch (position) {
            case 2:
                MainActivity.startWithPageSelected(context, ProjectConstants.MAIN_ACTIVITY_PAGE_ALL);
                break;
            case 3:
                TransparentActivity.startWithFragment(context, new InviteDialogFragment());
                break;
            case 4:
                MainActivity.startWithPageSelected(context, ProjectConstants.MAIN_ACTIVITY_PAGE_FAV);
                break;
            case 5:
                if (FarmersApp.isSkipMode())
                    LoginSignUpActivity.startAddMarketier(context);
                else
                    LoginSignUpActivity.startChooseMarketier(context);
                break;
        }
        drawerLayout.closeDrawers();
    }
}
