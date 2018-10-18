package com.ttc.sdk.util;

import android.content.Context;
import android.text.TextUtils;

import com.ttc.biz.http.BizApi;
import com.ttc.sdk.TTCAgent;

public class ActionHelper {

    public static final int ACTION_SUCCESS = 1;

    /**
     *
     * @param hash
     * @return  the sample is : "ufo:1:oplog:md5:hash_value:appid"
     */
    public static String toData(String hash) {
        if (!TextUtils.isEmpty(hash)) {
            return Constants.DATA_PREFIX + hash + ":" + TTCSp.getAppId();
        } else {
            return Constants.DATA_PREFIX + TTCSp.getAppId();
        }
    }

    /**
     * 必须满足的条件
     *
     * @return
     */
    public static boolean precondition() {
        Context context = TTCAgent.getClient().getContext();
        if (TextUtils.isEmpty(TTCSp.getUserId())) {
            return false;
        }

        return !TextUtils.isEmpty(BizApi.getPrivateKey(context)) && !TextUtils.isEmpty(BizApi.getIndividualAddress
                (context));
    }
}
