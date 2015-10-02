package com.farmers.underground.ui.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.farmers.underground.R;
import com.farmers.underground.config.FB;
import com.farmers.underground.remote.util.ICallback;
import com.farmers.underground.ui.base.BaseActivity;
import com.farmers.underground.ui.fragments.LoginFragment;
import com.farmers.underground.ui.fragments.SignUpFragment;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by tZpace
 * on 25-Sep-15.
 */
public class LoginSignUpActivity extends BaseActivity implements ICallback {

    private CallbackManager callbackManager;

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

    private void fethMyFB(final AccessToken accessToken) {
        GraphRequest request = GraphRequest.newMeRequest(
                accessToken,
                new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(JSONObject object, GraphResponse response) {

                        if (response.getError() == null){
                            try {
                                //TODO check if json has next keys;

                                object.get(FB.id);      //FB id
                                object.get(FB.name);    //full name
                                object.get(FB.email);   //can be null
                                object.get(FB.picture); //link
                                // + accessToken

                                if(object.has(FB.picture) && getCurrentFragment() instanceof SignUpFragment){
                                    try {
                                        Picasso.with(LoginSignUpActivity.this)
                                                .load(object.getJSONObject(FB.picture).getJSONObject("data").getString("url"))
                                                .into(((SignUpFragment) getCurrentFragment()).getImageView3());
                                    } catch (JSONException e) {
                                        /*ignore*/
                                    }

                                }
                                onSuccess(null);  // <-- TODO

                            } catch (JSONException e) {
                                onError(e.getMessage());
                            }

                        } else {
                            onError(response.getError());
                        }

                    }
                });
        final Bundle parameters = new Bundle();

        parameters.putString(FB.fields,  FB.id      +","+
                                         FB.name    +","+
                                         FB.email   +","+
                                         FB.picture);
        request.setParameters(parameters);
        request.executeAsync();
    }

    @Override
    public void onSuccess(Object result) {

        // TODO switch to add marketir screen A

    }

    @Override
    public void onError(@NonNull Object error) {

    }

    @Override
    public void anyway() {

    }
}
