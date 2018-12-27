package com.ttc.behavior.model;

public class EventBean {

    private int behaviorType;
    private String actionHash;
    private String userId;
    private long timestamp;
    private String extra;
    private String data;
    private int bcRetryCount = 1;  //0-suc, 1~10
    private int bizRetryCount = 1;  //0-suc, 1~10
    private String nonce;
    private int actionState;

    public int getBehaviorType() {
        return behaviorType;
    }

    public void setBehaviorType(int behaviorType) {
        this.behaviorType = behaviorType;
    }

    public String getActionHash() {
        return actionHash;
    }

    public void setActionHash(String actionHash) {
        this.actionHash = actionHash;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public String getExtra() {
        return extra;
    }

    public void setExtra(String extra) {
        this.extra = extra;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public int getBcRetryCount() {
        return bcRetryCount;
    }

    public void setBcRetryCount(int bcRetryCount) {
        this.bcRetryCount = bcRetryCount;
    }

    public int getBizRetryCount() {
        return bizRetryCount;
    }

    public void setBizRetryCount(int bizRetryCount) {
        this.bizRetryCount = bizRetryCount;
    }

    public String getNonce() {
        return nonce;
    }

    public void setNonce(String nonce) {
        this.nonce = nonce;
    }

    public int getActionState() {
        return actionState;
    }

    public void setActionState(int actionState) {
        this.actionState = actionState;
    }

}
