package com.farmers.underground.ui.fragments;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.text.method.PasswordTransformationMethod;
import android.text.method.TransformationMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

import com.facebook.login.LoginManager;
import com.farmers.underground.R;
import com.farmers.underground.config.FB;
import com.farmers.underground.remote.RetrofitSingleton;
import com.farmers.underground.remote.models.ErrorMsg;
import com.farmers.underground.remote.models.SuccessMsg;
import com.farmers.underground.remote.util.ACallback;
import com.farmers.underground.ui.activities.LoginSignUpActivity;
import com.farmers.underground.ui.activities.MainActivity;
import com.farmers.underground.ui.base.BaseFragment;
import com.farmers.underground.ui.utils.ValidationUtil;

import java.util.Arrays;

/**
 * Created by tZpace
 * on 25-Sep-15.
 */
public class LoginFragment extends BaseFragment<LoginSignUpActivity> {

    @Bind(R.id.etEmail)
    protected EditText etEmail;

    @Bind(R.id.etPassword)
    protected EditText etPassword;

    private boolean isVisiablePass = true;

    @Override
    protected int getLayoutResId() {
        return R.layout.fragment_login;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
        showHidePass();
    }

    @OnClick(R.id.tvRegister)
    protected void register() {
        getHostActivity().switchFragment(SignUpFragment.class.getName(), true);
    }

    @OnClick(R.id.btnLoginFB)
    protected void loginFB() {
        LoginManager.getInstance().logInWithReadPermissions(getHostActivity(), Arrays.asList(FB.public_profile, FB.email));
    }

    @OnClick(R.id.tvForgot)
    protected void forgotPW() {

        final View view = LayoutInflater.from(getContext()).inflate(R.layout.dialog_edit_text_field, null);

        AlertDialog dialog = new AlertDialog.Builder(getContext())
                .setTitle(R.string.dialog_title_forgot_password)
                .setView(view)
                .setCancelable(true)
                .setPositiveButton(R.string.dialog_btn_ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        EditText editText = (EditText) view.findViewById(R.id.et_dialog_field);

                        String email = editText.getText().toString();

                        apiCallForgotPass(email);
                    }
                })
                .setNegativeButton(R.string.dialog_btn_cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .create();
        dialog.show();
    }


    @OnClick(R.id.ivShowPass)
    protected void showHidePass() {
        if (isVisiablePass) {
            etPassword.setTransformationMethod(new PasswordTransformationMethod());
        } else {
            etPassword.setTransformationMethod(new TransformationMethod() {
                @Override
                public CharSequence getTransformation(CharSequence source, View view) {
                    return source;
                }

                @Override
                public void onFocusChanged(View view, CharSequence sourceText, boolean focused, int direction, Rect previouslyFocusedRect) {

                }
            });
        }
        isVisiablePass = !isVisiablePass;
        etPassword.setSelection(etPassword.getText().length());
    }

    @OnClick(R.id.btnLogin)
    protected void login() {
        String email = etEmail.getText().toString();
        String password = etPassword.getText().toString();
        if (isEmpty(email, "email") || isEmpty(password, "password")) {
            return;
        }
        if (!ValidationUtil.isValidEmail(email)) {
            getHostActivity().showToast("Email is incorrect", Toast.LENGTH_SHORT);
            return;
        }
        if (!ValidationUtil.isValidPassword(password)) {
            getHostActivity().showToast("Password is incorrect", Toast.LENGTH_SHORT);
            return;
        }

        getHostActivity().showProgressDialog();
        RetrofitSingleton.getInstance().loginViaEmail(email, password, new ACallback<SuccessMsg, ErrorMsg>() {
            @Override
            public void onSuccess(SuccessMsg result) {
                getHostActivity().showToast(result.getSuccessMsg(), Toast.LENGTH_SHORT);
                // TODO: 29.09.15
                Intent intent = new Intent(getHostActivity(), MainActivity.class);
                startActivity(intent);
            }

            @Override
            public void onError(@NonNull ErrorMsg error) {
                getHostActivity().showToast(error.getErrorMsg(), Toast.LENGTH_SHORT);
            }

            @Override
            public void anyway() {
                super.anyway();
                getHostActivity().hideProgressDialog();
            }
        });
    }

    private void apiCallForgotPass(@NonNull String email) {

        if (isEmpty(email, "email")) {
            return;
        }
        if (!ValidationUtil.isValidEmail(email)) {
            getHostActivity().showToast("Email is incorrect", Toast.LENGTH_SHORT);
            return;
        }

        getHostActivity().showProgressDialog();
        RetrofitSingleton.getInstance().forgotPass(email, new ACallback<SuccessMsg, ErrorMsg>() {
            @Override
            public void onSuccess(SuccessMsg result) {
                getHostActivity().showToast(result.getSuccessMsg(), Toast.LENGTH_SHORT);
            }

            @Override
            public void onError(@NonNull ErrorMsg error) {
                getHostActivity().showToast(error.getErrorMsg(), Toast.LENGTH_SHORT);
            }

            @Override
            public void anyway() {
                getHostActivity().hideProgressDialog();
            }

        });
    }

    private boolean isEmpty(String field, String nameField) {
        if (field.length() == 0) {
            getHostActivity().showToast("Please enter " + nameField, Toast.LENGTH_SHORT);
            return true;
        }
        return false;
    }

}