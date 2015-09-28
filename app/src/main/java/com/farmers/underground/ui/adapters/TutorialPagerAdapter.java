package com.farmers.underground.ui.adapters;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import com.farmers.underground.ui.fragments.TutorialItemFragment;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by omar
 * on 9/24/15.
 */
public class TutorialPagerAdapter<F extends TutorialItemFragment> extends FragmentPagerAdapter {

    private List<F> fragmentList;

    public void setFragments(List<F> fragmentList){
        this.fragmentList = fragmentList;
    }

    public TutorialPagerAdapter(FragmentManager fm) {
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
}
