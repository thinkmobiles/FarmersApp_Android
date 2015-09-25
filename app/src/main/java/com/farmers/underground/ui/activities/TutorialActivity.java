package com.farmers.underground.ui.activities;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import butterknife.Bind;
import butterknife.OnPageChange;
import butterknife.ButterKnife;

import com.farmers.underground.R;
import com.farmers.underground.adapters.TutorialPagerAdapter;
import com.farmers.underground.models.TutorialItemDataHolder;
import com.farmers.underground.ui.base.BaseActivity;
import com.farmers.underground.ui.fragments.TutorialItemFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by omar
 * on 9/24/15.
 */
public class TutorialActivity
        extends BaseActivity
        implements   TutorialItemFragment.Callback{

    public static final String KEY_DATA = "data";

    @Bind(R.id.rg_Tutorial)
    ViewGroup radioGroup;
    @Bind(R.id.vp_Tutorial)
    ViewPager viewPager;

    private TutorialPagerAdapter<TutorialItemFragment> adapter;

    @Override
    public int getLayoutResId() {
        return R.layout.tutorial_activity;
    }

    @Override
    protected int getFragmentContainerId() {
        return 0;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        initPager();
    }

    public void initPager() {
        adapter = new TutorialPagerAdapter<>(getSupportFragmentManager());
        viewPager.setAdapter(adapter);
        adapter.setFragments(getFragmentList(getDataList()));
        adapter.notifyDataSetChanged();
        viewPager.setCurrentItem(adapter.getCount() - 1);
    }


    private List<TutorialItemDataHolder> getDataList() {
        List<TutorialItemDataHolder> dataHolderList = new ArrayList<>();
        dataHolderList.add(new TutorialItemDataHolder(R.string.tutorial_back_title, R.string.tutorial_test_text, R.drawable.screenshot_1));
        dataHolderList.add(new TutorialItemDataHolder(R.string.tutorial_back_title, R.string.tutorial_test_text, R.drawable.screenshot_2));
        dataHolderList.add(new TutorialItemDataHolder(R.string.tutorial_back_title, R.string.tutorial_test_text,
                R.drawable.screenshot_1));
        return dataHolderList;
    }

    private List<TutorialItemFragment> getFragmentList(List<TutorialItemDataHolder> dataHolderList) {
        List<TutorialItemFragment> fragmentList = new ArrayList<>();
        for (TutorialItemDataHolder item : dataHolderList) {
            Bundle args = new Bundle();
            args.putSerializable(KEY_DATA, item);
            TutorialItemFragment fragmentTutorialItem = new TutorialItemFragment();
            fragmentTutorialItem.setArguments(args);
            fragmentList.add(fragmentTutorialItem);
            addDot();
        }
        return fragmentList;
    }

    private void addDot() {
        RadioGroup.LayoutParams params = new RadioGroup.LayoutParams(this, null);
        params.width = getResources().getDimensionPixelSize(R.dimen.tutorial_dot_size);
        params.height = getResources().getDimensionPixelSize(R.dimen.tutorial_dot_size);

        RadioButton radioButton = new RadioButton(this);
        radioButton.setEnabled(false);
        radioButton.setLayoutParams(params);
        radioButton.setButtonDrawable(R.drawable.tutorial_dot_selector);
        radioGroup.addView(radioButton);
    }

    @OnPageChange(R.id.vp_Tutorial)
    void onPagerPageChanged(int position) {
        ((RadioButton) radioGroup.getChildAt(position)).setChecked(true);
    }

    @Override
    public void onNextClicked() {
        int currentPagerItem = viewPager.getCurrentItem();
        if(0== currentPagerItem) onBackPressed();
        else viewPager.setCurrentItem(currentPagerItem-1);
    }
}
