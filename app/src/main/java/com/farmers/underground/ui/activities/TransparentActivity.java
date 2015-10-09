package com.farmers.underground.ui.activities;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;

import com.farmers.underground.R;
import com.farmers.underground.ui.base.BaseActivity;

/**
 * Created by tZpace
 * on 09-Oct-15.
 */
public class TransparentActivity extends BaseActivity {

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

}
