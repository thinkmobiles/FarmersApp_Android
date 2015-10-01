package com.farmers.underground.remote.services;

import com.farmers.underground.remote.models.SuccessMsg;
import com.farmers.underground.remote.models.UserCredentials;
import com.farmers.underground.remote.models.UserRegistration;
import retrofit.Call;
import retrofit.http.Body;
import retrofit.http.Field;
import retrofit.http.FormUrlEncoded;
import retrofit.http.POST;

/**
 * Created by tZpace
 * on 28-Sep-15.
 */
public interface AuthorizationService {

    @POST("users/register")
    Call<SuccessMsg> registerViaEmail(@Body UserRegistration user);

    @POST("users/signIn")
    Call<SuccessMsg> loginViaEmail(@Body UserCredentials user);

    @POST("users/signOut")
    Call<SuccessMsg> signOut();

    @FormUrlEncoded
    @POST("users/forgotPass")
    Call<SuccessMsg> forgotPass(@Field("email") String email);

}
