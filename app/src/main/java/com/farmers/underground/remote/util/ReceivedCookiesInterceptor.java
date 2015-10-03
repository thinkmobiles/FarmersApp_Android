package com.farmers.underground.remote.util;

import com.farmers.underground.FarmersApp;
import com.farmers.underground.config.ProjectConstants;
import com.squareup.okhttp.Interceptor;
import com.squareup.okhttp.Response;

import java.io.IOException;
import java.util.HashSet;

/**
 * Created by tZpace
 * on 02-Oct-15.
 */
public class ReceivedCookiesInterceptor implements Interceptor {
    @Override
    public Response intercept(Chain chain) throws IOException {
        Response originalResponse = chain.proceed(chain.request());

        if (!originalResponse.headers("Set-Cookie").isEmpty()) {
            HashSet<String> cookies = new HashSet<>();

            for (String header : originalResponse.headers("Set-Cookie")) {
                cookies.add(header);
            }

            FarmersApp.getUsrPreferences().edit()
                    .putStringSet(ProjectConstants.KEY_CURRENT_USER_COOKIES, cookies)
                    .apply();
        }

        return originalResponse;
    }
}
