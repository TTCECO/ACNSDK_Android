package com.ttc.sdk.command;

import com.ttc.biz.http.BizApi;
import com.ttc.sdk.TTCAgent;
import com.ttc.sdk.command.base.AbstractCommand;
import com.ttc.sdk.model.TransactionResult;
import com.ttc.sdk.util.ActionHelper;
import com.ttc.sdk.util.AlgorithmUtil;
import com.ttc.sdk.util.TTCLogger;
import com.ttc.sdk.util.TTCSp;


public class EventCommand extends AbstractCommand<TransactionResult> {

    private int behaviorType;
    private String extra;

    public EventCommand(int behaviorType, String extra) {
        this.behaviorType = behaviorType;
        this.extra = extra;
    }

    @Override
    public TransactionResult call() {
        long timestamp = System.currentTimeMillis();

        TTCLogger.e("onEvent behaviorType=" + behaviorType + ", extra=" + extra);

        String hash = AlgorithmUtil.hash(behaviorType, TTCSp.getUserId(TTCAgent.getClient().getContext()), timestamp, extra);
        TransactionResult result = transaction(hash);

        TTCLogger.d("transaction hash = " + result.getTransactionHash());

        if (result != null) {
            BizApi.behaviour(TTCAgent.getClient().getContext(), behaviorType, result.getTransactionHash(), extra, timestamp, null);
        }
        return result;
    }


    /**
     * 交易-写链
     */
    private TransactionResult transaction(String hash) {
        return new TransactionCommand(ActionHelper.toData(hash)).call();
    }

}
