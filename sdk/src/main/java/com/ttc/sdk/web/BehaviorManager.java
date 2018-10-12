package com.ttc.sdk.web;


import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;

import com.ttc.sdk.util.ActionHelper;
import com.ttc.sdk.util.TTCLogger;

import java.math.BigInteger;


public class BehaviorManager {
    public static final int WHAT_TRANSACTION = 1;
    public static final int INTERVAL = 60 * 1000;
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

        BigInteger reservedNonce;
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
                    TTCLogger.d("begin check transaction thread name: " + Thread.currentThread().getName());
                        checkTransaction();
                    break;
            }
        }
    }

}
