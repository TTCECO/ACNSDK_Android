package io.ttcnet.pay.model

/**
 * Created by lwq on 2018/12/5.
 */
class ErrorBean {
    private var errorId = 0
    private var errorMsg = ""

    constructor() {

    }

    constructor(id: Int) : this() {
        setErrorId(id)
    }

    companion object {
        val APP_ID_IS_EMPTY_ID = 1;
        val PUBLICK_KEY_IS_EMPTY_ID = 2;
        val SYMMETRIC_KEY_IS_EMPTY_ID = 3;
        val CREATE_TTC_ORDER_ERROR = 4;
        val NO_CURRENCY = 5;
        val GET_EXCHANGE_RATE_ERROR = 6
        val ACORN_BOX_NOT_INSTALLED = 7
        val ACORN_BOX_VERSION_LOW = 8
        val TTC_CONNECT_NOT_INSTALLED = 9
        val TTC_CONENCT_VERSION_LOW = 10
        val GET_ORDER_DETAIL_EEOR = 11


        val APP_ID_IS_EMPTY_MSG = "appId is empty";
        val PUBLIC_KEY_IS_EMPTY_MSG = "public key is empty";
        val SYMMERRIC_KEY_IS_EMPTY_MSG = "symmetric key is empty";
    }

    fun setErrorId(id: Int) {
        when (id) {
            APP_ID_IS_EMPTY_ID -> errorMsg = APP_ID_IS_EMPTY_MSG
            ACORN_BOX_NOT_INSTALLED -> errorMsg = "Acorn Box is not installed"
            ACORN_BOX_VERSION_LOW -> errorMsg = "the version of Acorn Box is too low"
        }
    }

    fun getErrorId(): Int {
        return errorId
    }

    fun setErrorMsg(msg: String?) {
        if (msg != null) {
            this.errorMsg = msg
        }
    }

    fun getErrorMsg(): String {
        return errorMsg
    }

}