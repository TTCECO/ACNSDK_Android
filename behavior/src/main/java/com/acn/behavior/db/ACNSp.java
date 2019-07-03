package com.acn.behavior.db;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;
import com.acn.behavior.ACNAgent;

import java.math.BigInteger;

public class ACNSp {

    private static SharedPreferences sp;
    private static SharedPreferences.Editor editor;

    private static final String SP_NAME = "acn_sp";
    private static final String NEXT_NONCE = "next_nonce";  //compare easily
    private static final String LAST_OPEN_MS = "last_open_ms";  //上次登录的时间戳，只记录每天的第一次；the earliest one of one day


    static {
        sp = ACNAgent.getClient().getContext().getSharedPreferences(SP_NAME, Context.MODE_PRIVATE);
        editor = sp.edit();
    }

    public static void clear() {
        editor.remove(NEXT_NONCE).remove(LAST_OPEN_MS).apply();
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
