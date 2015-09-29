package com.farmers.underground.ui.fragments;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.farmers.underground.R;
import com.farmers.underground.ui.activities.LoginSignUpActivity;
import com.farmers.underground.ui.base.BaseFragment;

/**
 * Created by tZpace
 * on 25-Sep-15.
 */
public class SignUpFragment extends BaseFragment<LoginSignUpActivity> implements View.OnClickListener {

    private ImageView ivBack, ivShowPass;
    private TextView tvLogin;
    private LinearLayout llSignUp, llRegisterFB;
    private EditText etName, etEmail, etPassword;

    @Override
    protected int getLayoutResId() {
        return R.layout.fragment_sign_up;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        findViews(view);
        setListeners();
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
                break;
            case R.id.ivShowPass_FSU:
                break;
            case R.id.tvLogin_FSU:
                break;
            case R.id.btnSignUp_FSU:
                getHostActivity().getSupportFragmentManager().beginTransaction()
                        .replace(getHostActivity().getFragmentContainerId(), new LoginAfterRegistrationAFragment())
                        .commit();
                break;
            case R.id.btnSignUpFB_FSU:
                break;
        }
    }
}