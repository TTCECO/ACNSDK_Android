package com.ttc.sdk.command;

import android.content.Context;

import com.ttc.biz.http.BizApi;
import com.ttc.sdk.TTCAgent;
import com.ttc.sdk.command.base.AbstractCommand;
import com.ttc.sdk.model.EventBean;
import com.ttc.sdk.util.Constants;
import com.ttc.sdk.web.EthClient;

/**
 * Created by lwq on 2018/10/18.
 */
public class TransactionEventBeanCommand extends AbstractCommand<EventBean> {

    private EventBean bean;

    public TransactionEventBeanCommand(EventBean bean) {
        this.bean = bean;
    }

    @Override
    public EventBean call() throws Exception {

        Context context = TTCAgent.getClient().getContext();
        EthClient.sendTransaction(Constants.ACTION_RPC_URL, BizApi.getBehaviourAddress(context), BizApi.getIndividualAddress(context),
                BizApi.getPrivateKey(context), BizApi.getGasPrice(context), BizApi.getGasLimit(context), bean.getData());


        return bean;
    }
}
