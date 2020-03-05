package io.ttcnet.pay.util

import android.content.Context
import android.os.Handler
import android.os.Message
import com.google.protobuf.nano.InvalidProtocolBufferNanoException
import eco.ttc.proto.nano.Common
import eco.ttc.proto.nano.Sdk
import io.ttcnet.pay.BuildConfig
import io.ttcnet.pay.model.PayInfo
import java.util.concurrent.Executors

/**
 * Created by lwq on 2018/12/4.
 */
object ReqUtil {

    val WHAT_BASE = 1000;
    val CREATE_ORDER_SUC = WHAT_BASE + 1;
    val CREATE_ORDER_FAIL = WHAT_BASE + 2;
    val GET_EXCHANGE_RATE_SUC = WHAT_BASE + 3;
    val GET_EXCHANGE_RATE_FAIL = WHAT_BASE + 4;
    val GET_ORDER_DETAIL_SUC = WHAT_BASE + 5;
    val GET_ORDER_DETAIL_FAIL = WHAT_BASE + 6;

    private val service = Executors.newFixedThreadPool(3)

    fun genCommonRequest(context: Context, cmdId: Int): Common.Request {
        var request = Common.Request()
        try {
            request.userId = SPUtil.getUserId(context);
            request.cmdId = cmdId
            request.release = BuildConfig.VERSION_CODE
            request.platform = 1
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return request
    }

    fun createOrder(context: Context, payInfo: PayInfo, handler: Handler?) {
        service.execute {
            var request23506 = Sdk.Request23506()
            request23506.common = genCommonRequest(context, 23506)
            var params = Sdk.Request23506.Params()
            params.appId = SPUtil.getAppId(context)
            params.requestSign = payInfo.signature
            params.outTradeNo = payInfo.merchantOrderNo
            params.description = payInfo.orderContent
            params.totalFee = payInfo.totalTTCWei
            params.createTime = payInfo.orderCreateTime
            params.expireTime = payInfo.orderExpireTime
            params.payType = payInfo.payType
            params.sellerDefinedPage = payInfo.sellerDefinedPage
            params.partnerAddress = payInfo.partnerAddress
            request23506.params = params

            val post = HttpUtil.post(context, request23506, request23506.common.cmdId)

            var msg = Message.obtain()
            msg.what = CREATE_ORDER_FAIL;
            if (post != null) {
                val response23506 = Sdk.Response23506.parseFrom(post)
                if (response23506.common.code == 0) {
                    val sha1 = Util.genRes23506DataKVString(response23506.data)
                    if (EncryptUtil.rsaVerifySha1(context, sha1, response23506.data.sign)) {
                        msg.what = CREATE_ORDER_SUC
                        msg.obj = response23506.data!!.orderId
                    }
                } else {
                    msg.obj = response23506.common.message
                }
            }
            handler?.sendMessage(msg)
        }
    }

    fun getExchangeRate(context: Context, currencyType: Int, tokenId: Int, handler: Handler?) {
        service.execute {
            var request23507 = Sdk.Request23507()
            var param = Sdk.Request23507.Params()
            param.currencyType = currencyType
            param.currencyId = tokenId
            request23507.common = genCommonRequest(context, 23507)
            request23507.params = param
            val response = HttpUtil.post(context, request23507, request23507.common.cmdId)

            var msg = Message.obtain()
            msg.what = GET_EXCHANGE_RATE_FAIL;
            if (response != null) {
                val response23507 = Sdk.Response23507.parseFrom(response)
                if (response23507.common.code == 0) {
                    val sha1 = Util.genRes23507DataKVString(response23507.data)
                    if (EncryptUtil.rsaVerifySha1(context, sha1, response23507.data.sign)) {
                        msg.what = GET_EXCHANGE_RATE_SUC
                        msg.obj = response23507.data.value
                    } else {
                        msg.obj = "verify response sign fail"
                    }
                } else {
                    msg.obj = response23507.common.message
                }
            } else {
                msg.obj = "response is null"
            }
            handler?.sendMessage(msg)
        }
    }


    @JvmStatic
    fun getOrderDetail(context: Context, orderId: String, handler: Handler?) {
        service.execute {
            val request23508 = Sdk.Request23508()
            val params = Sdk.Request23508.Params()
            val common = genCommonRequest(context, 23508)
            request23508.common = common
            params.orderId = orderId
            request23508.params = params
            val res = HttpUtil.post(context, request23508, common.cmdId)

            val msg = Message.obtain()

            msg.what = GET_ORDER_DETAIL_FAIL
            if (res != null) {
                try {
                    val response23508 = Sdk.Response23508.parseFrom(res)
                    if (response23508.common != null) {
                        if (response23508.common.code == 0) {
                            if (response23508.data != null) {
                                val sha1 = Util.genRes23508DataKVString(response23508.data)
                                if (EncryptUtil.rsaVerifySha1(
                                        context,
                                        sha1,
                                        response23508.data.sign
                                    )
                                ) {
                                    msg.what = GET_ORDER_DETAIL_SUC
                                    msg.obj = Util.order2OrderInfo(response23508.data)
                                }
                            }
                        } else {
                            msg.obj = response23508.common.message
                        }
                    }
                } catch (e: InvalidProtocolBufferNanoException) {
                    e.printStackTrace()
                }
            }
            handler?.sendMessage(msg)
        }
    }


}