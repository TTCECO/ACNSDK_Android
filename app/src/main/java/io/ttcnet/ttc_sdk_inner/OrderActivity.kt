package io.ttcnet.ttc_sdk_inner

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.Toast
import io.ttcnet.pay.ExchangeCallback
import io.ttcnet.pay.GetOrderDetailCallback
import io.ttcnet.pay.PayCallback
import io.ttcnet.pay.TTCPay
import io.ttcnet.pay.model.*
import kotlinx.android.synthetic.main.activity_order.*
import java.text.SimpleDateFormat
import java.util.concurrent.Executors
import kotlin.random.Random

class OrderActivity : AppCompatActivity() {

    private var activity = this
    private var payInfo = PayInfo()
    private var orderInfo = OrderInfo()  //return by TTC server
    private val foods = arrayOf("小龙虾", "大肠粉", "刀削面")
    private val prices = doubleArrayOf(0.3, 0.1, 0.6)
    private val curry = arrayOf(1, 2, 3, 4)
    private var price = 0.0
    private var priceType = 0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_order)

        init()
    }

    fun init() {

        order_create_order.setOnClickListener {
            val now = System.currentTimeMillis()
            val format = SimpleDateFormat("yyyyMMddHHmmss")
            payInfo.orderContent = foods[Random.nextInt(0, 100) % 3]
            payInfo.merchantOrderNo = format.format(now)
            price = prices[Random.nextInt(0, 100) % 3]
            priceType = curry[Random.nextInt(0, 100) % 3]
            payInfo.orderCreateTime = now
            payInfo.orderExpireTime = now + 60 * 1000

            orderInfo.createTime = payInfo.orderCreateTime
            orderInfo.expireTime = payInfo.orderExpireTime
            orderInfo.description = payInfo.orderContent
            orderInfo.outOrderId = payInfo.merchantOrderNo
            orderInfo.state = OrderState.STATE_CREATE

            main_msg_tv.text = displayContent()
        }

        order_pay.setOnClickListener {
            TTCPay.getExchangeRate(activity, priceType, object : ExchangeCallback {
                override fun done(ttcPrice: String) {
//                    payInfo.totalTTCWei = PayUtil.ttc2Wei(PayUtil.legalTender2TTC(ttcPrice, price))


                    TTCPay.pay(activity, payInfo, object : PayCallback {

                        override fun createTTCOrderOver(ttcOrderId: String) {
                            orderInfo.ttcOrderId = ttcOrderId
                        }

                        override fun payFinish(ttcOrderId: String, txHash: String, orderState: Int) {
                            orderInfo.txHash = txHash
                            orderInfo.state = orderState
                            main_msg_tv.text = displayContent()
                        }

                        override fun error(errorBean: ErrorBean) {
                            main_msg_tv.text = displayContent()

                            Toast.makeText(activity, errorBean.getErrorMsg(), Toast.LENGTH_SHORT).show()
                        }
                    })
                }

                override fun error(errorBean: ErrorBean) {
                    Toast.makeText(activity, errorBean.getErrorMsg(), Toast.LENGTH_SHORT).show()
                }
            })
        }

        Executors.callable {
            while (orderInfo.state != OrderState.STATE_FINISH) {
                Thread.sleep(1000)
                TTCPay.getOrderDetail(activity, orderInfo.ttcOrderId, object : GetOrderDetailCallback {
                    override fun done(orderInfo: OrderInfo) {
                        activity.orderInfo = orderInfo
                        displayContent()
                    }

                    override fun error(errorBean: ErrorBean) {

                    }
                })
            }
        }

    }

    fun displayContent(): String {
        val sb = StringBuilder()
        var statement = ""
        when (orderInfo.state) {
            OrderState.STATE_CREATE -> {
                statement = "待支付"

            }
            OrderState.STATE_VERIFYING -> {
                statement = "确认中"

            }

            OrderState.STATE_FINISH -> {
                statement = "已完成"
            }
            else -> {
                statement = "已过期"

            }
        }
        var priceName = ""
        when (priceType) {
            Currency.rmb -> priceName = "元"
            Currency.dollar -> priceName = "美元"
            Currency.won -> priceName = "韩元"
            Currency.dong -> priceName = "越南盾"
        }
        sb.append("订单号：" + orderInfo.outOrderId + "\n")
        sb.append("订单内容：" + orderInfo.description + "\n")
        sb.append("总价：" + price + priceName + "\n")
        sb.append("支付状态：" + statement)

        return sb.toString()
    }

}
