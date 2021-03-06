package com.farmers.underground.ui.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.widget.Toast;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.farmers.underground.BuildConfig;
import com.farmers.underground.FarmersApp;
import com.farmers.underground.R;
import com.farmers.underground.remote.RetrofitSingleton;
import com.farmers.underground.remote.models.ErrorMsg;
import com.farmers.underground.remote.models.FarmerPricesModel;
import com.farmers.underground.remote.models.LastCropPricesModel;
import com.farmers.underground.remote.models.MarketeerPricesByDateModel;
import com.farmers.underground.remote.models.StaticticModel;
import com.farmers.underground.remote.models.SuccessMsg;
import com.farmers.underground.remote.models.UserPriceQualityModel;
import com.farmers.underground.remote.util.ACallback;
import com.farmers.underground.ui.base.BaseActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by tZpace
 * on 24-Sep-15.
 */
public class TestActivity extends BaseActivity {

    private Intent intent;

    @Override
    public int getLayoutResId() {
        return R.layout.activity_test;
    }

    @Override
    protected int getFragmentContainerId() {
        return 0;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (BuildConfig.PRODUCTION) {
            if(FarmersApp.showTutorial()){
                intent = new Intent(this, TutorialActivity.class);
            } else {
                intent = new Intent(this, LoginSignUpActivity.class);
            }
            startActivity(intent);
            finish();
            return;
        }

        ButterKnife.bind(this);
    }

    @OnClick(R.id.tv_tutorialAct)
    void startTutorialActivity() {
        intent = new Intent(this, TutorialActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.tv_loginAct)
    void startLoginActivity() {
        intent = new Intent(this, LoginSignUpActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.tv_mainAct)
    void startMainActivity() {
        MainActivity.start(this);
    }

    @OnClick(R.id.btn_api_call_test)
    void testApiCallsReg() {


        RetrofitSingleton.getInstance().getStatisticsOfQualityAndMonth("ארטישוק ירוק", "סוג א", 11, new ACallback<List<StaticticModel>, ErrorMsg>() {
            @Override
            public void onSuccess(List<StaticticModel> result) {
                showToast("OK",Toast.LENGTH_SHORT);
            }

            @Override
            public void onError(@NonNull ErrorMsg error) {
                showToast("Bad",Toast.LENGTH_SHORT);
            }
        });
    }

    @OnClick(R.id.btn_wipe_app_prefs)
    void testWipeAppPrefs() {
        FarmersApp.wipeAppPreferences();
    }

    @OnClick(R.id.btn_wipe_usr_prefs)
    void testWipeUsrPrefs() {
        FarmersApp.wipeUsrPreferences();
    }

    @OnClick(R.id.tv_log_Out)
    void testLogout() {
        RetrofitSingleton.getInstance().signOut(new ACallback<SuccessMsg, ErrorMsg>() {
            @Override
            public void onSuccess(SuccessMsg result) {
                FarmersApp.getInstance().onUserLogOut();
            }

            @Override
            public void onError(@NonNull ErrorMsg error) {

            }
        });

    }

    @OnClick(R.id.tv_dellAccountByEmail)
    void dellAccountBySession() {
        RetrofitSingleton.getInstance().dellAccountBySession(new ACallback<SuccessMsg, ErrorMsg>() {
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

    @OnClick(R.id.tv_transp_Activity)
    void startTransparentActivity() {
        TransparentActivity.start(this);
    }

}
