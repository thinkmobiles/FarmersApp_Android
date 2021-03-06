package com.farmers.underground.ui.adapters;

import android.app.FragmentManager;
import android.support.v13.app.FragmentPagerAdapter;
import com.farmers.underground.ui.base.BaseFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by omar
 * on 9/24/15.
 */
public class ProjectPagerAdapter<F extends BaseFragment<?>> extends FragmentPagerAdapter {

    private List<F> fragmentList;
    private List<String> mFragmentTitles;

    public void setFragments(List<F> fragmentList) {
        if(this.fragmentList !=null){
            this.fragmentList.clear();
            this.fragmentList.addAll(fragmentList);
        } else {
            this.fragmentList = fragmentList;
        }
    }

    public void setTitles(List<String> mFragmentTitles) {
        if(this.mFragmentTitles !=null){
            this.mFragmentTitles.clear();
            this.mFragmentTitles.addAll(mFragmentTitles);
        } else {
            this.mFragmentTitles = mFragmentTitles;
        }
    }


    public ProjectPagerAdapter(FragmentManager fm) {
        super(fm);
        fragmentList = new ArrayList<>();
    }

    @Override
    public F getItem(int position) {
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

    public List<F> getFragmentList() {
        return fragmentList;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        if (mFragmentTitles != null) return mFragmentTitles.get(position);
        else return "";
    }


}
