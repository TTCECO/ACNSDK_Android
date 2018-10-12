package com.ttc.sdk.util;

import android.content.Context;
import android.text.TextUtils;

import com.ttc.biz.http.BizApi;
import com.ttc.sdk.TTCAgent;

public class ActionHelper {

    public static final int ACTION_SUCCESS = 1;

    public static String toData(String hash) {
        return Constants.DATA_PREFIX + hash;
    }

    /**
     * 必须满足的条件
     * @return
     */
    public static boolean precondition() {
        Context context = TTCAgent.getClient().getContext();
        if (TextUtils.isEmpty(TTCSp.getUserId(context))) {
            return false;
        }

        return !TextUtils.isEmpty(BizApi.getPrivateKey(context))
                && !TextUtils.isEmpty(BizApi.getIndividualAddress(context));
    }
}
