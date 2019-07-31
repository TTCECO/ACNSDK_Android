package com.acn.behavior.db;

import android.content.Context;
import android.content.SharedPreferences;
import com.acn.behavior.ACNAgent;
import com.acn.behavior.util.SDKLogger;

public class ACNSp {

    private static SharedPreferences sp;
    private static SharedPreferences.Editor editor;

    private static final String SP_NAME = "acn_sp";
    private static final String KEY_USER_ID = "user_id";
    private static final String KEY_DAPP_ID = "key_dapp_id";
    private static final String KEY_DAPP_SECRET_KEY = "key_dapp_secret_key";
//    private static final String NEXT_NONCE = "next_nonce";  //compare easily
    private static final String LAST_OPEN_MS = "last_open_ms";  //上次登录的时间戳，只记录每天的第一次；the earliest one of one day
//    private static final String MAIN_RPC_URL = "main_rpc_url";
//    private static final String SIDE_RPC_URL = "side_rpc_url";


    static {
        sp = ACNAgent.getClient().getContext().getSharedPreferences(SP_NAME, Context.MODE_PRIVATE);
        editor = sp.edit();
    }

    public static void clear() {
        //appid， secretKey不清除
        editor.remove(KEY_USER_ID).remove(LAST_OPEN_MS).apply();
    }


    public static void setUserId(String userId) {
        editor.putString(KEY_USER_ID, userId).apply();
    }


    public static String getUserId() {
        String userId = sp.getString(KEY_USER_ID, "");
        SDKLogger.d("sp userId:" + userId);
        return userId;
    }

    public static void setDappId(String dappId) {
        editor.putString(KEY_DAPP_ID, dappId).apply();
    }

    public static String getDappId() {
        String appId = sp.getString(KEY_DAPP_ID, "");
        return appId;
    }

    public static void setDappSecretKey(String secretKey) {
        editor.putString(KEY_DAPP_SECRET_KEY, secretKey).apply();
    }

    public static String getDappSecretKey() {
        String secretKey = sp.getString(KEY_DAPP_SECRET_KEY, "");
        return secretKey;
    }

    //write the last nonce
//    public static void setNextNonce(BigInteger nonce) {
//        if (nonce != null) {
//            editor.putString(NEXT_NONCE, nonce.toString()).apply();
//        }
//    }
//
//    public static BigInteger getNextNonce() {
//        String sNonce = sp.getString(NEXT_NONCE, "");
//        if (!TextUtils.isEmpty(sNonce) && TextUtils.isDigitsOnly(sNonce)) {
//            return new BigInteger(sNonce);
//        }
//
//
//        return BigInteger.ZERO;
//
//    }

    /**
     * @param lastLogin mm
     */
    public static void setLastOpenTimestamp(long lastLogin) {
        editor.putLong(LAST_OPEN_MS, lastLogin).apply();
    }

    public static long getLastOpenTimestamp() {
        return sp.getLong(LAST_OPEN_MS, 0L);
    }

//    public static void setMainChainRpcUrl(String mainRpcUrl) {
//        editor.putString(MAIN_RPC_URL, mainRpcUrl);
//    }
//
//    public static String getMainChainRpcUrl() {
//        String defaultRpc;
//        if (BaseInfo.getInstance().isProd()) {
//            defaultRpc = Constants.MAIN_CHAIN_RPC_URL;
//        } else {
//            defaultRpc = Constants.TEST_MAIN_CHAIN_RPC_URL;
//        }
//        return sp.getString(MAIN_RPC_URL, defaultRpc);
//    }
//
//    public static void setSideChainRpcUrl(String sideRpcUrl) {
//        editor.putString(SIDE_RPC_URL, sideRpcUrl);
//    }
//
//    public static String getSideChainRpcUrl() {
//        String defaultRpc;
//        if (BaseInfo.getInstance().isProd()) {
//            defaultRpc = Constants.SIDE_CHAIN_RPC_URL;
//        } else {
//            defaultRpc = Constants.TEST_SIDE_CHAIN_RPC_URL;
//        }
//        return sp.getString(SIDE_RPC_URL, defaultRpc);
//    }
}
