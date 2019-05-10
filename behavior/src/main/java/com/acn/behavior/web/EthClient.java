package com.acn.behavior.web;

import android.text.TextUtils;
import com.acn.behavior.db.ACNSp;
import com.acn.behavior.util.Constants;
import com.acn.behavior.util.SDKLogger;
import com.acn.behavior.util.Utils;
import org.web3j.crypto.Credentials;
import org.web3j.crypto.RawTransaction;
import org.web3j.crypto.TransactionEncoder;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.methods.response.*;
import org.web3j.protocol.http.HttpService;
import org.web3j.utils.Numeric;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;

/**
 * 发送交易，查询交易是否成功，内存中nonce的更新
 */
public class EthClient {

    public static BigDecimal getBalance(String address) {
        BigDecimal res = null;
        Web3j web3 = null;
        if (TextUtils.isEmpty(address)) {
            return null;
        }

        String hexAddress = Utils.change2Hex(address);
        try {
            web3 = Web3j.build(new HttpService(ACNSp.getMainChainRpcUrl()));
            EthGetBalance send = web3.ethGetBalance(hexAddress, DefaultBlockParameterName.LATEST).send();
            BigInteger balance = send.getBalance();  //Wei
            res = new BigDecimal(balance.divide(new BigInteger(Constants.ONE_QUINTILLION)).toString()); //ttc
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (web3 != null) {
                web3.shutdown();
            }
        }
        return res;
    }


    public static BigInteger getNonce(String rpcUrl, String from) throws IOException {
        String hexFrom = Utils.change2Hex(from);
        try {
            Web3j web3 = Web3j.build(new HttpService(rpcUrl));
            EthGetTransactionCount ethGetTransactionCount = web3.ethGetTransactionCount(hexFrom,
                    DefaultBlockParameterName.LATEST).send();
            if (ethGetTransactionCount != null) {
                return ethGetTransactionCount.getTransactionCount();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return BigInteger.ZERO;
    }

    /**
     * 交易
     *
     * @param rpcUrl
     * @param from
     * @param to
     * @param fromPrivateKey
     * @return
     * @throws IOException
     */
    public static String sendTransaction(String rpcUrl, String from, String to, String fromPrivateKey,
                                                    String gasPrice, int gasLimit, String data) {

        if (TextUtils.isEmpty(fromPrivateKey)) {
            SDKLogger.e("send transaction: fromPrivateKey is empty");
            return null;
        }
        String hexFrom = Utils.change2Hex(from);
        String hexTo = Utils.change2Hex(to);
        String hexPrivateKey = Utils.change2Hex(fromPrivateKey);

        String dataHex = Utils.stringToHex(data);
        Web3j web3 = null;
        BigInteger nonce = null;
        BigInteger nextNonce = ACNSp.getNextNonce();
        Credentials credentials = null;
        try {
            credentials = Credentials.create(hexPrivateKey);
            web3 = Web3j.build(new HttpService(rpcUrl));
            nonce = getNonce(rpcUrl, hexFrom);
            if (nonce.compareTo(nextNonce) <= 0) {
                nonce = nextNonce;  //nonce从0开始
            }
            SDKLogger.d("send transaction, nonce=" + nonce);

            // create our transaction
            RawTransaction rawTransaction = RawTransaction.createTransaction(nonce, new BigInteger(gasPrice), new
                    BigInteger(gasLimit + ""), hexTo, dataHex);
            // sign & send our transaction
            byte[] signedMessage = TransactionEncoder.signMessage(rawTransaction, credentials);
            String hexValue = Numeric.toHexString(signedMessage);
            EthSendTransaction ethSendTransaction = web3.ethSendRawTransaction(hexValue).send();
            if (ethSendTransaction != null) {
                if (ethSendTransaction.hasError()) {
                    SDKLogger.e(ethSendTransaction.getError().getMessage());
                } else {
                    String transactionHash = ethSendTransaction.getTransactionHash();
                    ACNSp.setNextNonce(nonce.add(BigInteger.ONE));
                    return transactionHash;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static boolean isTransactionSuccess(String rpcUrl, String hash) {
        try {
            Web3j web3 = Web3j.build(new HttpService(rpcUrl));
            EthGetTransactionReceipt ethGetTransactionReceipt = web3.ethGetTransactionReceipt(hash).send();
            if (ethGetTransactionReceipt != null) {
                TransactionReceipt transactionReceipt = ethGetTransactionReceipt.getTransactionReceipt().get();
                if (transactionReceipt != null) {
                    if ("0x1".equals(transactionReceipt.getStatus())) {
                        return true;
                    }
                }
            }
        } catch (Exception e) {    //when testing, this exception occur
            e.printStackTrace();
        }

        return false;
    }
}
