package com.ttc.behavior;


import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import com.ttc.behavior.db.TTCSp;
import com.ttc.behavior.util.Constants;
import com.ttc.behavior.util.TTCError;
import com.ttc.behavior.util.TTCLogger;
import com.ttc.behavior.util.Utils;
import com.ttc.behavior.web.Client;
import com.ttc.behavior.web.Repo;
import com.ttc.biz.BizApi;

import java.util.Map;

/**
 * 对外公开的类，主要的方法都在此类中
 */
public class TTCAgent {
    private static boolean SERVER_ENABLE = true;

    private static Client client = null;

    /**
     * init it in application.java
     *
     * @param context
     * @return
     */
    public static int init(Context context) {
        int errCode = 0;
        if (!SERVER_ENABLE) {
            errCode = TTCError.SDK_SERVER_OFF;
            TTCLogger.e(TTCError.getMessage(errCode));
            return errCode;
        }
        if (context == null) {
            errCode = TTCError.CONTEXT_IS_NULL;
            TTCLogger.e(TTCError.getMessage(errCode));
            return errCode;
        }

        String appId = Utils.getMeta(context, Constants.TTC_APP_ID);
        String secretKey = Utils.getMeta(context, Constants.TTC_APP_SECRET_KEY);
        if (TextUtils.isEmpty(appId)) {
            errCode = TTCError.APP_ID_IS_EMPTY;
            TTCLogger.e(TTCError.getMessage(errCode));
            return errCode;
        }
        if (TextUtils.isEmpty(secretKey)) {
            errCode = TTCError.SECRET_KEY_IS_EMPTY;
            TTCLogger.e(TTCError.getMessage(errCode));
            return errCode;
        }
        client = new Client(context);
        TTCSp.setAppId(appId);
        TTCSp.setSecretKey(secretKey);
        return errCode;
    }

    /**
     * when the user register in dapp, pls call it
     *
     * @param userId
     * @param callback
     */
    public static void register(String userId, IManager.UserInfoCallback callback) {
        int errCode = 0;
        if (!SERVER_ENABLE) {
            errCode = TTCError.SDK_SERVER_OFF;
            String errMsg = TTCError.getMessage(errCode);
            if (callback != null) {
                callback.error(errMsg);
            }
            TTCLogger.e(errMsg);
            return;
        }
        if (client == null) {
            errCode = TTCError.NOT_INITIAL;
            String errMsg = TTCError.getMessage(errCode);
            if (callback != null) {
                callback.error(errMsg);
            }
            TTCLogger.e(errMsg);
            return;
        }

        if (TextUtils.isEmpty(userId)) {
            errCode = TTCError.USER_ID_IS_EMPTY;
            String errMsg = TTCError.getMessage(errCode);
            if (callback != null) {
                callback.error(errMsg);
            }
            TTCLogger.e(errMsg);
            return;
        }

        String userIdSaved = TTCSp.getUserId();
        if (!TextUtils.isEmpty(userIdSaved) && !userIdSaved.equals(userId)) {
            TTCSp.clear();   //先清空，避免用户上次退出没有调用unregister
        }

        TTCSp.setUserId(userId);

        BizApi.init(TTCAgent.getClient().getContext(), TTCSp.getAppId(), TTCSp.getSecretKey(), TTCSp.getUserId(),
                BuildConfig.VERSION_CODE);

        repo().getPrivateKey();
        repo().registerUser(callback);
        client.retry();

        TTCLogger.d("userId:" + userId);
    }

    public static void unregister() {
        if (client != null) {
            BizApi.clear(TTCAgent.getClient().getContext());
            TTCSp.clear();
            TTCLogger.d("user unregister");
        }
    }

    /**
     * 常见的属性在UserAttr.java中已定义
     * Normal attributes are defined in UserAttr.java
     *
     * @param info
     * @param callback
     */
    public static void updateUserInfo(Map<String, String> info, IManager.UserInfoCallback callback) {
        String errMsg = TTCError.getMessage(checkServerClientUserId());
        if (!TextUtils.isEmpty(errMsg)) {
            if (callback != null) {
                callback.error(errMsg);
            }
            return;
        }
        repo().updateUser(info, callback);
    }

