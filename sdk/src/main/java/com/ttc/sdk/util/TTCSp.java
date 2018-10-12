package com.ttc.sdk.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

import java.math.BigInteger;

public class TTCSp {


    private static final String SP_NAME = "ttc_sp";
    private static final String KEY_USER_ID = "user_id";
    private static final String KEY_APP_ID = "key_app_id";
    private static final String KEY_SECRET_KEY = "key_secret_key";
    private static final String NEXT_NONCE = "last_nonce";  //compare easily


    public static void setUserId(Context context, String userId) {
        SharedPreferences sp = context.getSharedPreferences(SP_NAME, Context.MODE_PRIVATE);
        sp.edit().putString(KEY_USER_ID, userId).apply();
    }

    public static void deleteUserId(Context context) {
        SharedPreferences sp = context.getSharedPreferences(SP_NAME, Context.MODE_PRIVATE);
        sp.edit().remove(KEY_USER_ID).apply();
    }

    public static String getUserId(Context context) {
        SharedPreferences sp = context.getSharedPreferences(SP_NAME, Context.MODE_PRIVATE);
        String userId = sp.getString(KEY_USER_ID, "");
        TTCLogger.d("sp userId:" + userId);
        return userId;
    }

    public static void setAppId(Context context, String appId) {
        SharedPreferences sp = context.getSharedPreferences(SP_NAME, Context.MODE_PRIVATE);
        sp.edit().putString(KEY_APP_ID, appId).apply();
    }

    public static String getAppId(Context context) {
        SharedPreferences sp = context.getSharedPreferences(SP_NAME, Context.MODE_PRIVATE);
        String appId = sp.getString(KEY_APP_ID, "");
        return appId;
    }

    public static void setSecretKey(Context context, String secretKey) {
        SharedPreferences sp = context.getSharedPreferences(SP_NAME, Context.MODE_PRIVATE);
        sp.edit().putString(KEY_SECRET_KEY, secretKey).apply();
    }

    public static String getSecretKey(Context context) {
        SharedPreferences sp = context.getSharedPreferences(SP_NAME, Context.MODE_PRIVATE);
        String secretKey = sp.getString(KEY_SECRET_KEY, "");
        return secretKey;
    }

    //write the last nonce
    public static void setNextNonce(Context context, BigInteger nonce) {
        if (nonce != null) {
            SharedPreferences sp = context.getSharedPreferences(SP_NAME, Context.MODE_PRIVATE);
            sp.edit().putString(NEXT_NONCE, nonce.toString()).apply();
        }
    }

    public static BigInteger getNextNonce(Context context) {
        SharedPreferences sp = context.getSharedPreferences(SP_NAME, Context.MODE_PRIVATE);
        String sNonce =  sp.getString(NEXT_NONCE, "");
        if (!TextUtils.isEmpty(sNonce) && TextUtils.isDigitsOnly(sNonce)) {
            return new BigInteger(sNonce);
        }
        return BigInteger.ZERO;
    }

}
