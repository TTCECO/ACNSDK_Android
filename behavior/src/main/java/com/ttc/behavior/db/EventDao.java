package com.ttc.behavior.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.ttc.behavior.model.EventBean;
import com.ttc.behavior.util.TTCSp;

import java.util.ArrayList;
import java.util.List;


public class EventDao {
    private static SQLiteDatabase database;
    private static EventMapper mapper;

    private EventDao() {

    }

    public static void init(Context context) {
        TTCOpenHelper openHelper = new TTCOpenHelper(context);
        database = openHelper.getWritableDatabase();
        mapper = new EventMapper();
    }

    public static void insert(int behaviorType, String actionHash, String extra, long timestamp) {
        EventBean entity = new EventBean();
        entity.setBehaviorType(behaviorType);
        entity.setActionHash(actionHash);
        entity.setExtra(extra);
        entity.setTimestamp(timestamp);
        entity.setUserId(TTCSp.getUserId());
        insertEvent(entity);
    }

    public static void insertEvent(EventBean entity) {
        ContentValues contentValues = mapper.toSQL(entity);
        database.insert(EventTable.TABLE_NAME, null, contentValues);
    }

    public static List<EventBean> fetchEvents() {
        List<EventBean> list = new ArrayList<>();
        Cursor cursor = database.query(EventTable.TABLE_NAME, null, null, null, null, null, null);
        while (cursor.moveToNext()) {
            EventBean entity = mapper.toBean(cursor);
            list.add(entity);
        }
        cursor.close();
        return list;
    }

    public static List<EventBean> fetchEventByUserId(String userId) {
        List<EventBean> list = new ArrayList<>();
        Cursor cursor = database.query(EventTable.TABLE_NAME, null, EventTable.FIELD_USER_ID + " = ?", new
                String[]{userId}, null, null, null, "20");
        while (cursor.moveToNext()) {
            EventBean entity = mapper.toBean(cursor);
            list.add(entity);
        }
        cursor.close();
        return list;
    }

    public static EventBean fetchEventByUserActionHash(String actionHash) {
        Cursor cursor = database.rawQuery("select * from " + EventTable.TABLE_NAME + " where " + EventTable
                .FIELD_ACTION_HASH + "=?", new String[]{actionHash});
        if (cursor.moveToNext()) {
            EventBean entity = mapper.toBean(cursor);
            return entity;
        }
        cursor.close();
        return null;
    }

    public static boolean deleteEvent(String actionHash) {
        return database.delete(EventTable.TABLE_NAME, EventTable.FIELD_ACTION_HASH + " = ?" , new String[]{actionHash}) != 0;
    }

    public static boolean deleteEventByBCRetryCount(int retryCount) {
        return database.delete(EventTable.TABLE_NAME, EventTable.FIELD_BC_RETRY_COUNT + " >= " + retryCount, null) != 0;
    }


    public static boolean deleteEventByBizRetryCount(int retryCount) {
        return database.delete(EventTable.TABLE_NAME, EventTable.FIELD_BIZ_RETRY_COUNT + " >= " + retryCount, null)
                != 0;
    }

    public static void update(EventBean entity) {
        ContentValues contentValues = mapper.toSQL(entity);
        database.replace(EventTable.TABLE_NAME, null, contentValues);
    }

    //清空数据表
    public static void deleteAllData() {
        database.execSQL("delete from " + EventTable.TABLE_NAME);
    }

}
