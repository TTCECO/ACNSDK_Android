package com.acn.behavior;

import com.acn.biz.model.BindSucData;

import java.math.BigDecimal;
import java.util.Map;

public interface IManager {

    interface BindCallback {
        void success(BindSucData data);

        void error(String msg);
    }

    interface UnbindCallback{
        void success();

        void error(String msg);
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
