package io.ttcnet.pay

import io.ttcnet.pay.model.ErrorBean

/**
 * Created by lwq on 2018/12/4.
 */
interface PayCallback {
    fun createTTCOrderOver(ttcOrderId: String)
    fun payFinish(isSuc: Boolean, txHash: String, ttcOrderId: String, orderState: Int)
    fun error(errorBean: ErrorBean)
}