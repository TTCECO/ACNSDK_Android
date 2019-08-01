package com.acn.behavior.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import com.acn.behavior.model.BehaviorModel;
import com.acn.behavior.util.SDKLogger;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lwq on 2019-07-29.
 */
public class BehaviorDBManager {
    private BehaviorDBHelper helper;
    private SQLiteDatabase db;


    public BehaviorDBManager(Context context) {
        helper = new BehaviorDBHelper(context);
        db = helper.getWritableDatabase();
    }

//    public void add(BehaviorModel model) {
//
//        db.beginTransaction();
//        try {
//            db.execSQL("insert into " + BehaviorDBHelper.TABLE_NAME + " (timestamp, fromUserId, behaviorType, extra, hash, tryCount, state) values ( " + model.timestamp + ", " + model.fromUserId + ", " + model.behaviorType + ", " + model.extra + ", " + model.hash + ", " + model.tryCount + ", " + model.state +" )");
//            db.setTransactionSuccessful();
//        } finally {
//            db.endTransaction();
//        }
//    }

    public void insert(String timestamp, String fromUserId, int behaviorType, String extra, String hash, int tryCount, int state) {

        SDKLogger.d("execute insert");

        BehaviorModel queryModel = getBehaviorModel(timestamp);
        if (queryModel != null) {
            SDKLogger.e("this behavior is exist. " + queryModel.toString());
            return;
        }

        db.beginTransaction();
        try {

            ContentValues contentValues = new ContentValues();
            contentValues.put(BehaviorDBHelper.TIMESTAMP, timestamp);
            contentValues.put(BehaviorDBHelper.FROM_USER_ID, fromUserId);
            contentValues.put(BehaviorDBHelper.BEHAVIOR_TYPE, behaviorType);
            contentValues.put(BehaviorDBHelper.EXTRA, extra);
            contentValues.put(BehaviorDBHelper.HASH, hash);
            contentValues.put(BehaviorDBHelper.TRY_COUNT, tryCount);
            contentValues.put(BehaviorDBHelper.STATE, state);
            db.insert(BehaviorDBHelper.TABLE_NAME, null, contentValues);

//            db.execSQL("insert into " + BehaviorDBHelper.TABLE_NAME + " (timestamp, fromUserId, behaviorType, extra, hash, tryCount, state) values ( " + timestamp + ", " + fromUserId + ", " + behaviorType + ", " + extra + ", " + hash + ", " + tryCount + ", " + state+ " )");
            db.setTransactionSuccessful();
            SDKLogger.d("insert to  db suc. " + timestamp + "," + fromUserId + "," + behaviorType + "," + extra);
        } catch (Exception e) {
            SDKLogger.e(e.getMessage());
        } finally {
            db.endTransaction();
        }
    }

//    public void update(String timestamp, String key, Object value) {
//
//        ContentValues values = new ContentValues();
//        switch (key) {
//            case "timestamp":
//            case "fromUserId":
//            case "extra":
//            case "hash":
//                values.put(key, String.valueOf(value));
//                break;
//
//            case "behaviorType":
//            case "tryCount":
//            case "state":
//                values.put(key, value);
//                break;
//
//        }
//    }

    public void updateString(String timestamp, String key, String value) {

        ContentValues values = new ContentValues();
        values.put(key, value);
        String[] whereArgs = {timestamp};
        db.update(BehaviorDBHelper.TABLE_NAME, values, "timestamp=?", whereArgs);
    }

    public void updateInt(String timestamp, String key, int value) {

        ContentValues values = new ContentValues();
        values.put(key, value);
        String[] whereArgs = {timestamp};
        db.update(BehaviorDBHelper.TABLE_NAME, values, "timestamp=?", whereArgs);
    }

    public void delete(String timestamp) {

        try {
            db.beginTransaction();
            db.delete(BehaviorDBHelper.TABLE_NAME, "timestamp=?", new String[]{timestamp});
            db.setTransactionSuccessful();
        } catch (Exception e) {
            SDKLogger.e(e.getMessage());
        } finally {
            db.endTransaction();
        }
    }

    public BehaviorModel getBehaviorModel(String timestamp) {
        BehaviorModel model = null;

        String query = "select * from " + BehaviorDBHelper.TABLE_NAME + " where " + BehaviorDBHelper.TIMESTAMP + " = " + "\"" + timestamp + "\"";
        try {
            db.beginTransaction();
            Cursor cursor = db.rawQuery(query, null);
            if (cursor.getCount() > 0) {
                if (cursor.moveToNext()) {
                    model = parseCursor(cursor);
                }
            }
            db.setTransactionSuccessful();
        } catch (Exception e) {
            SDKLogger.e(e.getMessage());
        } finally {
            db.endTransaction();
        }

        return model;
    }


    public List<BehaviorModel> getAllASCTimestamp(String fromUserId) {
        List<BehaviorModel> res = new ArrayList<>();

        String[] column = new String[]{BehaviorDBHelper.FROM_USER_ID};
        String[] values = new String[]{fromUserId};
        String selection = BehaviorDBHelper.FROM_USER_ID + " = ? ";

        String query = "select * from " + BehaviorDBHelper.TABLE_NAME + " where " + BehaviorDBHelper.FROM_USER_ID + " = " + "\"" + fromUserId + "\" " + "order by "+ BehaviorDBHelper.TIMESTAMP + " asc";
        try {
            db.beginTransaction();
//            Cursor cursor = db.query(BehaviorDBHelper.TABLE_NAME, column, selection, values, null, null, null);
            Cursor cursor = db.rawQuery(query, null);
            if (cursor.getCount() > 0) {
                while (cursor.moveToNext()) {
                    BehaviorModel model = parseCursor(cursor);
                    res.add(model);
                }
            }
            db.setTransactionSuccessful();
        } catch (Exception e) {
            SDKLogger.e(e.getMessage());
        } finally {
            db.endTransaction();
        }

        return res;
    }

    private BehaviorModel parseCursor(Cursor cursor) {
        String timestamp = cursor.getString(cursor.getColumnIndex(BehaviorDBHelper.TIMESTAMP));
        String fromUserId = cursor.getString(cursor.getColumnIndex(BehaviorDBHelper.FROM_USER_ID));
        int behaviorType = cursor.getInt(cursor.getColumnIndex(BehaviorDBHelper.BEHAVIOR_TYPE));
        String extra = cursor.getString(cursor.getColumnIndex(BehaviorDBHelper.EXTRA));
        String hash = cursor.getString(cursor.getColumnIndex(BehaviorDBHelper.HASH));
        int tryCount = cursor.getInt(cursor.getColumnIndex(BehaviorDBHelper.TRY_COUNT));
        int state = cursor.getInt(cursor.getColumnIndex(BehaviorDBHelper.STATE));

        BehaviorModel model = new BehaviorModel();
        model.timestamp = timestamp;
        model.fromUserId = fromUserId;
        model.behaviorType = behaviorType;
        model.extra = extra;
        model.hash = hash;
        model.tryCount = tryCount;
        model.state = state;

        return model;
    }


}
