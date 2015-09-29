package com.farmers.underground.ui.fragments;

import android.os.Bundle;
import android.view.View;
import com.farmers.underground.ui.activities.TestActivity;
import com.farmers.underground.ui.base.BaseFragment;

/**
 * Created by tZpace
 * on 24-Sep-15.
 */

/** JUST EXAMPLE*/
public class MainFragment extends BaseFragment<TestActivity> {

    @Override
    public int getLayoutResId() {
        //set layout res id here;
        return 0;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //find views Here
    }
}
