package com.ttc.sdk.db;

import android.database.Cursor;

public class MapHelper {


    public static Integer toInteger(Cursor cursor, int index) {
        return toInteger(cursor, index, null);
    }

    public static Integer toInteger(Cursor cursor, int index, Integer defaultValue) {
        if (index != -1 && !cursor.isNull(index)) {
            return cursor.getInt(index);
        } else {
            return defaultValue;
        }
    }

    public static Long toLong(Cursor cursor, int index) {
        return toLong(cursor, index, null);
    }

    public static Long toLong(Cursor cursor, int index, Long defaultValue) {
        if (index != -1 && !cursor.isNull(index)) {
            return cursor.getLong(index);
        } else {
            return defaultValue;
        }
    }

    public static Double toDouble(Cursor cursor, int index) {
        return toDouble(cursor, index, null);
    }

    public static Double toDouble(Cursor cursor, int index, Double defaultValue) {
        if (index != -1 && !cursor.isNull(index)) {
            return cursor.getDouble(index);
        } else {
            return defaultValue;
        }
    }

    public static String toString(Cursor cursor, int index) {
        return index != -1 ? cursor.getString(index) : null;
    }

    public static Boolean toBoolean(Cursor cursor, int index) {
        if (index != -1 && !cursor.isNull(index)) {
            return cursor.getInt(index) != 0;
        } else {
            return null;
        }
    }

    public static Boolean toBoolean(Cursor cursor, int index, boolean defaultValue) {
        if (index != -1 && !cursor.isNull(index)) {
            return cursor.getInt(index) != 0;
        } else {
            return defaultValue;
        }
    }

}
