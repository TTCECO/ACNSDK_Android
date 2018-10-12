package com.ttc.sdk.command.base;

import com.ttc.sdk.web.Callback;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;

public interface Command<T> extends Callable<T> {

    void execute();

    void execute(Callback<T> callback);

    void execute(ExecutorService executorService);

}
