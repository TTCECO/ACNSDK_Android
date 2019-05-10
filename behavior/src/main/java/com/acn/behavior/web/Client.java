package com.acn.behavior.web;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.os.Looper;
import com.acn.behavior.util.ProcessUtil;
import com.acn.behavior.util.SDKLogger;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 *
 */
public class Client {

    public static final String ACTION_RETRY = "com.acn.action.retry";

    private Context context;
    private Repo repo;
    private Handler handler;
    private ExecutorService eventExecutorService;

    public Client(Context context) {
        this.context = context.getApplicationContext();
        handler = new Handler(Looper.getMainLooper());
        eventExecutorService = Executors.newFixedThreadPool(5);
        repo = new Repo();

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
                SDKLogger.e("net connected");
            }
        }
    }

}
