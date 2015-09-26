package com.farmers.underground.ui.base;

import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import com.farmers.underground.R;

/**
 * Created by tZpace
 * on 24-Sep-15.
 */
public abstract class BaseActivity extends AppCompatActivity {

    @LayoutRes
    public abstract int getLayoutResId();

    protected abstract int getFragmentContainerId();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //TODO: maybe use some default layout for activity
        if(getLayoutResId() == 0)
            throw new Error("WTF! add/override getLayoutResId.");

        setContentView(getLayoutResId());
    }

    public void switchFragment(@NonNull String fragmentClassName, boolean saveInBackStack) {
        updateFragment(instantiateFragment(fragmentClassName), saveInBackStack, false);
    }

    public void addFragment(@NonNull String fragmentClassName, boolean saveInBackStack) {
        updateFragment(instantiateFragment(fragmentClassName), saveInBackStack, true);
    }

    @Nullable
    public Fragment getCurrentFragment() {
        Fragment f = getSupportFragmentManager()
                .findFragmentById(getFragmentContainerId());
        return f != null && f.isAdded() ? f : null;
    }

    protected static Fragment instantiateFragment(@NonNull String fragmentClassName) {
        try {
            return (Fragment) Class.forName(fragmentClassName).newInstance();
        } catch (InstantiationException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

        protected void updateFragment(@NonNull Fragment fragment, boolean saveInBackStack, boolean add) {

        final String fragmentTag = ((Object) fragment).getClass().getName();

        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        if (add) {
            ft.add(getFragmentContainerId(), fragment, fragmentTag);
        } else {
            ft.replace(getFragmentContainerId(), fragment, fragmentTag);
        }
        if (saveInBackStack) {
            ft.addToBackStack(fragmentTag);
        }
        ft.commit();
    }

    protected final boolean isRTL(){
        return getResources().getBoolean(R.bool.isRTL);
    }

}