package com.ttc.behavior.command;

import android.text.TextUtils;
import com.ttc.behavior.TTCAgent;
import com.ttc.behavior.command.base.AbstractCommand;
import com.ttc.behavior.web.EthClient;
import com.ttc.biz.BizApi;

import java.math.BigDecimal;

public class WalletBalanceCommand extends AbstractCommand<BigDecimal> {


    @Override
    public BigDecimal call() {

        String wallet = BizApi.getIndividualAddress(TTCAgent.getClient().getContext());
        if (TextUtils.isEmpty(wallet)) {
            return new BigDecimal("0");
        }
        return EthClient.getBalance(wallet);
    }
}
