package com.farmers.underground.ui.fragments;

import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.method.PasswordTransformationMethod;
import android.text.method.TransformationMethod;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.farmers.underground.R;
import com.farmers.underground.remote.RetrofitSingleton;
import com.farmers.underground.remote.models.ErrorMsg;
import com.farmers.underground.remote.models.SuccessMsg;
import com.farmers.underground.remote.util.ACallback;
import com.farmers.underground.ui.activities.LoginSignUpActivity;
import com.farmers.underground.ui.activities.MainActivity;
import com.farmers.underground.ui.base.BaseFragment;
import com.farmers.underground.ui.utils.ValidationUtil;

/**
 * Created by tZpace
 * on 25-Sep-15.
 */
public class LoginFragment extends BaseFragment<LoginSignUpActivity> implements View.OnClickListener {

    private EditText etEmail, etPassword;
    private TextView tvRegister, tvForgot;
    private LinearLayout btnLogin, btnLoginFB;
    private ImageView ivShowPass;

    private boolean isVisiablePass = true;

    @Override
    protected int getLayoutResId() {
        return R.layout.fragment_login;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        findViews(view);
        setListeners();
        showHidePass();
    }

    private void findViews(View view){
        etEmail = (EditText) view.findViewById(R.id.etEmail);
        etPassword = (EditText) view.findViewById(R.id.etPassword);
        tvRegister = (TextView) view.findViewById(R.id.tvRegister);
        tvForgot = (TextView) view.findViewById(R.id.tvForgot);
        btnLogin = (LinearLayout) view.findViewById(R.id.btnLogin);
        btnLoginFB = (LinearLayout) view.findViewById(R.id.btnLoginFB);
        ivShowPass = (ImageView) view.findViewById(R.id.ivShowPass);
    }

    private void setListeners(){
        tvRegister.setOnClickListener(this);
        tvForgot.setOnClickListener(this);
        btnLogin.setOnClickListener(this);
        btnLoginFB.setOnClickListener(this);
        ivShowPass.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tvRegister:
                getHostActivity().switchFragment(SignUpFragment.class.getName(), true);
                break;
            case R.id.btnLogin:
                login();
                break;
            case R.id.btnLoginFB:
                break;
            case R.id.ivShowPass:
                showHidePass();
                break;
            case R.id.tvForgot:
                getHostActivity().showToast("To be done, later", Toast.LENGTH_SHORT);
                break;
        }
    }

    private void showHidePass(){
        if(isVisiablePass){
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

    private void login(){
        String email = etEmail.getText().toString();
        String password = etPassword.getText().toString();
        if(isEmpty(email, "email") || isEmpty(password, "password")){
            return;
        }
        if(!ValidationUtil.isValidEmail(email)){
            getHostActivity().showToast("Email is incorrect", Toast.LENGTH_SHORT);
            return;
        }
        if(!ValidationUtil.isValidPassword(password)){
            getHostActivity().showToast("Password is incorrect", Toast.LENGTH_SHORT);
            return;
        }

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

        });
    }

    private boolean isEmpty(String field, String nameField){
        if(field.length() == 0){
            getHostActivity().showToast("Please enter " + nameField, Toast.LENGTH_SHORT);
            return true;
        }
        return false;
    }

}