package com.farmers.underground.ui.fragments;

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
import com.farmers.underground.ui.base.BaseFragment;
import com.farmers.underground.ui.utils.ValidationUtil;

/**
 * Created by tZpace
 * on 25-Sep-15.
 */
public class SignUpFragment extends BaseFragment<LoginSignUpActivity> implements View.OnClickListener {

    private ImageView ivBack, ivShowPass;
    private TextView tvLogin;
    private LinearLayout llSignUp, llRegisterFB;
    private EditText etName, etEmail, etPassword;

    private boolean isVisiablePass = true;

    @Override
    protected int getLayoutResId() {
        return R.layout.fragment_sign_up;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        findViews(view);
        setListeners();
        showHidePass();
    }

    private void findViews(View view){
        ivBack = (ImageView) view.findViewById(R.id.ivBack_FSU);
        ivShowPass = (ImageView) view.findViewById(R.id.ivShowPass_FSU);
        tvLogin = (TextView) view.findViewById(R.id.tvLogin_FSU);
        llSignUp = (LinearLayout) view.findViewById(R.id.btnSignUp_FSU);
        llRegisterFB = (LinearLayout) view.findViewById(R.id.btnSignUpFB_FSU);
        etName = (EditText) view.findViewById(R.id.etName_FSU);
        etEmail = (EditText) view.findViewById(R.id.etEmail_FSU);
        etPassword = (EditText) view.findViewById(R.id.etPassword_FSU);
    }

    private void setListeners(){
        ivBack.setOnClickListener(this);
        ivShowPass.setOnClickListener(this);
        llSignUp.setOnClickListener(this);
        llRegisterFB.setOnClickListener(this);
        tvLogin.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.ivBack_FSU:
                getHostActivity().getSupportFragmentManager().popBackStack();
                break;
            case R.id.ivShowPass_FSU:
                showHidePass();
                break;
            case R.id.tvLogin_FSU:
                getHostActivity().getSupportFragmentManager().popBackStack();
                break;
            case R.id.btnSignUp_FSU:
                signUp();
                break;
            case R.id.btnSignUpFB_FSU:
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

    private void signUp(){
        String name = etName.getText().toString();
        String email = etEmail.getText().toString();
        String password = etPassword.getText().toString();
        if(isEmpty(name, "name") || isEmpty(email, "email") || isEmpty(password, "password")){
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
        //todo showProgressDialog
//        RetrofitSingleton.getInstance().registerViaEmail("FirstName LastName",/* "test" + System.currentTimeMillis() +*/   "tapacko7@gmail.com", "testpass", new ACallback<SuccessMsg, ErrorMsg>() {
        RetrofitSingleton.getInstance().registerViaEmail(name, email, password, new ACallback<SuccessMsg, ErrorMsg>() {
            @Override
            public void onSuccess(SuccessMsg result) {
                getHostActivity().showToast(result.getSuccessMsg(), Toast.LENGTH_SHORT);
                getHostActivity().getSupportFragmentManager().popBackStack();
                getHostActivity().switchFragment(LoginAfterRegistrationAFragment.class.getName(), false);
            }

            @Override
            public void onError(@NonNull ErrorMsg error) {
                getHostActivity().showToast(error.getErrorMsg(), Toast.LENGTH_SHORT);
            }

            @Override
            public void anyway() {
                //todo hideProgressDialog
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