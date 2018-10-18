package com.ttc.sdk.db;

public class EventTable {

    public static final String TABLE_NAME = "behavior";

    public static final String FIELD_ACTION_TYPE = "action_type";
    public static final String FIELD_ACTION_HASH = "action_hash";
    public static final String FIELD_USER_ID = "user_id";
    public static final String FIELD_TIMESTAMP = "timestamp";
    public static final String FIELD_EXTRA = "extra";
    public static final String FIELD_DATA = "data";
    public static final String FIELD_BC_RETRY_COUNT = "bc_retry_count";
    public static final String FIELD_BIZ_RETRY_COUNT = "biz_retry_count";
    public static final String FIELD_NONCE = "nonce";
    public static final String FIELD_ACTION_STATE = "action_state";


    public static final String CREATE_TABLE = "create table " + TABLE_NAME + " (" +
            FIELD_ACTION_TYPE + " text," + FIELD_ACTION_HASH + " text unique, " + FIELD_USER_ID + " text," +
            FIELD_TIMESTAMP + " long," + FIELD_EXTRA + " text," + FIELD_BC_RETRY_COUNT + " integer," +
            FIELD_BIZ_RETRY_COUNT + " integer," + FIELD_NONCE + " text," + FIELD_ACTION_STATE + " int, " + FIELD_DATA
            + " text)";



}
