package com.acn.behavior.util;

import com.acn.biz.model.BaseInfo;

public class Constants {

    public static final String TTC_CONNECT_DOWNLOAD_URL = "https://wallet.ttc.eco/wallet/download/android";
    public static final String ACORN_BOX_DOWNLOAD_URL = "https://acn.eco/acornbox/download";

    public static final String SIGN_TYPE = "MD5";
    public static final String DATA_PREFIX = "ufo:1:oplog:md5:";

    public static final int MAX_RETRY_COUNT = 5;

    public static final String ACN_DAPP_ID = "ACN_DAPP_ID";
    public static final String ACN_DAPP_SECRET_KEY = "ACN_DAPP_SECRET_KEY";
    public static final String AD_MOB_APP_ID = "com.google.android.gms.ads.APPLICATION_ID";

    public static final String ONE_QUINTILLION = "1000000000000000000";
//    public static final int ACTION_TYPE_MIN_VALUE = 101;  //最小值

    public static final int BIND_STATE_UNBOUND = 0;  //未绑定
    public static final int BIND_STATE_BOUND = 1;    //绑定

    public static final long ONE_DAY_MILLISECOND = 86400000L;


    public static final int TYPE_SHOW = 1;
    public static final int TYPE_CLICK = 2;
    public static final int TYPE_VIDEO_OVER = 3;  //video play over


    public static final int OPERATE_BIND = 1;
    public static final int OPERATE_UNBIND = 2;


    public static String getDownloadUrl() {
        return getWallet(2);
    }

    public static String getBindScheme() {

        return getWallet(1);
    }

    public static String getPackageName() {

        return getWallet(0);
    }


    //0-package, 1-scheme, 2-downloadUrl
    private static String getWallet(int infoType) {
        String packageName = "";
        String scheme = "";
        String downloadUrl = "";
        switch (BaseInfo.getInstance().walletType) {
            case 0:
                packageName = "com.ttc.wallet";
                scheme = "ttc://bind";
                downloadUrl = TTC_CONNECT_DOWNLOAD_URL;
                break;

            case 1:
                packageName = "eco.acorn";
                scheme = "acorn://bind";
                downloadUrl = ACORN_BOX_DOWNLOAD_URL;
                break;
        }

        switch (infoType) {
            case 0:
                return packageName;

            case 1:
                return scheme;

            case 2:
                return downloadUrl;

            default:
                return "";
        }

    }

}
