package com.ttc.behavior.model;

import java.math.BigInteger;

public class TransactionResult {

    private String transactionHash;

    private BigInteger nonce;

    public TransactionResult(String transactionHash, BigInteger nonce) {
        this.transactionHash = transactionHash;
        this.nonce = nonce;
    }

    public String getTransactionHash() {
        return transactionHash;
    }

    public void setTransactionHash(String transactionHash) {
        this.transactionHash = transactionHash;
    }

    public BigInteger getNonce() {
        return nonce;
    }

    public void setNonce(BigInteger nonce) {
        this.nonce = nonce;
    }
}
