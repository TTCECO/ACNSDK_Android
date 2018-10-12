package com.ttc.sdk.web;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class DefaultExecutorService extends ThreadPoolExecutor {

    private static final int DEFAULT_THREAD_COUNT = 3;

    public DefaultExecutorService() {
        super(DEFAULT_THREAD_COUNT, DEFAULT_THREAD_COUNT, 5, TimeUnit.SECONDS, new LinkedBlockingQueue<Runnable>());
        allowCoreThreadTimeOut(true);
    }

}
