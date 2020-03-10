package io.ttcnet.ttc_pay_demo_officer.view

import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.widget.TextView
import io.ttcnet.pay.GetOrderDetailCallback
import io.ttcnet.pay.TTCPay
import io.ttcnet.pay.model.ErrorBean
import io.ttcnet.pay.model.OrderInfo
import io.ttcnet.pay.model.OrderState
import io.ttcnet.ttc_pay_demo_officer.R
import io.ttcnet.ttc_pay_demo_officer.constant.Constant
import io.ttcnet.ttc_pay_demo_officer.util.Utils
import kotlinx.android.synthetic.main.activity_payment_detail.*
import kotlinx.android.synthetic.main.appbar_layout.*

class PaymentDetailActivity : BaseActivity() {

    private var orderId :String ?=null
    private var handler = MyHandler()

    lateinit var itemOrderNoTitle: TextView
    lateinit var itemOrderNoContent: TextView
    lateinit var itemCompanyTitle: TextView
    lateinit var itemCompanyContent: TextView
    lateinit var itemInfoTitle: TextView
    lateinit var itemInfoContent: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_payment_detail)

        appbar_left_iv1.setImageResource(R.mipmap.close)
        appbar_left_iv1.setOnClickListener { finish() }

        appbar_left_text1.setText(getString(R.string.payment_detail))

        itemOrderNoTitle = detail_order_number.findViewById(R.id.title_content_title_tv)
        itemOrderNoContent = detail_order_number.findViewById(R.id.title_content_content_tv)
        itemCompanyTitle = detail_order_company.findViewById(R.id.title_content_title_tv)
        itemCompanyContent = detail_order_company.findViewById(R.id.title_content_content_tv)
        itemInfoTitle = detail_order_info.findViewById(R.id.title_content_title_tv)
        itemInfoContent = detail_order_info.findViewById(R.id.title_content_content_tv)

        itemOrderNoTitle.setText("Order Number")
        itemCompanyTitle.setText("Company")
        itemInfoTitle.setText("Order Info")

        orderId = intent.getStringExtra(Constant.TTC_ORDER_ID)
    }

    fun queryOrderState() {
        if (orderId == null) {
            Utils.toast(activity,"order Id is null")
            return
        }
        TTCPay.getOrderDetail(activity, orderId!!, object : GetOrderDetailCallback {
            override fun done(orderInfo: OrderInfo) {
                display(orderInfo)
            }

            override fun error(errorBean: ErrorBean) {
                Utils.toast(activity, errorBean.getErrorMsg())
            }
        })
    }

    fun display(orderInfo: OrderInfo) {
        detail_status.setTextColor(activity.resources.getColor(R.color.black))
        when (orderInfo.state) {
            OrderState.STATE_CREATE -> detail_status.setText("created")
            OrderState.STATE_VERIFYING -> detail_status.setText("confirming...")
            OrderState.STATE_FINISH -> {
                detail_status.setText("Success")
                detail_status.setTextColor(activity.resources.getColor(R.color.green))
                handler.removeMessages(0)
            }
            OrderState.STATE_OVERDUE -> detail_status.setText("overdue")
            OrderState.STATE_INVALID -> detail_status.setText("invalid")
        }
        if (orderInfo.tokenId == 0) {
            detail_price.setText("Amount:" + orderInfo.totalTTC + "TTC")
        }else if (orderInfo.tokenId == 1) {
            detail_price.setText("Amount:" + orderInfo.totalTTC + "ACN")
        }

        itemOrderNoContent.setText(orderInfo.outOrderId)
        itemCompanyContent.setText(orderInfo.partnerName)
        itemInfoContent.setText(orderInfo.description)
    }

    override fun onResume() {
        super.onResume()
        handler.sendEmptyMessage(0)
    }

    override fun onPause() {
        super.onPause()
        handler.removeMessages(0)
    }

    inner class MyHandler : Handler() {
        override fun handleMessage(msg: Message?) {
            super.handleMessage(msg)
            queryOrderState()
            handler.sendEmptyMessageDelayed(0, 2000)
        }
    }
}
