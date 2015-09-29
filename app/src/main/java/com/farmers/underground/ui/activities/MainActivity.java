package com.farmers.underground.ui.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.widget.Toast;

import butterknife.ButterKnife;
import butterknife.OnClick;
import com.farmers.underground.R;
import com.farmers.underground.remote.RetrofitSingleton;
import com.farmers.underground.remote.models.ErrorMsg;
import com.farmers.underground.remote.models.SuccessMsg;
import com.farmers.underground.remote.util.ACallback;
import com.farmers.underground.ui.base.BaseActivity;

/**
 * Created by tZpace
 * on 24-Sep-15.
 */
public class MainActivity extends BaseActivity {

    private Intent intent;

    @Override
    public int getLayoutResId() {
        return R.layout.activity_main;
    }

    @Override
    protected int getFragmentContainerId() {
        return 0;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.tv_tutorialAct)
    void startTutorialActivity(){
        intent= new Intent(this, TutorialActivity.class);
               startActivity(intent);
    }

    @OnClick(R.id.tv_loginAct)
    void startLoginActivity(){
        intent = new Intent(this, LoginSignUpActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.btn_api_call_test_reg)
    void testApiCallsReg(){
        //todo showProgressDialog
        RetrofitSingleton.getInstance().registerViaEmail("FirstName LastName",/* "test" + System.currentTimeMillis() +*/   "tapacko7@gmail.com", "testpass", new ACallback<SuccessMsg, ErrorMsg>() {
            @Override
            public void onSuccess(SuccessMsg result) {
                showToast(result.getSuccessMsg(), Toast.LENGTH_SHORT);
            }

            @Override
            public void onError(@NonNull ErrorMsg error) {
                showToast(error.getErrorMsg(), Toast.LENGTH_SHORT);
            }

            @Override
            public void anyway() {
                //todo hideProgressDialog
            }
        });
    }

    @OnClick(R.id.btn_api_call_test_log_in)
    void testApiCallsIN() {
        RetrofitSingleton.getInstance().loginViaEmail("tapacko7@gmail.com", "testpass", new ACallback<SuccessMsg, ErrorMsg>() {
            @Override
            public void onSuccess(SuccessMsg result) {
                showToast(result.getSuccessMsg(), Toast.LENGTH_SHORT);
            }

            @Override
            public void onError(@NonNull ErrorMsg error) {
                showToast(error.getErrorMsg(), Toast.LENGTH_SHORT);
            }
        });
    }

    @OnClick(R.id.btn_api_call_test_log_out)
    void testApiCallsOUT(){
        RetrofitSingleton.getInstance().signOut(new ACallback<SuccessMsg, ErrorMsg>() {
            @Override
            public void onSuccess(SuccessMsg result) {
                showToast(result.getSuccessMsg(), Toast.LENGTH_SHORT);
            }

            @Override
            public void onError(@NonNull ErrorMsg error) {
                showToast(error.getErrorMsg(), Toast.LENGTH_SHORT);
            }
        });
    }
}
