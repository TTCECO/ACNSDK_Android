package com.ttc.sdk.command;

import android.text.TextUtils;

import com.ttc.sdk.command.base.AbstractCommand;
import com.ttc.sdk.db.EventDao;
import com.ttc.sdk.model.EventBean;
import com.ttc.sdk.model.TransactionResult;
import com.ttc.sdk.util.ActionHelper;
import com.ttc.sdk.util.AlgorithmUtil;
import com.ttc.sdk.util.TTCLogger;
import com.ttc.sdk.util.TTCSp;
import com.ttc.sdk.web.BehaviorManager;


public class EventCommand extends AbstractCommand<TransactionResult> {

    private int behaviorType;
    private String extra;

    public EventCommand(int behaviorType, String extra) {
        this.behaviorType = behaviorType;
        if (TextUtils.isEmpty(extra)) {
            this.extra = "{}";
        }else {
            this.extra = extra;
        }
    }

    @Override
    public TransactionResult call() {
        long timestamp = System.currentTimeMillis();
        String hash = AlgorithmUtil.hash(behaviorType, TTCSp.getUserId(), timestamp, extra);
        String data = ActionHelper.toData(hash);
        TransactionCommand transactionCommand = new TransactionCommand(data);
        TransactionResult result = transactionCommand.call();
        TTCLogger.d("onEvent behaviorType=" + behaviorType + ", extra=" + extra + ", transaction hash = " + result
                .getTransactionHash());

        if (result != null) {
            String actionHash = result.getTransactionHash();
            EventBean bean = EventDao.fetchEventByUserActionHash(actionHash);
            if (bean != null) {
                bean.setBcRetryCount(bean.getBcRetryCount() + 1);
                EventDao.update(bean);
            }else {
                EventDao.insert(behaviorType, actionHash, extra, timestamp);
            }
        }
        BehaviorManager.getInstance().delayCheckTransaction();
        return result;
    }

}
