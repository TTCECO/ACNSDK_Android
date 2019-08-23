package com.acn.behavior.web;

import android.text.TextUtils;

import com.acn.behavior.db.ACNSp;
import com.acn.behavior.util.Constants;
import com.acn.behavior.util.SDKLogger;
import com.acn.behavior.util.Utils;
import com.acn.biz.model.BaseInfo;

import org.web3j.abi.FunctionEncoder;
import org.web3j.abi.FunctionReturnDecoder;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.Address;
import org.web3j.abi.datatypes.Function;
import org.web3j.abi.datatypes.Type;
import org.web3j.abi.datatypes.generated.Uint256;
import org.web3j.crypto.Credentials;
import org.web3j.crypto.RawTransaction;
import org.web3j.crypto.TransactionEncoder;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.methods.request.Transaction;
import org.web3j.protocol.core.methods.response.EthCall;
import org.web3j.protocol.core.methods.response.EthGetBalance;
import org.web3j.protocol.core.methods.response.EthGetTransactionCount;
import org.web3j.protocol.core.methods.response.EthGetTransactionReceipt;
import org.web3j.protocol.core.methods.response.EthSendTransaction;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.protocol.http.HttpService;
import org.web3j.utils.Numeric;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import java8.util.Optional;

/**
 * 发送交易，查询交易是否成功，内存中nonce的更新
 */
public class EthClient {

