package com.farmers.underground.remote.util;

import android.util.Log;

import com.squareup.okhttp.*;
import okio.Buffer;

import java.io.IOException;

/**
 * Created by tZpace
 * on 28-Sep-15.
 */
public final class Loger implements Interceptor {

    private static final String TAG = "API Loger";

    private static String getBody(final Request request){
        Buffer buffer = new Buffer();
        try {
            request.body().writeTo(buffer);
        } catch (Exception e) {
            return e.getMessage();
        }
        return buffer.readUtf8();
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        final Request request = chain.request();

        final Response response = chain.proceed(request);

        // Do anything with response here
        Log.d(TAG, "outgoing: " + request.toString() + " body = " + getBody(request));

        final String bodyString = response.body().string();

        Log.d(TAG, "incoming: " + response.toString()  + " body = " + bodyString);


        return response.newBuilder()
                .headers(response.headers())
                .body(ResponseBody.create(response.body().contentType(), bodyString))
                .build();
    }
}
