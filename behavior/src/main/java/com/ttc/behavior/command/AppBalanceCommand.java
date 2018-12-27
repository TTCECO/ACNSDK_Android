package com.ttc.behavior.command;

import com.ttc.biz.http.BizApi;
import com.ttc.behavior.TTCAgent;
import com.ttc.behavior.command.base.AbstractCommand;
import com.ttc.behavior.web.EthClient;

import java.math.BigDecimal;

public class AppBalanceCommand extends AbstractCommand<BigDecimal> {

    @Override
    public BigDecimal call()  {
        String address = BizApi.getBehaviourAddress(TTCAgent.getClient().getContext());

        return EthClient.getBalance(address);
    }

}
