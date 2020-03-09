package io.ttcnet.pay

import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.os.Handler
import android.os.Looper
import android.os.Message
import io.ttcnet.pay.model.ErrorBean
import io.ttcnet.pay.model.OrderInfo
import io.ttcnet.pay.model.PayInfo
import io.ttcnet.pay.util.LogUtil
import io.ttcnet.pay.util.ReqUtil
import io.ttcnet.pay.util.SPUtil
import io.ttcnet.pay.util.Util

/**
 * Created by lwq on 2018/12/4.
 */
@SuppressLint("StaticFieldLeak")
object TTCPay {

    private lateinit var context: Context
    private var payCallback: PayCallback? = null
    private var handler = PayHandler(Looper.getMainLooper())
    private var payInfo: PayInfo? = null
    private var progressDialog: AlertDialog? = null

    /**
     * place in application
     */
    fun init(
        context: Context,
        userId: String,
        appId: String,
        ttcPublicKey: String,
        secretKey: String
    ) {
        SPUtil.setUserId(context, userId)
        SPUtil.setAppId(context, appId)
        SPUtil.setTTCPublicKey(context, ttcPublicKey)
        SPUtil.setSymmetricKey(context, secretKey)
    }

    /**
     * @param orderCreateTime millisecond
     * @param orderExpireTime millisecond
     *
     * return ttcOrderId
     */
    fun pay(context: Context, payInfo: PayInfo, callback: PayCallback?) {
        this.context = context
        this.payCallback = callback
        this.payInfo = payInfo

        if (context is Activity && progressDialog == null) {
            progressDialog = Util.showProgressDialog(context, false)
        }

        var errorBean = Util.checkInit(context)
        if (errorBean == null) {

            ReqUtil.createOrder(context, payInfo, handler)
        } else {
            callback?.error(errorBean)
            progressDialog?.dismiss()
            progressDialog = null
        }
    }

    /**
     * @param currencyType from Currency.kt
     * @param tokenId from Token.kt
     */
    fun getExchangeRate(
        context: Context,
        currencyType: Int,
        tokenId: Int,
        callback: ExchangeCallback?
    ) {
        var errorBean = Util.checkInit(context)
        if (errorBean != null) {
            callback?.error(errorBean)
        } else {
            if (context is Activity && progressDialog == null) {
                progressDialog = Util.showProgressDialog(context, false)
            }

            ReqUtil.getExchangeRate(
                context,
                currencyType,
                tokenId,
                object : Handler(Looper.getMainLooper()) {
                    override fun handleMessage(msg: Message?) {
                        super.handleMessage(msg)

                        when (msg!!.what) {
                            ReqUtil.GET_EXCHANGE_RATE_SUC -> {
                                progressDialog?.dismiss()
                                progressDialog = null
                                if (msg.obj is String) {
                                    var price = msg.obj as String
                                    callback?.done(price)
                                }
                            }

                            ReqUtil.GET_EXCHANGE_RATE_FAIL -> {
                                progressDialog?.dismiss()
                                progressDialog = null
                                var err = ErrorBean()
                                if (msg.obj is String) {
                                    err.setErrorMsg(msg.obj as String)
                                    LogUtil.e(err.getErrorMsg())
                                }
                                callback?.error(err)
                            }
                        }
                    }
                })
        }
    }

    fun getOrderDetail(context: Context, orderId: String, callback: GetOrderDetailCallback?) {
        var errorBean = Util.checkInit(context)
        if (errorBean != null) {
            callback?.error(errorBean)
        } else {
            ReqUtil.getOrderDetail(context, orderId, object : Handler(Looper.getMainLooper()) {
                override fun handleMessage(msg: Message?) {
                    super.handleMessage(msg)
                    when (msg!!.what) {
                        ReqUtil.GET_ORDER_DETAIL_SUC -> {
                            if (msg.obj is OrderInfo) {
                                callback?.done(msg.obj as OrderInfo)
                            }
                        }

                        ReqUtil.GET_ORDER_DETAIL_FAIL -> {
                            if (msg.obj is String) {
                                var error = ErrorBean()
                                error.setErrorMsg(msg.obj as String)
                                callback?.error(error)
                            }
                        }
                    }
                }
            })
        }
    }

    fun setEnvProd(context: Context, isProd: Boolean) {
        SPUtil.setEnvType(context, isProd)
    }

    class PayHandler(looper: Looper) : Handler(looper) {
        override fun handleMessage(msg: Message?) {
            super.handleMessage(msg)
            when (msg!!.what) {
                ReqUtil.GET_EXCHANGE_RATE_SUC -> {
                    if (msg.obj is String) {
                    }
                }

                ReqUtil.GET_EXCHANGE_RATE_FAIL -> {
                    if (msg.obj is String) {
                        var errorBean = ErrorBean()
                        errorBean.setErrorMsg(msg.obj as String)
                        payCallback?.error(errorBean)
                    }
                }

                ReqUtil.CREATE_ORDER_SUC -> {
                    progressDialog?.dismiss()
                    progressDialog = null
                    if (msg.obj is String) {
                        val orderId = msg.obj as String;
                        payCallback?.createTTCOrderOver(orderId)
                        if (payInfo?.payType == PayInfo.PAY_TYPE_TTC) {
                            Util.openTTCConnect(context, orderId)
                        } else if (payInfo?.payType == PayInfo.PAY_TYPE_ACN) {
                            Util.openAcornBox(context, orderId)
                        }
                    } else {
                        var bean = ErrorBean(ErrorBean.CREATE_TTC_ORDER_ERROR)
                        payCallback?.error(bean)
                    }
                }

                ReqUtil.CREATE_ORDER_FAIL -> {
                    var bean = ErrorBean()
                    bean.setErrorMsg(msg.obj as String)
                    payCallback?.error(bean)
                    progressDialog?.dismiss()
                    progressDialog = null
                }
            }
        }
    }


}