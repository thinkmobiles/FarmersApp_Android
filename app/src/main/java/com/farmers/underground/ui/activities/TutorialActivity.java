package com.farmers.underground.ui.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnPageChange;
import com.farmers.underground.R;
import com.farmers.underground.config.ProjectConstants;
import com.farmers.underground.ui.adapters.ProjectPagerAdapter;
import com.farmers.underground.ui.base.BaseActivity;
import com.farmers.underground.ui.fragments.TutorialItemFragment;
import com.farmers.underground.ui.models.TutorialItemDataHolder;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by omar
 * on 9/24/15.
 */
public class TutorialActivity extends BaseActivity implements TutorialItemFragment.Callback {



    @Bind(R.id.rg_Tutorial)
    protected ViewGroup radioGroup;
    @Bind(R.id.vp_Tutorial)
    protected ViewPager viewPager;

    private ProjectPagerAdapter<TutorialItemFragment> adapter;

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
        adapter = new ProjectPagerAdapter<>(getSupportFragmentManager());
        viewPager.setAdapter(adapter);
        adapter.setFragments(getFragmentList(getDataList()));
        adapter.notifyDataSetChanged();
        viewPager.setCurrentItem(isRTL() ? adapter.getCount() - 1 : 0);
    }


    private List<TutorialItemDataHolder> getDataList() {
        List<TutorialItemDataHolder> dataHolderList = new ArrayList<>();
        dataHolderList.add(new TutorialItemDataHolder(R.string.tutorial_content_1, R.drawable.tutorial_image_1));
        dataHolderList.add(new TutorialItemDataHolder(R.string.tutorial_content_2, R.drawable.tutorial_image_2));
        if (isRTL()) Collections.reverse(dataHolderList);
        return dataHolderList;
    }

    private List<TutorialItemFragment> getFragmentList(List<TutorialItemDataHolder> dataHolderList) {
        List<TutorialItemFragment> fragmentList = new ArrayList<>();
        for (TutorialItemDataHolder item : dataHolderList) {
            Bundle args = new Bundle();
            args.putSerializable(ProjectConstants.KEY_DATA, item);
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
        ((RadioButton) radioGroup.getChildAt(0)).setChecked(true);
    }

    @OnPageChange(R.id.vp_Tutorial)
   protected void onPagerPageChanged(int position) {
        ((RadioButton) radioGroup.getChildAt(position)).setChecked(true);
        adapter.getItem(position).animateNextButton();
    }

    @Override
    public void onNextClicked() {
        int currentPagerItem = viewPager.getCurrentItem();
        if (isRTL()) {
            if (0 == currentPagerItem)  onSkipClicked();
            else viewPager.setCurrentItem(currentPagerItem - 1);
        } else {
            if (adapter.getCount() - 1 == currentPagerItem)  onSkipClicked();
            else viewPager.setCurrentItem(currentPagerItem + 1);
        }
    }

    @Override
    public void onSkipClicked() {
        Intent intent = new Intent(this, LoginSignUpActivity.class);
        startActivity(intent);
        finish();
    }
}
