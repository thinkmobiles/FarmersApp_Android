package com.farmers.underground.ui.base;

import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.farmers.underground.R;

/**
 * Created by tZpace
 * on 24-Sep-15.
 */
public abstract class BaseFragment<A extends BaseActivity> extends Fragment {
    protected BaseFragment() {
        /* nothing to do */
    }

    protected A getHostActivity() {
        //noinspection unchecked
        return (A) getActivity();
    }

    @LayoutRes
    protected abstract int getLayoutResId();

    @Deprecated
    public static <F extends BaseFragment> F newInstance() {
        return null;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        //TODO: maybe use some default layout for fragment
        if (getLayoutResId() == 0)
            throw new Error("WTF! add/override getLayoutResId.");

        return inflater.inflate(getLayoutResId(), container, false);
    }
    protected final boolean isRTL(){
        return getResources().getBoolean(R.bool.isRTL);
    }
}