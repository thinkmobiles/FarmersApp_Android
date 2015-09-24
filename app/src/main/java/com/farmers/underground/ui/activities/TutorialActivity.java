package com.farmers.underground.ui.activities;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import butterknife.Bind;
import butterknife.ButterKnife;

import butterknife.OnPageChange;
import com.farmers.underground.R;
import com.farmers.underground.ui.adapters.TutorialPagerAdapter;
import com.farmers.underground.ui.dataholders.TutorialItemDataHolder;
import com.farmers.underground.ui.fragments.FragmentTutorialItem;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by omar on 9/24/15.
 */
public class TutorialActivity extends AppCompatActivity  {

    @Bind(R.id.rg_Tutorial)
    ViewGroup radioGroup;
    @Bind(R.id.vp_Tutorial)
    ViewPager viewPager;

    private TutorialPagerAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tutorial_activity);
        ButterKnife.bind(this);
        initPager();
    }

    public void initPager() {
        adapter = new TutorialPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(adapter);
        adapter.setFragments(getFragmentList(getDataList()));
        adapter.notifyDataSetChanged();
    }


    private List<TutorialItemDataHolder> getDataList() {
        List<TutorialItemDataHolder> dataHolderList = new ArrayList<>();
        dataHolderList.add(new TutorialItemDataHolder(0, 0));
        dataHolderList.add(new TutorialItemDataHolder(0, 0));
        dataHolderList.add(new TutorialItemDataHolder(0, 0));
        return dataHolderList;
    }

    private List<Fragment> getFragmentList(List<TutorialItemDataHolder> dataHolderList) {
        List<Fragment> fragmentList = new ArrayList<>();
        Gson gson = new GsonBuilder().create();
        for (int i = 0; i < dataHolderList.size(); i++) {
            Bundle args = new Bundle();
            String data = gson.toJson(dataHolderList.get(i));
            args.putString("data", data);
            FragmentTutorialItem fragmentTutorialItem = new FragmentTutorialItem();
            fragmentTutorialItem.setArguments(args);
            fragmentList.add(fragmentTutorialItem);
            addDot();
        }
        return fragmentList;
    }

    private void addDot(){
        RadioGroup.LayoutParams params = new RadioGroup.LayoutParams(this, null);
        params.width =  getResources().getDimensionPixelSize(R.dimen.tutorial_dot_size);
        params.height =  getResources().getDimensionPixelSize(R.dimen.tutorial_dot_size);

        RadioButton radioButton = new RadioButton(this);
        radioButton.setEnabled(false);
        radioButton.setLayoutParams(params);
        radioButton.setButtonDrawable(R.drawable.tutorial_dot_selector);
        radioGroup.addView(radioButton);
        ((RadioButton)radioGroup.getChildAt(0)).setChecked(true);
    }

    @OnPageChange(R.id.vp_Tutorial)
    void onPagerPageChanged(int position){
        ((RadioButton) radioGroup.getChildAt(position)).setChecked(true);
    }


}
