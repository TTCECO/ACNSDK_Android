package com.ttc.behavior.command;

import android.content.Context;
import com.ttc.behavior.TTCAgent;
import com.ttc.behavior.command.base.AbstractCommand;
import com.ttc.behavior.model.EventBean;
import com.ttc.behavior.util.Constants;
import com.ttc.behavior.web.EthClient;
import com.ttc.biz.BizApi;

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
