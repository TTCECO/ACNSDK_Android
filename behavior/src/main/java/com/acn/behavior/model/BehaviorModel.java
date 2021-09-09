package com.acn.behavior.model;

/**
 * Created by lwq on 2019-07-29.
 */
public class BehaviorModel {
    public String timestamp;  //milliseconds产生行为时的时间戳
    public String fromUserId;  //dapp的userId
    public int behaviorType;
    public String extra;
    public String hash;
    public int tryCount;  // 如果超过5次，则不再上传
    public int state;  //0-pending 1-latest
    public String writeBlockTimestamp;  //最后一次写链的时间
    public int blockNumber;   //写链时的blockNumber

    @Override
    public String toString() {
        return timestamp + "," + fromUserId + "," + behaviorType + "," + extra + "," + hash + "," + tryCount + "," + state;
    }
}
