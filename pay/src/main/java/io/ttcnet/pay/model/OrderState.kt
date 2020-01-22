package io.ttcnet.pay.model

/**
 * Created by lwq on 2018/12/10.
 */
object OrderState {

    const val STATE_CREATE = 1  //
    const val STATE_VERIFYING = 2
    const val STATE_FINISH = 3
    const val STATE_OVERDUE = 4
    const val STATE_INVALID = 5
}