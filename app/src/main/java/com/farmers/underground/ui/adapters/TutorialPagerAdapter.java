package com.farmers.underground.ui.adapters;


import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by omar on 9/24/15.
 */
public class TutorialPagerAdapter extends FragmentPagerAdapter {

    List<Fragment> fragmentList;

    public void setFragments(List<Fragment> fragmentList){
        this.fragmentList = fragmentList;
    }

    public TutorialPagerAdapter(FragmentManager fm) {
        super(fm);
        fragmentList = new ArrayList<>();
    }

    @Override
    public Fragment getItem(int position) {
        return fragmentList.get(position);
    }

    @Override
    public int getCount() {
        return fragmentList.size();
    }

    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }
}
