package io.ttcnet.pay

import io.ttcnet.pay.model.ErrorBean

/**
 * Created by lwq on 2018/12/4.
 */
interface PayCallback {
    fun createMaroOrderOver(maroOrderId: String)
    fun payFinish(isSuc: Boolean, txHash: String, maroOrderId: String, orderState: Int)
    fun error(errorBean: ErrorBean)
}