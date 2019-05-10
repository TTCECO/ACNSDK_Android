package com.acn.behavior.util;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

public class AlgorithmUtil {

    /*
      int32	behaviorType = 1;	// 1-发帖 2-点赞 3-评论 4-分享 5-视频播放 6-文件上传 7-举报 8-广告
      自定义占用 behaviorType > 100 的
      string  actionHash = 2 ;// 行为记录的交易的hash值，交易单号 ,注意这个参数不参与 hash 值计算
      string  fromUserId = 3;	// 行为发起方ID
      int64 	timestamp = 4;	// 行为时间，单位毫秒
      string 	extra = 5;		// 附属信息   不同behaviorType对应的特定信息,必须为 json 结构字
     */
    public static String hash(int behaviorType, String fromUserId, long timestamp, String extra) {

        Map<String, String> map = new HashMap<>();
        map.put("actionType", String.valueOf(behaviorType));
        map.put("fromUserId", fromUserId);
        map.put("timestamp", String.valueOf(timestamp));
        map.put("extra", extra);

        TreeMap<String, String> sortMap = new TreeMap<>(new Comparator<String>() {
            @Override
            public int compare(String o1, String o2) {
                return o1.compareTo(o2);
            }
        });

        sortMap.putAll(map);

        StringBuilder sb = new StringBuilder();

        boolean first = true;

        for (Map.Entry<String, String> entry : sortMap.entrySet()) {

            if (first) {
                first = false;
            } else {
                sb.append("&");
            }

            sb.append(entry.getKey()).append("=").append(entry.getValue());

        }

        return EncryptUtil.md5(sb.toString());

    }
}
