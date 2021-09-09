package io.ttcnet.pay.util

import android.util.Log
import io.ttcnet.pay.BuildConfig

/**
 * Created by lwq on 2018/12/18.
 */
object LogUtil {
    fun d(content:String) {
        if (BuildConfig.BUILD_TYPE != "release") {
            Log.d("ttc_pay", content)
        }
    }

    fun e(content: String) {
        Log.e("ttc_pay", content)
    }
}