package io.ttcnet.pay

import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Handler
import android.os.Looper
import android.os.Message
import io.ttcnet.pay.model.ErrorBean
import io.ttcnet.pay.model.OrderInfo
import io.ttcnet.pay.model.PayInfo
import io.ttcnet.pay.receiver.PayTxHashReceiver
import io.ttcnet.pay.util.LogUtil
import io.ttcnet.pay.util.ReqUtil
import io.ttcnet.pay.util.SPUtil
import io.ttcnet.pay.util.Util

/**
 * Created by lwq on 2018/12/4.
 */
@SuppressLint("StaticFieldLeak")
object TTCPay {
    private lateinit var activity: Activity
    private var payCallback: PayCallback? = null
    private var handler = PayHandler(Looper.getMainLooper())
    private var payInfo: PayInfo? = null
    private var progressDialog: AlertDialog? = null
    private var sleepTime = Long.MAX_VALUE;  //ms
    private lateinit var getTxHashThread: Thread
    /**
     * place in Main or Home activity
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

        var receiver = PayTxHashReceiver()
        var filter = IntentFilter()
        filter.addAction("acn.pay.SEND.TX_HASH")
        filter.addCategory(Intent.CATEGORY_DEFAULT)

        context.registerReceiver(receiver, filter)

        getTxHashThread = Thread(Runnable {
            while (true) {
                if (PayTxHashReceiver.receivedData) {
                    handler.post {
                        payCallback?.payFinish(
                            PayTxHashReceiver.isSuc,
                            PayTxHashReceiver.txHash,
                            PayTxHashReceiver.orderId,
                            PayTxHashReceiver.orderState
                        )
                    }
                    PayTxHashReceiver.receivedData = false
                    sleepTime = Long.MAX_VALUE
                }
                try {
                    LogUtil.d("get txhash receiver sleep:$sleepTime")
                    Thread.sleep(sleepTime)
                } catch (t: Throwable) {
                    t.printStackTrace()
                }
            }
        })
        getTxHashThread.start()
    }

    /**
     * @param orderCreateTime millisecond
     * @param orderExpireTime millisecond
     *
     * 在调用pay的activity的onActivityResult中通过intent返回txHash. intent.getString
     */
    fun pay(context: Activity, payInfo: PayInfo, callback: PayCallback?) {
        this.activity = context
        this.payCallback = callback
        this.payInfo = payInfo

        if (progressDialog == null) {
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
                                var err = ErrorBean(ErrorBean.GET_EXCHANGE_RATE_ERROR)
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
                            val error = ErrorBean(ErrorBean.GET_ORDER_DETAIL_EEOR)
                            if (msg.obj is String) {
                                error.setErrorMsg(msg.obj as String)
                            }
                            callback?.error(error)
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
                    var errorBean = ErrorBean(ErrorBean.GET_EXCHANGE_RATE_ERROR)
                    if (msg.obj is String) {
                        errorBean.setErrorMsg(msg.obj as String)
                    }
                    payCallback?.error(errorBean)
                }

                ReqUtil.CREATE_ORDER_SUC -> {
                    progressDialog?.dismiss()
                    progressDialog = null
                    if (msg.obj is String) {
                        val orderId = msg.obj as String;
                        payCallback?.createTTCOrderOver(orderId)
                        var openRes = 0
                        if (payInfo?.payType == PayInfo.PAY_TYPE_TTC) {
                            openRes = Util.openTTCConnect(activity, orderId)
                        } else if (payInfo?.payType == PayInfo.PAY_TYPE_ACN) {
                            openRes = Util.openAcornBox(activity, orderId)
                        }

                        if (openRes == 0) {
                            sleepTime = 100L
                            getTxHashThread.interrupt()
                        } else {
                            payCallback?.error(ErrorBean(openRes))
                        }

                    } else {
                        payCallback?.error(ErrorBean(ErrorBean.CREATE_TTC_ORDER_ERROR))
                    }
                }

                ReqUtil.CREATE_ORDER_FAIL -> {
                    val bean = ErrorBean(ErrorBean.CREATE_TTC_ORDER_ERROR)
                    if (msg.obj is String) {
                        bean.setErrorMsg(msg.obj as String)
                    }
                    payCallback?.error(bean)
                    progressDialog?.dismiss()
                    progressDialog = null
                }
            }
        }
    }


}