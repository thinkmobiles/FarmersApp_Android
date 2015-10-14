package com.farmers.underground.ui.base;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;
import com.farmers.underground.R;
import com.farmers.underground.ui.dialogs.ProgressDialog;
import org.intellij.lang.annotations.MagicConstant;
import org.jetbrains.annotations.NotNull;

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

    @Override
    protected void onPause() {
        super.onPause();
    }

    public void switchFragment(@NotNull Fragment fragment, boolean saveInBackStack) {
        updateFragment(fragment, saveInBackStack, false);
    }

    public void switchFragment(@NonNull String fragmentClassName, boolean saveInBackStack) {
        updateFragment(instantiateFragment(fragmentClassName), saveInBackStack, false);
    }

    public void addFragment(@NonNull String fragmentClassName, boolean saveInBackStack) {
        updateFragment(instantiateFragment(fragmentClassName), saveInBackStack, true);
    }

    public void popBackStackUpTo(@Nullable Class<? extends BaseFragment<?>> fragmentClass, int flags) {
        getSupportFragmentManager().popBackStackImmediate(fragmentClass != null ? fragmentClass.getName() : null, flags);
    }

    public void popBackStackUpTo(@Nullable Class<? extends BaseFragment<?>> fragmentClass) {
        popBackStackUpTo(fragmentClass, 0);
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

    public final boolean isRTL(){
        return getResources().getBoolean(R.bool.isRTL);
    }

    public void showToast(String msg, @MagicConstant(intValues = {Toast.LENGTH_LONG, Toast.LENGTH_SHORT}) final int duration){
        Toast.makeText(this, msg, duration).show();
    }
    public void hideSoftKeyboard(){
        InputMethodManager imm = (InputMethodManager)  getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm.isActive() && getCurrentFocus()!=null)
            imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
    }

    private ProgressDialog mProgressDialog;

    public final void showProgressDialog(final @NotNull String progressDialogString, @Nullable DialogInterface.OnDismissListener dismissListener) {
        if (this.isFinishing()) {
            return;
        }

        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(this);
        }

        mProgressDialog.setText(progressDialogString);
        mProgressDialog.setCanceledOnTouchOutside(false);
        mProgressDialog.setCancelable(dismissListener != null);
        mProgressDialog.setOnDismissListener(dismissListener);

        if (!mProgressDialog.isShowing())
            mProgressDialog.show();
    }

    public final void showProgressDialog(@Nullable DialogInterface.OnDismissListener dismissListener) {
        showProgressDialog(getString(R.string.dialog_progress_message_default), dismissListener);
    }

    public void showProgressDialog() {
        showProgressDialog((DialogInterface.OnDismissListener) null);
    }


    public void hideProgressDialog() {
        if (mProgressDialog != null) {
            if (mProgressDialog.isShowing())
                mProgressDialog.dismiss();
            mProgressDialog = null;
        }
    }
}