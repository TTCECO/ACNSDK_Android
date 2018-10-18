package com.ttc.sdk;

import java.math.BigDecimal;
import java.util.Map;

public interface IManager {

    interface BindCallback {
        void onMessage(boolean success, String message);
    }

    interface BalanceCallback {
        void success(BigDecimal balance);

        void error(String msg);
    }

    interface UserInfoCallback {
        void success(Map<String, String> map);

        void error(String msg);
    }
}
