package com.ttc.sdk.db;

import android.content.ContentValues;
import android.database.Cursor;

import com.ttc.sdk.model.EventBean;


public class EventMapper {

    public ContentValues toSQL(EventBean entity) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(EventTable.FIELD_ACTION_TYPE, entity.getBehaviorType());
        contentValues.put(EventTable.FIELD_ACTION_HASH, entity.getActionHash());
        contentValues.put(EventTable.FIELD_USER_ID, entity.getUserId());
        contentValues.put(EventTable.FIELD_EXTRA, entity.getExtra());
        contentValues.put(EventTable.FIELD_DATA, entity.getData());
        contentValues.put(EventTable.FIELD_TIMESTAMP, entity.getTimestamp());
        contentValues.put(EventTable.FIELD_BC_RETRY_COUNT, entity.getBcRetryCount());
        contentValues.put(EventTable.FIELD_BIZ_RETRY_COUNT, entity.getBizRetryCount());
        contentValues.put(EventTable.FIELD_NONCE, entity.getNonce());
        contentValues.put(EventTable.FIELD_ACTION_STATE, entity.getActionState());
        return contentValues;
    }

    public EventBean toBean(Cursor cursor) {
        ColumnMap columnMap = new ColumnMap(cursor);
        EventBean entity = new EventBean();
        entity.setBehaviorType(MapHelper.toInteger(cursor, columnMap.indexOf(EventTable.FIELD_ACTION_TYPE)));
        entity.setActionHash(MapHelper.toString(cursor, columnMap.indexOf(EventTable.FIELD_ACTION_HASH)));
        entity.setTimestamp(MapHelper.toLong(cursor, columnMap.indexOf(EventTable.FIELD_TIMESTAMP)));
        entity.setExtra(MapHelper.toString(cursor, columnMap.indexOf(EventTable.FIELD_EXTRA)));
        entity.setData(MapHelper.toString(cursor, columnMap.indexOf(EventTable.FIELD_DATA)));
        entity.setUserId(MapHelper.toString(cursor, columnMap.indexOf(EventTable.FIELD_USER_ID)));
        entity.setBcRetryCount(MapHelper.toInteger(cursor, columnMap.indexOf(EventTable.FIELD_BC_RETRY_COUNT)));
        entity.setBizRetryCount(MapHelper.toInteger(cursor, columnMap.indexOf(EventTable.FIELD_BIZ_RETRY_COUNT)));
        entity.setNonce(MapHelper.toString(cursor, columnMap.indexOf(EventTable.FIELD_NONCE)));
        entity.setActionState(MapHelper.toInteger(cursor, columnMap.indexOf(EventTable.FIELD_ACTION_STATE)));
        return entity;
    }
}
