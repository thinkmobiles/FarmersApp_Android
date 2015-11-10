package com.farmers.underground.ui.base;

import android.os.Bundle;
import android.app.Fragment;
import android.support.annotation.LayoutRes;

import android.view.View;
import android.view.ViewGroup;
import android.view.LayoutInflater;

import com.farmers.underground.BuildConfig;

/**
 * Created by tZpace
 * on 24-Sep-15.
 */
public abstract class BaseFragment<A extends BaseActivity> extends Fragment {

    protected BaseFragment() {
        /* nothing to do */
    }

    @SuppressWarnings("noinspection unchecked")
    protected A getHostActivity() {
        return (A) getActivity();
    }

    @LayoutRes
    protected abstract int getLayoutResId();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        //TODO: maybe use some default layout for fragment
        if (BuildConfig.DEBUG && getLayoutResId() == 0)
            throw new Error("WTF! add/override getLayoutResId.");

        return inflater.inflate(getLayoutResId(), container, false);
    }

   /* protected final boolean isRTL() {
        return getResources().getBoolean(R.bool.isRTL);
    }

    protected void hideSoftKeyboard() {
        InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm.isActive() && getHostActivity().getCurrentFocus() != null)
            imm.hideSoftInputFromWindow(getHostActivity().getCurrentFocus().getWindowToken(), 0);
    }*/
}