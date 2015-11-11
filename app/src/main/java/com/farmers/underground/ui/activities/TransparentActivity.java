package com.farmers.underground.ui.activities;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;

import com.farmers.underground.R;
import com.farmers.underground.ui.base.BaseActivity;
import com.farmers.underground.ui.base.BaseFragment;

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

    public static  <A extends BaseActivity, F extends BaseFragment<TransparentActivity>> void startWithFragmentForResult(@NonNull A activity, F fragment, int code) {
        temp = fragment;
        Intent intent = new Intent(activity, TransparentActivity.class);
        activity.startActivityForResult(intent, code);
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
