package com.farmers.underground.config;

import com.farmers.underground.BuildConfig;

/**
 * Created by tZpace
 * on 28-Sep-15.
 */
public final class ApiConstants {

    /**
      *  global "134.249.164.53"
      *  local "192.168.88.250"
      *
      * */
    public static final String IP = BuildConfig.PRODUCTION ? "134.249.164.53": "192.168.88.250";

    public static final String PORT = "7792";

    public static final String BASE_URL = "http://"+ IP + ":" + PORT + "/";

}
