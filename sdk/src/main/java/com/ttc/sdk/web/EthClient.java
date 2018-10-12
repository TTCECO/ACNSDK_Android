package com.ttc.sdk.web;

import com.ttc.sdk.TTCAgent;
import com.ttc.sdk.model.TransactionResult;
import com.ttc.sdk.util.TTCLogger;
import com.ttc.sdk.util.TTCSp;

import org.web3j.crypto.Credentials;
import org.web3j.crypto.RawTransaction;
import org.web3j.crypto.TransactionEncoder;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.Web3jFactory;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.Response;
import org.web3j.protocol.core.methods.response.EthGetTransactionCount;
import org.web3j.protocol.core.methods.response.EthSendTransaction;
import org.web3j.protocol.core.methods.response.Transaction;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.protocol.http.HttpService;
import org.web3j.utils.Numeric;

import java.io.IOException;
import java.math.BigInteger;

/**
 * 发送交易，查询交易是否成功，内存中nonce的更新
 */
public class EthClient {

    private static Web3j web3j;

    private static Web3j web3(String rpcUrl) {
        if (web3j == null) {
            web3j = Web3jFactory.build(new HttpService(rpcUrl));
        }
        return web3j;
    }

    public static BigInteger getNonce(String rpcUrl, String from) throws IOException {
        Web3j web3 = web3(rpcUrl);
//        // get the next available nonce
        EthGetTransactionCount ethGetTransactionCount = web3.ethGetTransactionCount(from, DefaultBlockParameterName
                .LATEST).send();
        return ethGetTransactionCount.getTransactionCount();
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

        data = stringToHex(data);
        Web3j web3 = web3(rpcUrl);
        Credentials credentials = Credentials.create(fromPrivateKey);
        BigInteger nonce = null;
        BigInteger nextNonce = TTCSp.getNextNonce(TTCAgent.getClient().getContext());
        try {
            nonce = getNonce(rpcUrl, from);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

        if (nonce.compareTo(nextNonce) <= 0) {
            nonce = nextNonce;  //nonce从0开始
        }

        TTCLogger.d("send transaction, nonce=" + nonce);
        // create our transaction
        RawTransaction rawTransaction = RawTransaction.createTransaction(nonce, new BigInteger(gasPrice), new
                BigInteger(gasLimit + ""), to, data);

        // sign & send our transaction
        byte[] signedMessage = TransactionEncoder.signMessage(rawTransaction, credentials);
        String hexValue = Numeric.toHexString(signedMessage);
        EthSendTransaction ethSendTransaction = null;

        String transactionHash = null;
        try {
            ethSendTransaction = web3.ethSendRawTransaction(hexValue).send();

            transactionHash = ethSendTransaction.getTransactionHash();
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (transactionHash == null) {
            Response.Error error = ethSendTransaction.getError();
            if (error != null) {
                TTCLogger.e(error.getMessage());
            }
            return null;
        }
        TTCSp.setNextNonce(TTCAgent.getClient().getContext(), nonce.add(new BigInteger("1")));
        return new TransactionResult(transactionHash, nonce);
    }

    public static TransactionResult sendTransaction(String rpcUrl, String from, String to, String fromPrivateKey,
                                                    String gasPrice, int gasLimit, String data, BigInteger nonce)
            throws IOException {

        data = stringToHex(data);
        TTCLogger.e("data=" + data);
        Web3j web3 = web3(rpcUrl);
        Credentials credentials = Credentials.create(fromPrivateKey);
        TTCLogger.e("resend nonce=" + nonce + " thread name: " + Thread.currentThread().getName());

        // create our transaction
        RawTransaction rawTransaction = RawTransaction.createTransaction(nonce, new BigInteger(gasPrice), new
                BigInteger(gasLimit + ""), to, data);

        // sign & send our transaction
        byte[] signedMessage = TransactionEncoder.signMessage(rawTransaction, credentials);
        String hexValue = Numeric.toHexString(signedMessage);
        EthSendTransaction ethSendTransaction = web3.ethSendRawTransaction(hexValue).send();

        String transactionHash = ethSendTransaction.getTransactionHash();

        if (ethSendTransaction.hasError()) {
            TTCLogger.d("failed to send transaction");
        } else {
            TTCLogger.d("send transaction successfully");
        }

        if (transactionHash == null) {
            Response.Error error = ethSendTransaction.getError();
            if (error != null) {
                TTCLogger.e(error.getMessage());
            }
            return null;
        }
        TTCSp.setNextNonce(TTCAgent.getClient().getContext(), nonce.add(new BigInteger("1")));
        return new TransactionResult(transactionHash, nonce);
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

    public static boolean isTransactionSuccess(String rpcUrl, String hash) throws IOException {

        Web3j web3 = web3(rpcUrl);
        Transaction transaction = web3.ethGetTransactionByHash(hash).send().getTransaction();
        if (transaction != null) {
            String blockHash = transaction.getBlockHash();
            TTCLogger.e("blockHash=" + blockHash);
            String blockNumberRaw = transaction.getBlockNumberRaw();
            TTCLogger.e("blockNumberRaw=" + blockNumberRaw);
            TransactionReceipt transactionReceipt = web3.ethGetTransactionReceipt(hash).send().getTransactionReceipt();

            if (transactionReceipt != null) {
                TTCLogger.e("receipt" + transactionReceipt);
                String status = transactionReceipt.getStatus();
                System.out.println("status=" + status);

                try {
                    int statusInt = Integer.parseInt(status);
                    if (statusInt == 1) {
                        return true;
                    }
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                } catch (RuntimeException e) {
                    e.printStackTrace();
                }
            }
        }
        return false;
    }
}
