package com.ttc.sdk.command;

import android.content.Context;

import com.ttc.biz.http.BizApi;
import com.ttc.sdk.TTCAgent;
import com.ttc.sdk.command.base.AbstractCommand;
import com.ttc.sdk.model.TransactionResult;
import com.ttc.sdk.util.Constants;
import com.ttc.sdk.web.EthClient;

public class TransactionCommand extends AbstractCommand<TransactionResult> {

    private String data;

    public TransactionCommand(String data) {
        this.data = data;
    }

    @Override
    public TransactionResult call() {

        TransactionResult result = null;

        Context context = TTCAgent.getClient().getContext();
        result = EthClient.sendTransaction(Constants.ACTION_RPC_URL, BizApi.getBehaviourAddress(context), BizApi.getIndividualAddress(context),
                BizApi.getPrivateKey(context), BizApi.getGasPrice(context), BizApi.getGasLimit(context), data);
        return result;
    }

}
