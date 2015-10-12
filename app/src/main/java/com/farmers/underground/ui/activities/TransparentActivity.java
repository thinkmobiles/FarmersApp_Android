package com.farmers.underground.ui.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;

import com.farmers.underground.R;
import com.farmers.underground.ui.base.BaseActivity;
import com.farmers.underground.ui.base.BaseFragment;
import com.farmers.underground.ui.dialogs.CropQualitiesDialogFragment;
import com.farmers.underground.ui.dialogs.WhyCanISeeThisPriceDialogFragment;

/**
 * Created by tZpace
 * on 09-Oct-15.
 */
public class TransparentActivity extends BaseActivity {

    private static BaseFragment<TransparentActivity> temp;

    @Override
    public int getLayoutResId() {
        return R.layout.activity_transtparent;
    }

    @Override
    protected int getFragmentContainerId() {
        return R.id.fr_container_TransActivity;
    }

    public static void start(@NonNull Context context) {
        Intent intent = new Intent(context, TransparentActivity.class);
        context.startActivity(intent);
    }

    public static  <F extends BaseFragment<TransparentActivity>> void startWithFragment(@NonNull Context context, F fragment) {
        temp = fragment;
        Intent intent = new Intent(context, TransparentActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //for test todo
        //startWithFragment(this, new CropQualitiesDialogFragment());
        startWithFragment(this, new WhyCanISeeThisPriceDialogFragment());
    }

    @Override
    protected void onStart() {
        super.onStart();
        if(temp!=null){
            switchFragment(temp,false);
        }
    }

    @Override
    protected void onDestroy() {
        temp=null;
        super.onDestroy();
    }

}
