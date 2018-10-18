package com.ttc.sdk.command;

import android.text.TextUtils;

import com.ttc.biz.http.BizApi;
import com.ttc.sdk.TTCAgent;
import com.ttc.sdk.command.base.AbstractCommand;
import com.ttc.sdk.web.EthClient;

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