    /**
     * When the user  call it,  the dapp disconnect with wallet
     *
     * @param callback
     */
    public static void unbindApp(IManager.BindCallback callback) {
        String errMsg = TTCError.getMessage(checkServerClientUserId());
        if (!TextUtils.isEmpty(errMsg)) {
            if (callback != null) {
                callback.onMessage(false, errMsg);
            }
            return;
        }
        repo().unbindApp(callback);
    }

    /**
     * 获取绑定的钱包的余额
     * get the bound wallet's balance
     *
     * @param callback
     */
    public static void getWalletBalance(IManager.BalanceCallback callback) {
        String errMsg = TTCError.getMessage(checkServerClientUserId());
        if (!TextUtils.isEmpty(errMsg)) {
            if (callback != null) {
                callback.error(errMsg);
            }
            return;
        }

        if (TextUtils.isEmpty(BizApi.getBoundWalletAddress(client.getContext()))) {
            errMsg = TTCError.getMessage(TTCError.WALLET_ADDRESS_IS_EMPTY);
            if (callback != null) {
                callback.error(errMsg);
            }
            return;
        }

        repo().getWalletBalance(callback);
    }

    /**
     * 获取dapp的余额
     * get the dapp's balance
     *
     * @param callback
     */
    public static void getAppBalance(IManager.BalanceCallback callback) {
        String errMsg = TTCError.getMessage(checkServerClientUserId());
        if (!TextUtils.isEmpty(errMsg)) {
            if (callback != null) {
                callback.error(errMsg);
            }
            return;
        }
        repo().getAppBalance(callback);
    }

    /**
     * @param behaviorType 用户自定义，请传大于100的数；the num must be greater than 100, if u custom it
     * @param extra        json串; json string
     * @return
     */
    public static int onEvent(int behaviorType, String extra) {
        int errCode = checkServerClientUserId();
        if (errCode > 0) {
            return errCode;
        }

        TTCLogger.d("behaviorType:" + behaviorType + ", extra:" + extra);
        if (behaviorType < Constants.ACTION_TYPE_MIN_VALUE) {
            errCode = TTCError.BEHAVIOR_TYPE_IS_SMALLER;
            TTCLogger.e(TTCError.getMessage(errCode));
            return errCode;
        }
        repo().onEvent(behaviorType, extra);
        return errCode;
    }

    /**
     * 配置日志和sdk功能的开关
     * set the log and sdk func switch
     *
     * @param configure
     */
    public static void configure(TTCConfigure configure) {
        if (configure != null) {
            TTCLogger.ON = configure.logEnabled();
            SERVER_ENABLE = configure.serverEnabled();
        }
    }

    private static Repo repo() {
        return client.getRepo();
    }

    //make sure that client is not null
    private static int checkServerClientUserId() {
        int errCode = 0;
        String errMsg = "";
        if (!SERVER_ENABLE) {
            errCode = TTCError.SDK_SERVER_OFF;
            errMsg = TTCError.getMessage(errCode);
            TTCLogger.e(errMsg);
            return errCode;
        }
        if (client == null) {
            errCode = TTCError.NOT_INITIAL;
            errMsg = TTCError.getMessage(errCode);
            TTCLogger.e(errMsg);
            return errCode;
        }
        if (TextUtils.isEmpty(TTCSp.getUserId())) {
            errCode = TTCError.USER_ID_IS_EMPTY;
            errMsg = TTCError.getMessage(errCode);
            TTCLogger.e(errMsg);
            return errCode;
        }
        return errCode;
    }

    public static String getVersionName() {
        return BuildConfig.VERSION_NAME;
    }

    public static int getVersionCode() {
        return BuildConfig.VERSION_CODE;
    }

    public static Client getClient() {
        return client;
    }


    public static void setEnvProd(boolean isProd) {
        BizApi.setEnv(client.getContext(), isProd);
    }

    public static boolean isEnvProd() {
        return BizApi.isEnvProd(client.getContext());
    }
}
