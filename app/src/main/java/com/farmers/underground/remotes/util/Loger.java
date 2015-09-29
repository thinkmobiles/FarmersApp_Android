package com.farmers.underground.remotes.util;

import android.util.Log;

import com.squareup.okhttp.Interceptor;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import com.squareup.okhttp.ResponseBody;

import java.io.IOException;

import okio.Buffer;

/**
 * Created by tZpace
 * on 28-Sep-15.
 */
public final class Loger {

    private static final String TAG = "API Loger";

    public static OkHttpClient getLogerClient() {

        final OkHttpClient client = new OkHttpClient();

        client.interceptors().add(new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {

                final Request request = chain.request();

                final Response response = chain.proceed(request);

                // Do anything with response here
                Log.d(TAG, "outgoing: " + request.toString() + " body = " + getBody(request));

                final String bodyString = response.body().string();

                Log.d(TAG, "incoming: " + response.toString()  + " body = " + bodyString);

                return response.newBuilder()
//                        .headers(response.headers())
                        .body(ResponseBody.create(response.body().contentType(), bodyString))
                        .build();
            }
        });

        return client;
    }

    private static String getBody(final Request request){
        Buffer buffer = new Buffer();
        try {
            request.body().writeTo(buffer);
        } catch (IOException e) {
            return e.getMessage();
        }
        return buffer.readUtf8();
    }
}
