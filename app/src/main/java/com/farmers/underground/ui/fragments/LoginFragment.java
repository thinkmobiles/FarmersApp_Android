package com.farmers.underground.ui.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.farmers.underground.R;
import com.farmers.underground.ui.activities.LoginSignUpActivity;
import com.farmers.underground.ui.base.BaseFragment;

/**
 * Created by tZpace
 * on 25-Sep-15.
 */
public class LoginFragment extends BaseFragment<LoginSignUpActivity> implements View.OnClickListener {

    private EditText etEmail, etPassword;
    private TextView tvRegister, tvForgot;
    private LinearLayout btnLogin, btnLoginFB;
    private ImageView ivShowPass;

    @Override
    protected int getLayoutResId() {
        return R.layout.fragment_login;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        findViews(view);
        setListeners();
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
                getHostActivity().getSupportFragmentManager().beginTransaction()
                        .replace(getHostActivity().getFragmentContainerId(), new SignUpFragment())
                        .commit();
                break;
            case R.id.btnLogin:
                break;
            case R.id.btnLoginFB:
                break;
            case R.id.ivShowPass:
                break;
            case R.id.tvForgot:
                break;
        }
    }
}