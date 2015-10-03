package com.farmers.underground.remote.util;

import android.util.Log;

import com.farmers.underground.FarmersApp;
import com.farmers.underground.config.ProjectConstants;
import com.squareup.okhttp.Interceptor;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;
import java.util.HashSet;

/**
 * Created by tZpace
 * on 02-Oct-15.
 */
public class AddCookiesInterceptor implements Interceptor {

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request.Builder builder = chain.request().newBuilder();
        HashSet<String> preferences = (HashSet<String>)  FarmersApp.getUsrPreferences().getStringSet(ProjectConstants.KEY_CURRENT_USER_COOKIES, new HashSet<String>());
        for (String cookie : preferences) {
            builder.addHeader("Cookie", cookie);
            Log.d("OkHttp", "Adding Header: " + cookie); // This is done so I know which headers are being added; this interceptor is used after the normal logging of OkHttp
        }

        return chain.proceed(builder.build());
    }
}