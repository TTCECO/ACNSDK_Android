package io.ttcnet.pay

import io.ttcnet.pay.model.ErrorBean

/**
 * Created by lwq on 2018/12/4.
 */
interface ExchangeCallback {
    fun done(ttcPrice: String)
    fun error(errorBean: ErrorBean)
}