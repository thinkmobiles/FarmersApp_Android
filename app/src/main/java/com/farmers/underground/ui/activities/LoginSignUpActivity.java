package com.farmers.underground.ui.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.farmers.underground.FarmersApp;
import com.farmers.underground.R;
import com.farmers.underground.config.FB;
import com.farmers.underground.remote.RetrofitSingleton;
import com.farmers.underground.remote.models.ErrorMsg;
import com.farmers.underground.remote.models.SuccessMsg;
import com.farmers.underground.remote.models.UserProfile;
import com.farmers.underground.remote.models.UserSignUpFB;
import com.farmers.underground.remote.util.ACallback;
import com.farmers.underground.remote.util.ICallback;
import com.farmers.underground.ui.base.BaseActivity;
import com.farmers.underground.ui.fragments.LoginAfterRegistrationAFragment;
import com.farmers.underground.ui.fragments.LoginFragment;
import com.farmers.underground.ui.fragments.SignUpFragment;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by tZpace
 * on 25-Sep-15.
 */
public class LoginSignUpActivity extends BaseActivity implements ICallback<SuccessMsg, ErrorMsg> {

    private CallbackManager callbackManager;
    private String nameMarketeer = null;

    @Override
    public int getLayoutResId() {
        return R.layout.activity_login_signup;
    }

    @Override
    public int getFragmentContainerId() {
        return R.id.fr_containrer_LSA;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        switchFragment(LoginFragment.class.getName(), false);


        callbackManager = CallbackManager.Factory.create();

        LoginManager.getInstance().registerCallback(callbackManager,
                new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {
                        showProgressDialog();
                        fethMyFB(loginResult.getAccessToken());
                    }

                    @Override
                    public void onCancel() {
                        // App code
                    }

                    @Override
                    public void onError(FacebookException exception) {
                        // App code
                        if (exception != null)
                            showToast(exception.getMessage(), Toast.LENGTH_SHORT);
                    }
                });

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    private void fethMyFB(@NonNull final AccessToken accessToken) {
        GraphRequest request = GraphRequest.newMeRequest(
                accessToken,
                new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(JSONObject object, GraphResponse response) {

                        if (response.getError() == null) {
                            try {

                                String id = object.getString(FB.id);        //FB id
                                String name = object.optString(FB.name);    //full name
                                String email = object.optString(FB.email);  //can be null
                                String picture = "";

                                if (object.has(FB.picture)) {
                                    try {
                                        picture = object.getJSONObject(FB.picture).getJSONObject("data").optString("url"); //pic url
                                    } catch (JSONException e) {
                                       /* ignore */
                                    }
                                }
                                if (TextUtils.isEmpty(id)||TextUtils.isEmpty(accessToken.getToken())){
                                    onError(new ErrorMsg("No facebook id")); //<--TODO
                                } else {
                                    final UserSignUpFB userSignUpFB = new UserSignUpFB(id, accessToken.getToken(), picture, name, email);
                                    apiCallSignupFb(userSignUpFB);
                                }
                            } catch (JSONException e) {
                                onError(new ErrorMsg(e.getMessage()));
                            }

                        } else {
                            onError(new ErrorMsg(response.getError().toString()));
                        }

                    }
                });
        final Bundle parameters = new Bundle();

        parameters.putString(FB.fields, FB.id + "," +
                FB.name + "," +
                FB.email + "," +
                FB.picture);
        request.setParameters(parameters);
        request.executeAsync();
    }

    public void apiCallSignupFb(@NonNull UserSignUpFB userSignUpFB){
        RetrofitSingleton.getInstance().signUpFb(userSignUpFB, this);
    }


    @Override
    public void onSuccess(SuccessMsg result) {
        // TODO switch to add marketire screen A or MainActivity

        if (true) {
            switchFragment(new LoginAfterRegistrationAFragment(),false);
        } else {

        }

        showToast(result.getSuccessMsg(), Toast.LENGTH_SHORT);
        anyway();
    }

    @Override
    public void onError(@NonNull ErrorMsg error) {
        // TODO

        showToast(error.getErrorMsg(), Toast.LENGTH_SHORT);
        anyway();
    }

    @Override
    public void anyway() {
        hideProgressDialog();
    }

    public String getNameMarketeer() {
        return nameMarketeer;
    }

    public void setNameMarketeer(String nameMarketeer) {
        this.nameMarketeer = nameMarketeer;
    }

    public void getUserProfileAsync() {
        showProgressDialog();
        FarmersApp.getInstance().getUserProfileAsync(new ICallback<UserProfile, ErrorMsg>() {
            @Override
            public void onSuccess(UserProfile result) {

            }

            @Override
            public void onError(@NonNull ErrorMsg error) {

            }

            @Override
            public void anyway() {
                hideProgressDialog();
            }
        });
    }
}
