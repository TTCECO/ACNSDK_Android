package com.ttc.behavior.command;

import android.content.Context;
import android.text.TextUtils;
import com.ttc.behavior.TTCAgent;
import com.ttc.behavior.db.TTCSp;
import com.ttc.behavior.model.TransactionResult;
import com.ttc.behavior.util.*;
import com.ttc.behavior.web.EthClient;
import com.ttc.biz.BizApi;
import com.ttc.biz.BizCallback;

/**
 * Created by lwq on 2018/11/20.
 */
public class BehaviorReq {

    public static void doIt(final int behaviorType, final String extra) {
        TTCAgent.getClient().getEventExecutorService().execute(new Runnable() {
            @Override
            public void run() {
                long timestamp = System.currentTimeMillis();
                String sExtra = extra;
                if (TextUtils.isEmpty(extra)) {
                    sExtra = "{}";
                }
                String hash = AlgorithmUtil.hash(behaviorType, TTCSp.getUserId(), timestamp, sExtra);
                String data = ActionHelper.toData(hash);
                TransactionResult result = reqBlock(data);

                if (result != null) {
                    TTCLogger.d("onEvent behaviorType=" + behaviorType + ", extra=" + sExtra + ", transaction hash = " +
                            result.getTransactionHash());

                    String actionHash = result.getTransactionHash();

                    if (!TextUtils.isEmpty(actionHash)) {
                        BizApi.behaviour(TTCAgent.getClient().getContext(), behaviorType, actionHash, sExtra,
                                timestamp, new BizCallback<String>() {
                                    @Override
                                    public void success(String s) {
                                        if (behaviorType == CommonType.OPEN_DAPP) {
                                            TTCSp.setLastOpenTimestamp(System.currentTimeMillis());
                                        }
                                    }

                                    @Override
                                    public void error(String msg) {

                                    }
                                });
                    }
                }
            }
        });
    }

    private static TransactionResult reqBlock(String data) {
        TransactionResult result = null;

        Context context = TTCAgent.getClient().getContext();
        result = EthClient.sendTransaction(Constants.ACTION_RPC_URL, BizApi.getBehaviourAddress(context), BizApi.getIndividualAddress(context),
                BizApi.getPrivateKey(context), BizApi.getGasPrice(context), BizApi.getGasLimit(context), data);
        return result;
    }

}
