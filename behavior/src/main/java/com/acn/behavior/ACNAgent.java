package com.acn.behavior;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.text.TextUtils;
import com.acn.behavior.db.ACNSp;
import com.acn.behavior.util.*;
import com.acn.behavior.web.Client;
import com.acn.behavior.web.Repo;
import com.acn.biz.BizApi;
import com.acn.biz.model.BaseInfo;
import com.google.android.gms.ads.MobileAds;

import java.util.HashMap;
import java.util.Map;

/**
 * 对外公开的类，主要的方法都在此类中
 */
public class ACNAgent {
    private static boolean SERVER_ENABLE = true;

    private static Client client = null;
    private static BindReceiver bindReceiver = null;
    private static String appId = "";
    private static String secretKey = "";
//    private static String adMobAppId = "";

    /***
     * init it in application.java
     *
     * @param context
     * @return
     */
    public static int init(Context context) {
        int errCode = 0;
        if (!SERVER_ENABLE) {
            errCode = SDKError.SDK_SERVER_OFF;
            SDKLogger.e(SDKError.getMessage(errCode));
            return errCode;
        }
        if (context == null) {
            errCode = SDKError.CONTEXT_IS_NULL;
            SDKLogger.e(SDKError.getMessage(errCode));
            return errCode;
        }

        appId = Utils.getMeta(context, Constants.ACN_DAPP_ID);
        secretKey = Utils.getMeta(context, Constants.ACN_DAPP_SECRET_KEY);
        String adMobAppId = Utils.getMeta(context, Constants.AD_MOB_APP_ID);

        if (TextUtils.isEmpty(appId)) {
            errCode = SDKError.APP_ID_IS_EMPTY;
            SDKLogger.e(SDKError.getMessage(errCode));
            return errCode;
        }
        if (TextUtils.isEmpty(secretKey)) {
            errCode = SDKError.SECRET_KEY_IS_EMPTY;
            SDKLogger.e(SDKError.getMessage(errCode));
            return errCode;
        }

        //init only once
        if (!TextUtils.isEmpty(adMobAppId)) {
            MobileAds.initialize(context, adMobAppId);
        }

        client = new Client(context);

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
            errCode = SDKError.SDK_SERVER_OFF;
            String errMsg = SDKError.getMessage(errCode);
            if (callback != null) {
                callback.error(errMsg);
            }
            SDKLogger.e(errMsg);
            return;
        }
        if (client == null) {
            errCode = SDKError.NOT_INITIAL;
            String errMsg = SDKError.getMessage(errCode);
            if (callback != null) {
                callback.error(errMsg);
            }
            SDKLogger.e(errMsg);
            return;
        }

        if (TextUtils.isEmpty(userId)) {
            errCode = SDKError.USER_ID_IS_EMPTY;
            String errMsg = SDKError.getMessage(errCode);
            if (callback != null) {
                callback.error(errMsg);
            }
            SDKLogger.e(errMsg);
            return;
        }

        String userIdSaved = BaseInfo.getInstance().getUserId();
        if (!TextUtils.isEmpty(userIdSaved) && !userIdSaved.equals(userId)) {
            ACNSp.clear();   //先清空，避免用户上次退出没有调用unregister
            BaseInfo.getInstance().clear();
        }


        BizApi.getInstance().init(appId, secretKey, userId, BuildConfig.VERSION_CODE);

        //upload device Id
        Map<String, String> info = new HashMap<>();
        info.put(UserAttr.CLIENT_ID, Utils.getClientId());
        info.put(UserAttr.COUNTRY_CODE, Utils.getLocationCode(ACNAgent.getClient().getContext()));
        BizApi.getInstance().updateUser(info, null);

        repo().registerUser(callback);
        client.retry();

        bindReceiver = new BindReceiver();
        IntentFilter filter = new IntentFilter("acn.bind.receiver");
        filter.addCategory(Intent.CATEGORY_DEFAULT);
        client.getContext().registerReceiver(bindReceiver, filter);

