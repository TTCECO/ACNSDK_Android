package com.ttc.behavior.web;

import android.text.TextUtils;
import com.ttc.behavior.db.TTCSp;
import com.ttc.behavior.model.TransactionResult;
import com.ttc.behavior.util.Constants;
import com.ttc.behavior.util.TTCLogger;
import com.ttc.behavior.util.Utils;
import org.web3j.crypto.Credentials;
import org.web3j.crypto.RawTransaction;
import org.web3j.crypto.TransactionEncoder;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.Response;
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
        try {
            Web3j web3 = Web3j.build(new HttpService(Constants.SIDE_CHAIN_RPC_URL));
            EthGetBalance send = web3.ethGetBalance(Utils.change2Hex(address), DefaultBlockParameterName.LATEST).send();
            BigInteger balance = send.getBalance();  //Wei
            res = new BigDecimal(balance.divide(new BigInteger(Constants.ONE_QUINTILLION)).toString()); //ttc
        } catch (Exception e) {
            e.printStackTrace();
        }
        return res;
    }


    public static BigInteger getNonce(String rpcUrl, String from) throws IOException {
        try {
            Web3j web3 = Web3j.build(new HttpService(rpcUrl));
            EthGetTransactionCount ethGetTransactionCount = web3.ethGetTransactionCount(from,
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
    public static TransactionResult sendTransaction(String rpcUrl, String from, String to, String fromPrivateKey,
                                                    String gasPrice, int gasLimit, String data) {

        if (TextUtils.isEmpty(fromPrivateKey)) {
            TTCLogger.e("send transaction: fromPrivateKey is empty");
            return null;
        }

        String dataHex = stringToHex(data);
        Web3j web3 = null;
        BigInteger nonce = null;
        BigInteger nextNonce = TTCSp.getNextNonce();
        Credentials credentials = null;
        try {
            credentials = Credentials.create(fromPrivateKey);
            web3 = Web3j.build(new HttpService(rpcUrl));
            nonce = getNonce(rpcUrl, from);
            if (nonce.compareTo(nextNonce) <= 0) {
                nonce = nextNonce;  //nonce从0开始
            }
            TTCLogger.d("send transaction, nonce=" + nonce);

            // create our transaction
            RawTransaction rawTransaction = RawTransaction.createTransaction(nonce, new BigInteger(gasPrice), new
                    BigInteger(gasLimit + ""), to, dataHex);
            // sign & send our transaction
            byte[] signedMessage = TransactionEncoder.signMessage(rawTransaction, credentials);
            String hexValue = Numeric.toHexString(signedMessage);
            EthSendTransaction ethSendTransaction = web3.ethSendRawTransaction(hexValue).send();
            if (ethSendTransaction != null) {
                String transactionHash = ethSendTransaction.getTransactionHash();
                if (transactionHash == null) {
                    Response.Error error = ethSendTransaction.getError();
                    if (error != null) {
                        TTCLogger.e(error.getMessage());
                    }
                    return null;
                }
                TTCSp.setNextNonce(nonce.add(new BigInteger("1")));
                return new TransactionResult(transactionHash, nonce);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private static String stringToHex(String str) {
        StringBuilder sb = new StringBuilder();
        int length = str.length();
        for (int i = 0; i < length; i++) {
            char ch = str.charAt(i);
            sb.append(Integer.toHexString(ch));
        }
        return sb.toString();
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
