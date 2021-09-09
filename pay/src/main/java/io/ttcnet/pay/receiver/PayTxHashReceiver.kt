package io.ttcnet.pay.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

class PayTxHashReceiver : BroadcastReceiver() {

    companion object {
        var receivedData = false
        var isSuc = false
        var txHash = ""
        var orderId = ""
        var orderState = 0   //// 1-创建订单 2-确认中 3-支付完成  4-已过期 5-已失效(有效期内下架等)
    }

    override fun onReceive(context: Context, intent: Intent) {
        receivedData = true
        isSuc = intent.getBooleanExtra("pay_suc", false)
        txHash = intent.getStringExtra("tx_hash")
        orderId = intent.getStringExtra("order_id")
        orderState = intent.getIntExtra("order_state", 0)
    }
}
