package com.ttc.sdk.command.base;

import com.ttc.sdk.TTCAgent;
import com.ttc.sdk.web.Client;
import com.ttc.sdk.web.Dispatcher;
import com.ttc.sdk.web.Callback;

import java.util.concurrent.ExecutorService;


public abstract class AbstractCommand<T> implements Command<T> {

    private Dispatcher dispatcher;

    public AbstractCommand() {
        Client client = TTCAgent.getClient();
        dispatcher = client.getDispatcher();
    }

    @Override
    public void execute() {
        dispatcher.dispatch(this, null);
    }

    @Override
    public void execute(Callback<T> callback) {
        dispatcher.dispatch(this, callback);
    }

    @Override
    public void execute(ExecutorService executorService) {
        dispatcher.dispatch(executorService, this, null);
    }
}