        SDKLogger.d("userId:" + userId);
    }

    public static void unregister() {
        if (client != null) {
            ACNSp.clear();
            BaseInfo.getInstance().clear();
            if (bindReceiver != null) {
                client.getContext().unregisterReceiver(bindReceiver);
                bindReceiver = null;
            }
            SDKLogger.d("user unregister");
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
        String errMsg = SDKError.getMessage(checkServerClientUserId());
        if (!TextUtils.isEmpty(errMsg)) {
            if (callback != null) {
                callback.error(errMsg);
            }
            return;
        }
        repo().updateUser(info, callback);
    }

    //user can get result on OnActivityResult()
    public static void bindApp(Activity activity, String appIconUrl, int reqCode) {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("ttc://bind"));
        intent.putExtra(ACNKey.OPERATE_TYPE, Constants.OPERATE_BIND);
        intent.putExtra(ACNKey.USER_ID, BaseInfo.getInstance().getUserId());
        intent.putExtra(ACNKey.APP_ID, BaseInfo.getInstance().getAppId());
        intent.putExtra(ACNKey.APP_ICON_URL, appIconUrl);
        intent.putExtra(ACNKey.APP_NAME, Utils.getApplicationName(client.getContext()));
        activity.startActivityForResult(intent, reqCode);
    }

    /**
     * When the user  call it,  the dapp disconnect with wallet
     *
     * @param callback
     */
    public static void unbindApp(IManager.UnbindCallback callback) {
        String errMsg = SDKError.getMessage(checkServerClientUserId());
        if (!TextUtils.isEmpty(errMsg)) {
            if (callback != null) {
                callback.error(errMsg);
            }
            return;
        }
        repo().unbindApp(callback);
    }

    /**
     * 获取绑定的钱包的余额
     * get the bound wallet's balance
     * acn balance
     *
     * @param callback
     */
    public static void getWalletBalance(IManager.BalanceCallback callback) {
        String errMsg = SDKError.getMessage(checkServerClientUserId());
        if (!TextUtils.isEmpty(errMsg)) {
            if (callback != null) {
                callback.error(errMsg);
            }
            return;
        }

        if (TextUtils.isEmpty(BaseInfo.getInstance().getWalletAddress())) {
            errMsg = SDKError.getMessage(SDKError.WALLET_ADDRESS_IS_EMPTY);
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
        String errMsg = SDKError.getMessage(checkServerClientUserId());
        if (!TextUtils.isEmpty(errMsg)) {
            if (callback != null) {
                callback.error(errMsg);
            }
            return;
        }
        repo().getAppBalance(callback);
    }

    //返回绑定的钱包地址
    public static String getBoundWalletAddress() {
        return BaseInfo.getInstance().getWalletAddress();
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

        SDKLogger.d("behaviorType:" + behaviorType + ", extra:" + extra);
        if (behaviorType < Constants.ACTION_TYPE_MIN_VALUE) {
            errCode = SDKError.BEHAVIOR_TYPE_IS_SMALLER;
            SDKLogger.e(SDKError.getMessage(errCode));
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
    public static void configure(ACNConfigure configure) {
        if (configure != null) {
            SDKLogger.ON = configure.logEnabled();
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
            errCode = SDKError.SDK_SERVER_OFF;
            errMsg = SDKError.getMessage(errCode);
            SDKLogger.e(errMsg);
            return errCode;
        }
        if (client == null) {
            errCode = SDKError.NOT_INITIAL;
            errMsg = SDKError.getMessage(errCode);
            SDKLogger.e(errMsg);
            return errCode;
        }
        if (TextUtils.isEmpty(BaseInfo.getInstance().getUserId())) {
            errCode = SDKError.USER_ID_IS_EMPTY;
            errMsg = SDKError.getMessage(errCode);
            SDKLogger.e(errMsg);
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
        BaseInfo.getInstance().setProd(isProd);
    }

    public static boolean isEnvProd() {
        return BaseInfo.getInstance().isProd();
    }
}
