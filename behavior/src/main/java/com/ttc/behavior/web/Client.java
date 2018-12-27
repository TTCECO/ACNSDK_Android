package com.ttc.behavior.web;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.os.Looper;

import com.ttc.behavior.db.EventDao;
import com.ttc.behavior.util.ProcessUtil;
import com.ttc.behavior.util.TTCLogger;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 *
 */
public class Client {

    public static final String ACTION_RETRY = "com.ttc.action.retry";

    private Context context;
    private Dispatcher dispatcher;
    private Repo repo;
    private HttpStack httpStack;
    private Handler handler;
    private ExecutorService eventExecutorService;

    public Client(Context context) {
        this.context = context.getApplicationContext();
        handler = new Handler(Looper.getMainLooper());
        dispatcher = new Dispatcher(new DefaultExecutorService(), handler);
        httpStack = new HurlStack();
        eventExecutorService = Executors.newCachedThreadPool();
        repo = new Repo();
        EventDao.init(context);

        if (ProcessUtil.isMainProcess(context)) {
            registerReceiver();
        } else {
            TTCLogger.e("is not main process, don't register receiver!");
        }
    }

    public Context getContext() {
        return context;
    }

    public ExecutorService getEventExecutorService() {
        return eventExecutorService;
    }

    public Dispatcher getDispatcher() {
        return dispatcher;
    }

    public Repo getRepo() {
        return repo;
    }

    public HttpStack getHttpStack() {
        return httpStack;
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
                TTCLogger.e("net connected");
            }
        }
    }

}
