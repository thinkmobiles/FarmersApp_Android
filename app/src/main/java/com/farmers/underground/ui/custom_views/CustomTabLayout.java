package com.farmers.underground.ui.custom_views;

import android.content.Context;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.view.PagerAdapter;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;
import android.view.ViewGroup;

/**
 * Created by samson on 07.10.15.
 */
public class CustomTabLayout extends TabLayout {
    public CustomTabLayout(Context context) {
        super(context);
    }

    public CustomTabLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CustomTabLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void setTabsFromPagerAdapter(@NonNull PagerAdapter adapter) {
        Typeface typeface = Typeface.createFromAsset(getContext().getAssets(), "Fonts/ArialMTBold.ttf");

        this.removeAllTabs();

        ViewGroup slidingTabStrip = (ViewGroup) getChildAt(0);

        for (int i = 0, count = adapter.getCount(); i < count; i++) {
            Tab tab = this.newTab();
            this.addTab(tab.setText(adapter.getPageTitle(i)));
            AppCompatTextView view = (AppCompatTextView) ((ViewGroup)slidingTabStrip.getChildAt(i)).getChildAt(1);
            view.setTypeface(typeface);
        }
    }
}
