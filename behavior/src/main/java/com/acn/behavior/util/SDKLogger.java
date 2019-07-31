package com.acn.behavior.util;

import android.util.Log;

public class SDKLogger {

    private static final String TAG = "ACN_SDK";

    public static boolean ON;

    public static void d(String msg) {
        if (ON) {
            Log.d(TAG, msg);
        }
    }

    public static void e(String msg) {
        if (ON) {
            Log.e(TAG, msg);
        }
    }

}
