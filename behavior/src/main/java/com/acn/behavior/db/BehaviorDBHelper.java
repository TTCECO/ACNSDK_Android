package com.acn.behavior.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by lwq on 2019-07-29.
 */
public class BehaviorDBHelper extends SQLiteOpenHelper {
    //v2:add "write_chain_timestamp"
    private static final int DB_VERSION = 3;
    private static final String DB_NAME = "behavior.db";
    public static final String TABLE_NAME = "Behaviors";

    public static final String TIMESTAMP = "timestamp";
    public static final String FROM_USER_ID = "fromUserId";
    public static final String BEHAVIOR_TYPE = "behaviorType";
    public static final String EXTRA = "extra";
    public static final String HASH = "hash";
    public static final String TRY_COUNT = "tryCount";
    public static final String STATE = "state";
    public static final String WRITE_CHAIN_TIMESTAMP = "write_chain_timestamp";  //
    public static final String BLOCK_NUMBER = "block_number";

    public BehaviorDBHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // create table Orders(Id integer primary key, CustomName text, OrderPrice integer, Country text);
        String sql = "create table if not exists " + TABLE_NAME + " (" + TIMESTAMP + " text primary key, " + FROM_USER_ID + " text, " + BEHAVIOR_TYPE + " int, " + EXTRA + " text, " + HASH + " text, " + TRY_COUNT + " int, " + STATE + " int, " + WRITE_CHAIN_TIMESTAMP + " text, "+ BLOCK_NUMBER + " int)";
        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String sql = "DROP TABLE IF EXISTS " + TABLE_NAME;
        db.execSQL(sql);
        onCreate(db);
    }
}
