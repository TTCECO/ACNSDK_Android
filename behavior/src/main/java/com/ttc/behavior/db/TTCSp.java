package com.ttc.behavior.db;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

import com.ttc.behavior.TTCAgent;
import com.ttc.behavior.util.TTCLogger;

import java.math.BigInteger;

public class TTCSp {

    //todo ttc separate env sp
    private static SharedPreferences sp;
    private static SharedPreferences.Editor editor;

    private static final String SP_NAME = "ttc_sp";
    private static final String KEY_USER_ID = "user_id";
    private static final String KEY_APP_ID = "key_app_id";
    private static final String KEY_SECRET_KEY = "key_secret_key";
    private static final String NEXT_NONCE = "next_nonce";  //compare easily
    private static final String LAST_OPEN_MS = "last_open_ms";  //上次登录的时间戳，只记录每天的第一次；the earliest one of one day

    static {
        sp = TTCAgent.getClient().getContext().getSharedPreferences(SP_NAME, Context.MODE_PRIVATE);
        editor = sp.edit();
    }

    public static void clear() {
        editor.remove(KEY_USER_ID).remove(NEXT_NONCE).remove(LAST_OPEN_MS).apply();
    }


    public static void setUserId(String userId) {
        editor.putString(KEY_USER_ID, userId).apply();
    }


    public static String getUserId() {
        String userId = sp.getString(KEY_USER_ID, "");
        TTCLogger.d("sp userId:" + userId);
        return userId;
    }

    public static void setAppId(String appId) {
        editor.putString(KEY_APP_ID, appId).apply();
    }

    public static String getAppId() {
        String appId = sp.getString(KEY_APP_ID, "");
        return appId;
    }

    public static void setSecretKey(String secretKey) {
        editor.putString(KEY_SECRET_KEY, secretKey).apply();
    }

    public static String getSecretKey() {
        String secretKey = sp.getString(KEY_SECRET_KEY, "");
        return secretKey;
    }

    //write the last nonce
    public static void setNextNonce(BigInteger nonce) {
        if (nonce != null) {
            editor.putString(NEXT_NONCE, nonce.toString()).apply();
        }
    }

    public static BigInteger getNextNonce() {
        String sNonce = sp.getString(NEXT_NONCE, "");
        if (!TextUtils.isEmpty(sNonce) && TextUtils.isDigitsOnly(sNonce)) {
            return new BigInteger(sNonce);
        }
        return BigInteger.ZERO;
    }

    /**
     * @param lastLogin mm
     */
    public static void setLastOpenTimestamp(long lastLogin) {
        editor.putLong(LAST_OPEN_MS, lastLogin).apply();
    }

    public static long getLastOpenTimestamp() {
        return sp.getLong(LAST_OPEN_MS, 0L);
    }

}
