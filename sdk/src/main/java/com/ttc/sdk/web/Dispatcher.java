package com.ttc.sdk.web;

import android.os.Handler;

import com.ttc.sdk.command.base.Command;

import java.util.concurrent.ExecutorService;

/**
 * 子线程
 */
public class Dispatcher {

    private Handler handler;

    private ExecutorService executorService;

    public Dispatcher(ExecutorService executorService, Handler handler) {

        this.handler = handler;

        this.executorService = executorService;

    }


    public <T> void dispatch(final Command<T> command, final Callback<T> callback) {
        dispatch(executorService, command, callback);
    }

    public <T> void dispatch(ExecutorService executorService, final Command<T> command, final Callback<T> callback) {
        executorService.execute(new Runnable() {
            @Override
            public void run() {

                try {
                    T t = command.call();
                    if (null != callback) {
                        handler.post(new InternalRunnable<>(new Result<>(t), callback));
                    }
                } catch (Throwable e) {
                    if (null != callback) {
                        handler.post(new InternalRunnable<>(new Result<T>(e), callback));
                    }
                    e.printStackTrace();
                }
            }
        });
    }


    private static class InternalRunnable<T> implements Runnable {

        private Result<T> result;

        private Callback<T> callback;

        public InternalRunnable(Result<T> result, Callback<T> callback) {
            this.result = result;
            this.callback = callback;
        }

        @Override
        public void run() {

            if (result.e == null) {
                callback.success(result.t);
            } else {
                callback.error(result.e);
            }

        }
    }

    static class Result<T> {
        private T t;

        private Throwable e;

        public Result(T result) {
            this.t = result;
        }

        public Result(Throwable e) {
            this.e = e;
        }
    }


}
