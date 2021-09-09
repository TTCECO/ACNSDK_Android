package io.ttcnet.pay

import io.ttcnet.pay.model.ErrorBean
import io.ttcnet.pay.model.OrderInfo

/**
 * Created by lwq on 2018/12/6.
 */
interface GetOrderDetailCallback {

    fun done(orderInfo: OrderInfo)
    fun error(errorBean: ErrorBean)
}