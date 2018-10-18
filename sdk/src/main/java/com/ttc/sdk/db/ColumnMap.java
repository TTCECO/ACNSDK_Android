package com.ttc.sdk.db;

import android.database.Cursor;

import java.util.HashMap;

public class ColumnMap {
    private final HashMap<String, Integer> map;

    public ColumnMap(Cursor cursor) {
        String[] names = cursor.getColumnNames();
        int count = names.length;
        map = new HashMap<>(count);
        for (int i = 0; i < count; i++) {
            map.put(names[i], i);
        }
    }

    public int indexOf(String column) {
        Integer index = map.get(column);
        return index != null ? index : -1;
    }
}