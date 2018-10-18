package com.ttc.sdk.web;


import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;

import com.ttc.biz.http.BizApi;
import com.ttc.biz.http.BizCallback;
import com.ttc.sdk.TTCAgent;
import com.ttc.sdk.command.TransactionEventBeanCommand;
import com.ttc.sdk.db.EventDao;
import com.ttc.sdk.model.EventBean;
import com.ttc.sdk.util.ActionHelper;
import com.ttc.sdk.util.CommonType;
import com.ttc.sdk.util.Constants;
import com.ttc.sdk.util.TTCSp;

import java.util.List;

/**
 * 延时检查交易是否成功，如果没有则增大gas，再次发送
 */
public class BehaviorManager {
    private static final int WHAT_TRANSACTION = 1;
    private static final int INTERVAL = 10 * 1000;  // 10s
    private Handler handler;

    private static BehaviorManager sInstance = new BehaviorManager();

    public static BehaviorManager getInstance() {
        return sInstance;
    }

    private BehaviorManager() {
        HandlerThread handlerThread = new HandlerThread("behavior_handler_thread");
        handlerThread.start();
        handler = new EventHandler(handlerThread.getLooper());
    }

    public void delayCheckTransaction() {
        handler.removeMessages(WHAT_TRANSACTION);
        Message message = handler.obtainMessage(WHAT_TRANSACTION);
        handler.sendMessageDelayed(message, INTERVAL);
    }

    private void checkTransaction() {
        if (!ActionHelper.precondition()) {
            return;
        }

        List<EventBean> beans = EventDao.fetchEvents();
        if (beans != null) {
            for (EventBean bean : beans) {
                if (bean.getBcRetryCount() > 0) {
                    if (bean.getBcRetryCount() >= Constants.MAX_RETRY_COUNT) {
                        EventDao.deleteEvent(bean.getActionHash());
                        continue;
                    }
                    if (EthClient.isTransactionSuccess(Constants.ACTION_RPC_URL, bean.getActionHash())) {
                        bean.setBcRetryCount(0);
                        EventDao.update(bean);
                        uploadBizBehavior(bean);
                    } else {
                      new TransactionEventBeanCommand(bean).execute();

                      handler.removeMessages(WHAT_TRANSACTION);
                      handler.sendEmptyMessageDelayed(WHAT_TRANSACTION, INTERVAL);
                    }
                    continue;
                }
                if (bean.getBizRetryCount() > 0) {
                    if (bean.getBizRetryCount() >= Constants.MAX_RETRY_COUNT) {
                        EventDao.deleteEvent(bean.getActionHash());
                        continue;
                    }
                    uploadBizBehavior(bean);
                    continue;
                }

                EventDao.deleteEvent(bean.getActionHash());
            }
        }
    }

    private void uploadBizBehavior(final EventBean bean) {
        if (bean == null) {
            return;
        }

        BizApi.behaviour(TTCAgent.getClient().getContext(), bean.getBehaviorType(), bean.getActionHash(), bean
                .getExtra(), bean.getTimestamp(), new BizCallback<String>() {


            @Override
            public void success(String actionHash) {
                if (bean.getBehaviorType() == CommonType.OPEN_DAPP) {
                    TTCSp.setLastOpenTimestamp(System.currentTimeMillis());
                }

                EventDao.deleteEvent(bean.getActionHash());
            }

            @Override
            public void error(String msg) {
                bean.setBizRetryCount(bean.getBizRetryCount() + 1);
                EventDao.update(bean);
                handler.removeMessages(WHAT_TRANSACTION);
                handler.sendEmptyMessage(WHAT_TRANSACTION);
            }
        });
    }


    public void execute(Runnable command) {
        handler.post(command);
    }

    private class EventHandler extends Handler {
        EventHandler(Looper looper) {
            super(looper);
        }

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case WHAT_TRANSACTION:
                    checkTransaction();
                    break;

            }
        }
    }

}
