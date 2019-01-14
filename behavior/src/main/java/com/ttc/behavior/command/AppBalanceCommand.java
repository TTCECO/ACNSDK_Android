package com.ttc.behavior.command;

import com.ttc.behavior.TTCAgent;
import com.ttc.behavior.command.base.AbstractCommand;
import com.ttc.behavior.web.EthClient;
import com.ttc.biz.BizApi;

import java.math.BigDecimal;

public class AppBalanceCommand extends AbstractCommand<BigDecimal> {

    @Override
    public BigDecimal call()  {
        String address = BizApi.getBehaviourAddress(TTCAgent.getClient().getContext());

        return EthClient.getBalance(address);
    }

}
