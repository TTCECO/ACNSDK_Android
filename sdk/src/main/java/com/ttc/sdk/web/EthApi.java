package com.ttc.sdk.web;

import com.ttc.sdk.util.Constants;
import com.ttc.sdk.model.TransactionResult;

import org.web3j.protocol.Web3j;
import org.web3j.protocol.Web3jFactory;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.methods.response.EthGetBalance;
import org.web3j.protocol.http.HttpService;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;

public class EthApi {

    public static BigDecimal balance(String address) {
        BigDecimal res = new BigDecimal("0");

        EthGetBalance send = null;
        try {
            Web3j web3 = Web3jFactory.build(new HttpService(Constants.TOKEN_RPC_URL));
            send = web3.ethGetBalance(address, DefaultBlockParameterName.LATEST).send();
            BigInteger balance = send.getBalance();  //Wei
            res = new BigDecimal(balance.divide(new BigInteger(Constants.ONE_QUINTILLION)).toString()); //ttc
        } catch (IOException e) {
            e.printStackTrace();
        }
        return res;

    }

    public static TransactionResult sendTransaction(String address, String toAddress, String pk, String gasPrice, int
            gasLimit, String data) {
        return EthClient.sendTransaction(Constants.ACTION_RPC_URL, address, toAddress, pk, gasPrice, gasLimit, data);
    }

    public static TransactionResult sendTransaction(String address, String toAddress, String pk, String gasPrice, int
            gasLimit, String data, BigInteger nonce) throws IOException {
        return EthClient.sendTransaction(Constants.ACTION_RPC_URL, address, toAddress, pk, gasPrice, gasLimit, data,
                nonce);
    }

}
