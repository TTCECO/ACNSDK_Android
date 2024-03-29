package com.acn.behavior.web;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import com.acn.behavior.db.ACNSp;
import com.acn.behavior.db.BehaviorDBManager;
import com.acn.behavior.model.BehaviorModel;
import com.acn.behavior.util.ProcessUtil;
import com.acn.behavior.util.SDKLogger;
import com.acn.biz.model.BaseInfo;

import java.math.BigInteger;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

/**
 *
 */
public class Client {

    public static final String ACTION_RETRY = "com.acn.action.retry";

    private Context context;
    private Repo repo;
    private Handler handler;
    private ExecutorService eventExecutorService;
    private BehaviorDBManager dbManager;
    private ScheduledFuture<?> scheduledFuture;

    public Client(Context context) {
        this.context = context.getApplicationContext();
        handler = new Handler(Looper.getMainLooper());
        eventExecutorService = Executors.newFixedThreadPool(2);

        repo = new Repo();
        dbManager = new BehaviorDBManager(context);
        SDKLogger.d("create schedule thread");

        scheduledFuture = Executors.newScheduledThreadPool(1).scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                SDKLogger.d("schedule thread");
                try {
                    ACNSp.setNextNonce(EthClient.getNonce(BaseInfo.getInstance().getSideChainRPCUrl(), BaseInfo.getInstance().getDappActionAddress()));

                    List<BehaviorModel> allList = dbManager.getAll(BaseInfo.getInstance().getUserId());
                    SDKLogger.d("checking data size:" + allList.size());
                    for (BehaviorModel m : allList) {
                        if (m.writeBlockTimestamp == null) {
                            m.writeBlockTimestamp = "0";
                        }

                        if (!TextUtils.isEmpty(m.hash) && EthClient.isTransactionSuccess(BaseInfo.getInstance().getSideChainRPCUrl(), m.hash)) {
                            dbManager.delete(m.timestamp);
                            SDKLogger.d("delete. behaviorType=" + m.behaviorType);
                        } else {
                            if (EthClient.needRewriteBlock(BaseInfo.getInstance().getSideChainRPCUrl(), m.hash, m.blockNumber)) {
                                BigInteger tgasBalance = EthClient.getDappActionAddressBalance(BaseInfo.getInstance().getSideChainRPCUrl(), BaseInfo.getInstance().getDappActionAddress());
                                BigInteger min = new BigInteger(BaseInfo.getInstance().getGasPrice()).multiply(new BigInteger(String.valueOf(BaseInfo.getInstance().getGasLimit())));
                                if (tgasBalance.compareTo(min) > 0) {
                                    repo.onEvent(m.behaviorType, m.extra, Long.valueOf(m.timestamp));
                                    SDKLogger.d("write to block chain. behaviorType=" + m.behaviorType);
                                } else {
                                    SDKLogger.e("TGas is not enough");
                                }
                            }
                        }
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, 0, 10, TimeUnit.MINUTES);


        if (ProcessUtil.isMainProcess(context)) {
            registerReceiver();
        } else {
            SDKLogger.e("is not main process, don't register receiver!");
        }
    }

    public Context getContext() {
        return context;
    }

    public ExecutorService getEventExecutorService() {
        return eventExecutorService;
    }


    public Repo getRepo() {
        return repo;
    }

    public BehaviorDBManager getDbManager() {
        return dbManager;
    }

    private void registerReceiver() {
        IntentFilter filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        filter.addAction(ACTION_RETRY);
        getContext().registerReceiver(new ConnectivityChangeReceiver(), filter);
    }


    public void retry() {
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                context.sendBroadcast(new Intent(ACTION_RETRY));
            }
        }, 10 * 1000);
    }

    private class ConnectivityChangeReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = cm.getActiveNetworkInfo();
            boolean connected = networkInfo != null && networkInfo.isConnected();
            if (connected) {
                SDKLogger.d("network connected");
            }
        }
    }

//    private boolean isNeedUploadOpenBehavior() {
//        long lastOpenDay = ACNSp.getLastOpenTimestamp() / Constants.ONE_DAY_MILLISECOND;
//        long currentDay = System.currentTimeMillis() / Constants.ONE_DAY_MILLISECOND;
//        return currentDay > lastOpenDay;
//    }

    public ScheduledFuture<?> getScheduledFuture() {
        return scheduledFuture;
    }
}
