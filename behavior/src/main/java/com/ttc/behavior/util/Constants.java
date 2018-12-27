package com.ttc.behavior.util;

import com.ttc.behavior.BuildConfig;

public class Constants {
    public static final String ACTION_RPC_URL = BuildConfig.ACTION_RPC_URL;
    public static final String TOKEN_RPC_URL = BuildConfig.TOKEN_RPC_URL;

    public static final String SIGN_TYPE = "MD5";
    public static final String DATA_PREFIX = "ufo:1:oplog:md5:";

    public static final int MAX_RETRY_COUNT = 5;

    public static final String TTC_APP_ID = "TTC_APP_ID";
    public static final String TTC_APP_SECRET_KEY = "TTC_APP_SECRET_KEY";

    public static final String ONE_QUINTILLION = "1000000000000000000";
    public static final int ACTION_TYPE_MIN_VALUE = 101;  //最小值

    public static final int BIND_STATE_UNBOUND = 0;  //未绑定
    public static final int BIND_STATE_BOUND = 1;    //绑定

    public static final long ONE_DAY_MILLISECOND = 86400000L;

}
