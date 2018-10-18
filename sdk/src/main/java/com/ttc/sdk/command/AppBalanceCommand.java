package com.ttc.sdk.command;

import com.ttc.biz.http.BizApi;
import com.ttc.sdk.TTCAgent;
import com.ttc.sdk.command.base.AbstractCommand;
import com.ttc.sdk.web.EthClient;

import java.math.BigDecimal;

public class AppBalanceCommand extends AbstractCommand<BigDecimal> {

    @Override
    public BigDecimal call()  {
        String address = BizApi.getBehaviourAddress(TTCAgent.getClient().getContext());

        return EthClient.getBalance(address);
    }

}
