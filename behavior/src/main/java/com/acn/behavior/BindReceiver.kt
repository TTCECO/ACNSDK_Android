package com.acn.behavior

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.acn.biz.BizApi

class BindReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        BizApi.getInstance().userRegister(null)
    }
}
