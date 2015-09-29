package com.farmers.underground.remote.services;

/**
 * Created by tZpace on 28-Sep-15.
 */
public class RetrofitSingleton {
    private static RetrofitSingleton ourInstance = new RetrofitSingleton();

    public static RetrofitSingleton getInstance() {
        return ourInstance;
    }

    private RetrofitSingleton() {
    }
}
