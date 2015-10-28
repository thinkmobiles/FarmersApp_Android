package com.farmers.underground.remote;

import android.support.annotation.NonNull;
import com.farmers.underground.BuildConfig;
import com.farmers.underground.config.ApiConstants;
import com.farmers.underground.remote.models.*;
import com.farmers.underground.remote.models.base.MarketeerBase;
import com.farmers.underground.remote.models.base.PriceBase;
import com.farmers.underground.remote.services.AuthorizationService;
import com.farmers.underground.remote.services.CropsService;
import com.farmers.underground.remote.services.MarketeerService;
import com.farmers.underground.remote.services.PricesService;
import com.farmers.underground.remote.util.*;
import com.google.gson.JsonSyntaxException;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.ResponseBody;
import retrofit.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

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
    private MarketeerService marketeerService;
    private AuthorizationService authorizationService;

    private CropsService cropsService;
    private PricesService pricesService;

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
        /*OR try this
        CookieManager cookieManager = new CookieManager();
        cookieManager.setCookiePolicy(CookiePolicy.ACCEPT_ALL);
        client.setCookieHandler(cookieManager);*/

        retroBuilder.client(client);
        retroBuilder.addConverterFactory(GsonConverterFactory.create());
        retrofit = retroBuilder.build();
    }


    private void initCropsService(Retrofit retrofit) {
        cropsService = retrofit.create(CropsService.class);
    }

    private CropsService getCropsService() {
        if (cropsService == null)
            initCropsService(retrofit);

        return cropsService;
    }

    private void initPricesService(Retrofit retrofit) {
        pricesService = retrofit.create(PricesService.class);
    }

    private PricesService getPricesService() {
        if (pricesService == null)
            initPricesService(retrofit);

        return pricesService;
    }

    public void getLastCropPricesList(final ACallback<List<LastCropPricesModel>,ErrorMsg> callback){
        getPricesService().getLast().enqueue(new Callback<List<LastCropPricesModel>>() {
            @Override
            public void onResponse(Response<List<LastCropPricesModel>> response, Retrofit retrofit) {
                performCallback(callback,response);
                callback.anyway();
            }

            @Override
            public void onFailure(Throwable t) {
                callback.onError(new ErrorMsg("Network Error"));
                callback.anyway();
            }
        });
    }

    public void getCropPricesForPeriod(@NonNull String startDate, @NonNull  String endDate, @NonNull  String name,
                                       final ACallback<List<PriceBase>,ErrorMsg> callback){
        getPricesService().getCropPricesForPeriod(startDate,endDate,name).enqueue(new Callback<List<PriceBase>>() {
            @Override
            public void onResponse(Response<List<PriceBase>> response, Retrofit retrofit) {
                performCallback(callback,response);
                callback.anyway();
            }

            @Override
            public void onFailure(Throwable t) {
                callback.onError(new ErrorMsg("Network Error"));
                callback.anyway();
            }
        });
    }

    private void initMarketeerService(Retrofit retrofit) {
        marketeerService = retrofit.create(MarketeerService.class);
    }

    private MarketeerService getMarketeerService() {
        if (marketeerService == null)
            initMarketeerService(retrofit);

        return marketeerService;
    }


    public void getMarketterList(final ACallback<ArrayList<String>,ErrorMsg> callback){
        getMarketeerService().getMarketterList().enqueue(new Callback<ArrayList<String>>() {
            @Override
            public void onResponse(Response<ArrayList<String>> response, Retrofit retrofit) {
                performCallback(callback,response);
                callback.anyway();
            }

            @Override
            public void onFailure(Throwable t) {
                callback.onError(new ErrorMsg("Network Error"));
                callback.anyway();
            }
        });
    }

    public void addMarketeer(@NonNull String fullName, final ACallback<SuccessMsg,ErrorMsg> callback){
        getMarketeerService().addMarketeer(fullName).enqueue(new Callback<SuccessMsg>() {
            @Override
            public void onResponse(Response<SuccessMsg> response, Retrofit retrofit) {
                performCallback(callback,response);
                callback.anyway();
            }

            @Override
            public void onFailure(Throwable t) {
                callback.onError(new ErrorMsg("Unknown Error"));
                callback.anyway();
            }
        });
    }

    public void getMarketeerBySession(final ACallback<MarketeerBase, ErrorMsg> callback){
        getMarketeerService().getMarketeerBySession().enqueue(new Callback<MarketeerBase>() {
            @Override
            public void onResponse(Response<MarketeerBase> response, Retrofit retrofit) {
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


    private void initAuthorizationService(Retrofit retrofit) {
        authorizationService = retrofit.create(AuthorizationService.class);
    }

    private AuthorizationService getAuthorizationService() {
        if (authorizationService == null)
            initAuthorizationService(retrofit);

        return authorizationService;
    }


    public void registerViaEmail(@NonNull String fullName, @NonNull String email, @NonNull String pass, final ACallback<SuccessMsg, ErrorMsg> callback) {

        getAuthorizationService().registerViaEmail(new UserRegistration(fullName, email.toLowerCase(), pass)).enqueue(new Callback<SuccessMsg>() {

            @Override
            public void onResponse(Response<SuccessMsg> response, Retrofit retrofit) {
                performCallback(callback, response);
                callback.anyway();
            }

            @Override
            public void onFailure(Throwable t) {
                callback.onError(new ErrorMsg("Network Error"));
                callback.anyway();
            }

        });
    }

    public void loginViaEmail(@NonNull String email, @NonNull String pass, final ACallback<SuccessMsg, ErrorMsg> callback) {

        getAuthorizationService().loginViaEmail(new UserCredentials(email.toLowerCase(), pass)).enqueue(new Callback<SuccessMsg>() {
            @Override
            public void onResponse(Response<SuccessMsg> response, Retrofit retrofit) {
                performCallback(callback, response);
                callback.anyway();
            }

            @Override
            public void onFailure(Throwable t) {
                callback.onError(new ErrorMsg("Network Error"));
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


    public void addCropsToFavorites(@NonNull String displayName, final ACallback<SuccessMsg, ErrorMsg> callback) {

        getAuthorizationService().addCropsToFavorites(displayName).enqueue(new Callback<SuccessMsg>() {

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

    public void deleteCropsFromFavorites(@NonNull String displayName, final ACallback<SuccessMsg, ErrorMsg> callback) {

        getAuthorizationService().deleteCropsFromFavorites(displayName).enqueue(new Callback<SuccessMsg>() {

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

    /** this is just for testing*/
    public void dellAccountBySession(final ACallback<SuccessMsg, ErrorMsg> callback) {

            getAuthorizationService().dellAccountBySession().enqueue(new Callback<SuccessMsg>() {
                @Override
                public void onResponse(Response<SuccessMsg> response, Retrofit retrofit) {
                    performCallback(callback, response);
                    callback.anyway();
                }

                @Override
                public void onFailure(Throwable t) {
                    callback.onError(new ErrorMsg("Network Error"));
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
        } catch (JsonSyntaxException jsonSyntaxException){
                myError = new ErrorMsg("Server Error");
        }
        return myError;
    }


}