    public static BigDecimal getBalance(String rpcUrl, String address) {
        BigDecimal res = null;
        Web3j web3 = null;
        if (TextUtils.isEmpty(address)) {
            return null;
        }

        try {
            String hexAddress = Utils.format2ETHAddress(address);

            web3 = Web3j.build(new HttpService(rpcUrl));
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

    //token
    public static BigDecimal getTokenBalance(String fromAddress, String contractAddress) throws Exception {

        String methodName = "balanceOf";
        List<Type> inputs = new ArrayList<>();
        inputs.add(new Address(Utils.format2ETHAddress(fromAddress)));
        List<TypeReference<?>> outputs = new ArrayList<>();
        TypeReference<?> typeReference = new TypeReference<Uint256>() {
        };
        outputs.add(typeReference);

        BigDecimal res = null;
        String errMsg = "";
        try {
            Web3j web3j = Web3j.build(new HttpService(BaseInfo.getInstance().getMainChainRPCUrl()));
            Function function = new Function(methodName, inputs, outputs);
            String data = FunctionEncoder.encode(function);
            org.web3j.protocol.core.methods.request.Transaction transaction = Transaction.createEthCallTransaction(Utils.format2ETHAddress(fromAddress),
                    Utils.format2ETHAddress(contractAddress), data);

            EthCall ethCall = web3j.ethCall(transaction, DefaultBlockParameterName.LATEST).sendAsync().get();
            if (ethCall.hasError()) {
                errMsg = ethCall.getError().getMessage();
            } else {
                List<Type> results = FunctionReturnDecoder.decode(ethCall.getValue(),
                        function.getOutputParameters());

                if (results != null && results.size() > 0) {
                    BigInteger balance = (BigInteger) results.get(0).getValue();
                    res = new BigDecimal(balance).divide(new BigDecimal(Constants.ONE_QUINTILLION));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            errMsg = e.getMessage();
            throw e;
        }

        return res;
    }


    public static BigDecimal getDappActionAddressBalance(String rpcUrl, String address) throws Exception {
        BigDecimal res = null;
        Web3j web3 = null;


        String hexAddress = Utils.format2ETHAddress(address);

        web3 = Web3j.build(new HttpService(rpcUrl));
        EthGetBalance send = web3.ethGetBalance(hexAddress, DefaultBlockParameterName.LATEST).send();
        BigInteger balance = send.getBalance();  //Wei
        res = new BigDecimal(balance.divide(new BigInteger(Constants.ONE_QUINTILLION)).toString()); //ttc

        return res;
    }


    //查询代币冻结余额
    public static BigDecimal getFrozenAvailableAmount(String fromAddress, String tokenAddress, String contractAddress) throws Exception {
        String methodName = "getAccountBalance";
        String errMsg = "";
        try {
            Web3j web3j = Web3j.build(new HttpService(BaseInfo.getInstance().getMainChainRPCUrl()));

            ArrayList<Type> inputParameters = new ArrayList<>();
            ArrayList<TypeReference<?>> outputParameters = new ArrayList<>();
            inputParameters.add(new Address(Utils.format2ETHAddress(tokenAddress)));
            inputParameters.add(new Address(Utils.format2ETHAddress(fromAddress)));

            TypeReference<Uint256> frozenType = new TypeReference<Uint256>() {
            };
            TypeReference<Uint256> balanceType = new TypeReference<Uint256>() {
            };
            outputParameters.add(frozenType);
            outputParameters.add(balanceType);

            Function function = new Function(methodName, inputParameters, outputParameters);
            String data = FunctionEncoder.encode(function);
            Transaction transaction = Transaction.createEthCallTransaction(Utils.format2ETHAddress(fromAddress),
                    contractAddress, data);

            EthCall ethCall = web3j.ethCall(transaction, DefaultBlockParameterName.LATEST).sendAsync().get();
            if (ethCall.hasError()) {
                errMsg = ethCall.getError().getMessage();
            } else {
                List<Type> results = FunctionReturnDecoder.decode(ethCall.getValue(),
                        function.getOutputParameters());

                if (results.size() >= 2) {
                    BigInteger[] balance = new BigInteger[2];
                    balance[0] = (BigInteger) results.get(0).getValue();
                    balance[1] = (BigInteger) results.get(1).getValue();
                    BigDecimal res = new BigDecimal(balance[0].add(balance[1])).divide(new BigDecimal(Constants.ONE_QUINTILLION));
                    return res;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
//                errMsg = e.getMessage();
//                LogUtils.INSTANCE.e(errMsg);
        }
        return null;

//            if (handler == null) {
//                return;
//            }
//
//            Message msg = handler.obtainMessage();
//            msg.arg1 = tokenId;
//            if (balance != null) {
//                msg.what = GET_FROZEN_AVAILABLE_AMOUNT_SUC;
//                msg.obj = balance;
//            } else {
//                msg.what = GET_FROZEN_AVAILABLE_AMOUNT_FAIL;
//                msg.obj = errMsg;
//                LogUtils.INSTANCE.e("getFrozenAvailable error:" + errMsg);
//            }
//            handler.sendMessage(msg);

    }


    public static BigInteger getNonce(String rpcUrl, String from) throws IOException {
        try {
            String hexFrom = Utils.format2ETHAddress(from);

            Web3j web3 = Web3j.build(new HttpService(rpcUrl));
            EthGetTransactionCount ethGetTransactionCount = web3.ethGetTransactionCount(hexFrom,
                    DefaultBlockParameterName.LATEST).send();
            BigInteger count = ethGetTransactionCount.getTransactionCount();
            SDKLogger.d("getTransactionCount:" + count);
            return count;
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
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
                                         String gasPrice, int gasLimit, String data) throws Exception {


        if (TextUtils.isEmpty(fromPrivateKey)) {
            SDKLogger.e("send transaction: fromPrivateKey is empty");
            return null;
        }

        try {

            String hexFrom = Utils.format2ETHAddress(from);
            String hexTo = Utils.format2ETHAddress(to);
            String hexPrivateKey = Utils.format2ETHAddress(fromPrivateKey);

            String dataHex = Utils.stringToHex(data);
            Web3j web3 = null;
            BigInteger nonce = null;
            BigInteger nextNonce = ACNSp.getNextNonce();
            Credentials credentials = null;

            credentials = Credentials.create(hexPrivateKey);
            web3 = Web3j.build(new HttpService(rpcUrl));
            nonce = getNonce(rpcUrl, hexFrom);
            if (nonce.compareTo(nextNonce) <= 0) {
                nonce = nextNonce;  //nonce从0开始
            }

            // create our transaction
            RawTransaction rawTransaction = RawTransaction.createTransaction(nonce, new BigInteger(gasPrice), new
                    BigInteger(gasLimit + ""), hexTo, dataHex);
            // sign & send our transaction
            byte[] signedMessage = TransactionEncoder.signMessage(rawTransaction, credentials);
            String hexValue = Numeric.toHexString(signedMessage);
            EthSendTransaction ethSendTransaction = web3.ethSendRawTransaction(hexValue).send();
            if (ethSendTransaction != null) {
                if (ethSendTransaction.hasError()) {
                    String errMsg = ethSendTransaction.getError().getMessage();
                    SDKLogger.e(errMsg);
                } else {
                    ACNSp.setNextNonce(nonce.add(BigInteger.ONE));
                    String transactionHash = ethSendTransaction.getTransactionHash();
                    SDKLogger.d("send transaction, nonce=" + nonce + ",hash=" + transactionHash);

                    return transactionHash;
                }
            }
        } catch (Exception e) {
            SDKLogger.e(e.getMessage());
            throw e;
        }
        return null;
    }

    public static boolean isTransactionSuccess(String rpcUrl, String hash) throws Exception {
        try {
            String hexHash = Utils.format2ETHAddress(hash);
            Web3j web3 = Web3j.build(new HttpService(rpcUrl));
            EthGetTransactionReceipt ethGetTransactionReceipt = web3.ethGetTransactionReceipt(hexHash).send();
            if (ethGetTransactionReceipt != null) {
                Optional<TransactionReceipt> receiptOptional = ethGetTransactionReceipt.getTransactionReceipt();
                if (!receiptOptional.isEmpty()) {
                    TransactionReceipt transactionReceipt = receiptOptional.get();
                    if (transactionReceipt != null) {
                        if ("0x1".equals(transactionReceipt.getStatus())) {
                            return true;
                        }
                    }
                }
            }
        } catch (Exception e) {    //when testing, this exception occur
            e.printStackTrace();
            throw e;
        }

        return false;
    }

    public static boolean isTransactionFailed(String rpcUrl, String hash) throws Exception {

        if (TextUtils.isEmpty(hash)) {
            return true;
        }

        String hexHash = Utils.format2ETHAddress(hash);
        Web3j web3 = Web3j.build(new HttpService(rpcUrl));
        EthGetTransactionReceipt ethGetTransactionReceipt = web3.ethGetTransactionReceipt(hexHash).send();
        if (ethGetTransactionReceipt == null) {
            return true;
        }

        Optional<TransactionReceipt> receiptOptional = ethGetTransactionReceipt.getTransactionReceipt();
        if (receiptOptional.isEmpty()) {
            return true;

        }

        TransactionReceipt transactionReceipt = receiptOptional.get();
        if (transactionReceipt == null) {
            return true;
        }

        if ("0x0".equals(transactionReceipt.getStatus())) {
            return true;
        }

        return false;
    }

    public static boolean needRewriteBlock(String rpcUrl, String hash, int savedBlockNumber) {
        try {
            int currentBlockNumber = getBlockNumber(rpcUrl);
            if (currentBlockNumber - 1000 > savedBlockNumber) {
                if (!isTransactionSuccess(rpcUrl, hash)) {
                    return true;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }


    public static int getBlockNumber(String rpcUrl) throws Exception {
        Web3j web3j = Web3j.build(new HttpService(rpcUrl));
        BigInteger blockNumber = web3j.ethBlockNumber().send().getBlockNumber();
        return blockNumber.intValue();

    }
}
