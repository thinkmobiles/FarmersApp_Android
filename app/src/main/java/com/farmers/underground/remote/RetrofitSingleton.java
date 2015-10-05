package com.farmers.underground.remote;

import android.support.annotation.NonNull;
import com.farmers.underground.BuildConfig;
import com.farmers.underground.FarmersApp;
import com.farmers.underground.config.ApiConstants;
import com.farmers.underground.remote.models.ErrorMsg;
import com.farmers.underground.remote.models.SuccessMsg;
import com.farmers.underground.remote.models.UserCredentials;
import com.farmers.underground.remote.models.UserProfile;
import com.farmers.underground.remote.models.UserRegistration;
import com.farmers.underground.remote.models.UserSignUpFB;
import com.farmers.underground.remote.services.AuthorizationService;
import com.farmers.underground.remote.util.ACallback;
import com.farmers.underground.remote.util.AddCookiesInterceptor;
import com.farmers.underground.remote.util.ICallback;
import com.farmers.underground.remote.util.Loger;
import com.farmers.underground.remote.util.ReceivedCookiesInterceptor;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.ResponseBody;
import retrofit.*;

import java.io.IOException;

/**
 * Created by tZpace
 * on 28-Sep-15.
 *
 * HELP
 * Retrofit 2.0
 * http://inthecheesefactory.com/blog/retrofit-2.0/en
 */
public class RetrofitSingleton {

    private final Retrofit retrofit;

    private static RetrofitSingleton ourInstance = new RetrofitSingleton();

    public static RetrofitSingleton getInstance() {
        return ourInstance;
    }

    private RetrofitSingleton() {


        final OkHttpClient client = new OkHttpClient();

        Retrofit.Builder retroBuilder = new Retrofit.Builder();

        retroBuilder.baseUrl(ApiConstants.BASE_URL);

        if (BuildConfig.DEBUG)
            client.interceptors().add(new Loger());

        client.interceptors().add(new AddCookiesInterceptor());
        client.interceptors().add(new ReceivedCookiesInterceptor());
        //OR
       /* CookieManager cookieManager = new CookieManager();
        cookieManager.setCookiePolicy(CookiePolicy.ACCEPT_ALL);
        client.setCookieHandler(cookieManager);*/

        retroBuilder.client(client);

        retroBuilder.addConverterFactory(GsonConverterFactory.create());

        retrofit = retroBuilder.build();
    }


    private AuthorizationService authorizationService;

    private void initAuthorizationService(Retrofit retrofit) {
        authorizationService = retrofit.create(AuthorizationService.class);
    }

    public AuthorizationService getAuthorizationService() {
        if (authorizationService == null)
            initAuthorizationService(retrofit);

        return authorizationService;
    }


    public void registerViaEmail(@NonNull String fullName, @NonNull String email, @NonNull String pass, final ACallback<SuccessMsg, ErrorMsg> callback) {

        getAuthorizationService().registerViaEmail(new UserRegistration(fullName, email, pass)).enqueue(new Callback<SuccessMsg>() {

            @Override
            public void onResponse(Response<SuccessMsg> response, Retrofit retrofit) {
                performCallback(callback, response);
                callback.anyway();
            }

            @Override
            public void onFailure(Throwable t) {
                callback.onError(new ErrorMsg("Unknown Error"));
                callback.anyway();
            }

        });
    }

    public void loginViaEmail(@NonNull String email, @NonNull String pass, final ACallback<SuccessMsg, ErrorMsg> callback) {

        getAuthorizationService().loginViaEmail(new UserCredentials(email, pass)).enqueue(new Callback<SuccessMsg>() {
            @Override
            public void onResponse(Response<SuccessMsg> response, Retrofit retrofit) {
                performCallback(callback, response);
                callback.anyway();
            }

            @Override
            public void onFailure(Throwable t) {
                callback.onError(new ErrorMsg("Unknown Error"));
                callback.anyway();
            }
        });
    }

    public void signUpFb(@NonNull UserSignUpFB user, final ICallback<SuccessMsg, ErrorMsg> callback) {

        getAuthorizationService().signUpFb(user).enqueue(new Callback<SuccessMsg>() {
            @Override
            public void onResponse(Response<SuccessMsg> response, Retrofit retrofit) {
                performCallback(callback, response);
                callback.anyway();
            }

            @Override
            public void onFailure(Throwable t) {
                callback.onError(new ErrorMsg("Unknown Error"));
                callback.anyway();
            }
        });
    }

    //TODO this is just for testing
    public void dellAccountByEmail(final ACallback<SuccessMsg, ErrorMsg> callback) {
        getAuthorizationService().dellAccountByEmail().enqueue(new Callback<SuccessMsg>() {
            @Override
            public void onResponse(Response<SuccessMsg> response, Retrofit retrofit) {
                performCallback(callback, response);
                callback.anyway();
            }

            @Override
            public void onFailure(Throwable t) {
                callback.onError(new ErrorMsg("Unknown Error"));
                callback.anyway();
            }
        });
    }


    public void signOut(final ACallback<SuccessMsg, ErrorMsg> callback) {

        getAuthorizationService().signOut().enqueue(new Callback<SuccessMsg>() {

            @Override
            public void onResponse(Response<SuccessMsg> response, Retrofit retrofit) {
                performCallback(callback, response);
                callback.anyway();
            }

            @Override
            public void onFailure(Throwable t) {
                callback.onError(new ErrorMsg("Unknown Error"));
                callback.anyway();
            }
        });
    }

    public void getUserProfileBySession(final ACallback<UserProfile, ErrorMsg> callback) {
        getAuthorizationService().getUserProfileBySession().enqueue(new Callback<UserProfile>() {

            @Override
            public void onResponse(Response<UserProfile> response, Retrofit retrofit) {
                //TODO
                callback.onSuccess(response.body());
                callback.anyway();
            }

            @Override
            public void onFailure(Throwable t) {
                //TODO
                //do relogin maybe

                callback.onError(new ErrorMsg("Unknown Error"));
                callback.anyway();
            }
        });
    }

    public void forgotPass(@NonNull String email, final ACallback<SuccessMsg, ErrorMsg> callback) {

        getAuthorizationService().forgotPass(email).enqueue(new Callback<SuccessMsg>() {

            @Override
            public void onResponse(Response<SuccessMsg> response, Retrofit retrofit) {
                performCallback(callback, response);
                callback.anyway();
            }

            @Override
            public void onFailure(Throwable t) {
                callback.onError(new ErrorMsg("Unknown Error"));
                callback.anyway();
            }
        });
    }

    private static final Converter<ResponseBody, ?> errorConverter = GsonConverterFactory.create().fromResponseBody(ErrorMsg.class, null) ;

    private <R> void performCallback(ACallback<R, ErrorMsg> callback, Response<R> response) {
        if (response.isSuccess()) {
            callback.onSuccess(response.body());
        } else {
            callback.onError(parseErrorMsg(response.errorBody()));
        }
    }
    private <R> void performCallback(ICallback<R, ErrorMsg> callback, Response<R> response) {
        if (response.isSuccess()) {
            callback.onSuccess(response.body());
        } else {
            callback.onError(parseErrorMsg(response.errorBody()));
        }
    }

    private static ErrorMsg parseErrorMsg(ResponseBody rawResponseBody) {
        ErrorMsg myError;
        try {
            myError = (ErrorMsg) errorConverter.convert(rawResponseBody);
        } catch (IOException e) {
            if (BuildConfig.DEBUG)
                myError = new ErrorMsg(e.getMessage());
            else
                myError = new ErrorMsg("Unknown Error");
        }
        return myError;
    }

}
