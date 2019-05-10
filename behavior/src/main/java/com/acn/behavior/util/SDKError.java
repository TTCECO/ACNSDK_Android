package com.acn.behavior.util;

/**
 * Created by lwq on 2018/8/8
 */
public class SDKError {
    public static final int CONTEXT_IS_NULL = 10;
    public static final int APP_ID_IS_EMPTY = 100;
    public static final int SECRET_KEY_IS_EMPTY = 101;
    public static final int USER_ID_IS_EMPTY = 102;
    public static final int NOT_INITIAL = 103;
    public static final int SDK_SERVER_OFF = 104;
    public static final int USER_ID_NOT_CORRECT = 105;
    public static final int NOT_REGISTER = 106;
    public static final int NOT_BOUND_WALLET = 107;
    public static final int EXTRA_NOT_JSON = 109;
    public static final int BEHAVIOR_TYPE_IS_SMALLER = 110;
    public static final int WALLET_ADDRESS_IS_EMPTY = 111;

    public static final int TRANSACTION_ERROR = 300;
    public static final int PRV_KEY_ERROR = 301;
    public static final int ACTION_ADDRESS_IS_EMPTY = 302;

    public static String getMessage(int errorCode) {
        String msg = "";
        switch (errorCode) {
            case CONTEXT_IS_NULL:
                msg = "context is null";
                break;

            case APP_ID_IS_EMPTY:
                msg = "appId cannot be empty";
                break;

            case SECRET_KEY_IS_EMPTY:
                msg = "SecretKey cannot be empty";
                break;
            case USER_ID_IS_EMPTY:
                msg = "userID cannot be empty";
                break;
            case NOT_INITIAL:
                msg = "SDK is not initiated, please initiate";
                break;
            case SDK_SERVER_OFF:
                msg = "SDK is off";
                break;
            case USER_ID_NOT_CORRECT:
                msg = "Update information must be current user";
                break;
            case NOT_REGISTER:
                msg = "No registered user";
                break;
            case NOT_BOUND_WALLET:
                msg = "no bound wallet";
                break;
            case EXTRA_NOT_JSON:
                msg = "Extra is not json";
                break;
            case BEHAVIOR_TYPE_IS_SMALLER:
                msg = "behaviorType must be greater than 100";
                break;

            case WALLET_ADDRESS_IS_EMPTY:
                msg = "wallet address is empty";
                break;

            case TRANSACTION_ERROR:
                msg = "transaction is error";
                break;

            case PRV_KEY_ERROR:
                msg = "private key is error";
                break;

            case ACTION_ADDRESS_IS_EMPTY:
                msg = "action address is empty";
                break;

            default:
                break;
        }
        return msg;
    }
}
