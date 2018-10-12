package com.ttc.sdk.util;

import android.util.Log;

public class TTCLogger {

    private static final String TAG = "TTC_SDK";

    public static boolean ON;

    public static void d(String msg) {
        if (ON) {
            Log.e(TAG, msg);
        }
    }

    public static void e(String msg) {
        if (ON) {
            Log.d(TAG, msg);
        }
    }

}
