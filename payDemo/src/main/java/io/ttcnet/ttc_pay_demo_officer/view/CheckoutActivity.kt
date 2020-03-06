package io.ttcnet.ttc_pay_demo_officer.view

import android.app.Activity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import android.content.Intent
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import android.text.TextUtils
import android.view.View
import io.ttcnet.pay.ExchangeCallback
import io.ttcnet.pay.PayCallback
import io.ttcnet.pay.PayUtil
import io.ttcnet.pay.TTCPay
import io.ttcnet.pay.model.Currency
import io.ttcnet.pay.model.ErrorBean
import io.ttcnet.pay.model.PayInfo
import io.ttcnet.pay.model.Token
import io.ttcnet.ttc_pay_demo_officer.MyApplication
import io.ttcnet.ttc_pay_demo_officer.R
import io.ttcnet.ttc_pay_demo_officer.adapter.CheckoutAdapter
import io.ttcnet.ttc_pay_demo_officer.constant.Constant
import io.ttcnet.ttc_pay_demo_officer.model.PayChannelModel
import io.ttcnet.ttc_pay_demo_officer.util.Utils
import io.ttcnet.ttc_pay_demo_officer.viewmodel.CartViewModel
import io.ttcnet.ttc_pay_demo_officer.viewmodel.ChannelViewModel
import kotlinx.android.synthetic.main.activity_checkout.*
import kotlinx.android.synthetic.main.appbar_layout.*
import java.text.SimpleDateFormat

class CheckoutActivity : BaseActivity() {


    private lateinit var cartViewModel: CartViewModel
    private lateinit var channelViewModel: ChannelViewModel

    private lateinit var adapter: CheckoutAdapter
    private var payChannels: ArrayList<PayChannelModel>? = null
    private var payInfo = PayInfo()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_checkout)

        appbar_left_iv1.setImageResource(R.mipmap.black_back)
        appbar_left_iv1.setOnClickListener { onBackPressed() }
        appbar_left_text1.setText(getString(R.string.checkout))

        cartViewModel = ViewModelProviders.of(this).get(CartViewModel::class.java)
        channelViewModel = ViewModelProviders.of(this).get(ChannelViewModel::class.java)

        payChannels = channelViewModel.channels.value
        adapter = CheckoutAdapter(this, payChannels, channelViewModel)
        checkout_rv.layoutManager = androidx.recyclerview.widget.LinearLayoutManager(this)
        checkout_rv.adapter = adapter

        channelViewModel.channels.observe(this, Observer {
            if (it != null && it.size > 0) {
                adapter.notifyDataSetChanged()
            }
        })

        adapter.setCheckedChannelCallback(object : CheckoutAdapter.CheckedChannelCallback {
            override fun done(channelId: Int) {
//               var selectedChannel : PayChannelModel
                for (channel in payChannels!!) {
                    if (channel.id == channelId) {
                        channel.checked = true;
//                        selectedChannel = channel
                        checkout_pay_total.setBackgroundResource(channel.checkedBgColorId)
                    }else {
                        channel.checked = false;
                    }
                }

                if (channelId == Constant.PAY_CHANNEL_TTC_ID) {
                    payInfo.payType = 0
                    getTTCExchangeRate(Token.ID_TTC)
                } else if (channelId == Constant.PAY_CHANNEL_ACN_ID) {
                    payInfo.payType = 1
                    getTTCExchangeRate(Token.ID_ACN)
                }

                adapter.notifyDataSetChanged()
            }
        })

        checkout_pay_total.setOnClickListener {
            if (!TextUtils.isEmpty(payInfo.totalTTCWei)) {
                payTTC()
            } else {
                Utils.toast(activity, "total fee is empty")
            }
        }
    }

    fun getTTCExchangeRate(tokenId: Int) {
        TTCPay.getExchangeRate(this, Currency.dollar, tokenId, object : ExchangeCallback {
            override fun done(ttcPrice: String) {
                var goods = cartViewModel.checkedFurniture.value
                if (goods != null) {
                    var totalLegalTender = 0.0

                    var sb = StringBuilder()
                    for (f in goods) {
                        totalLegalTender += f.price
                        if (!sb.isEmpty()) {
                            sb.append(",")
                        }
                        sb.append(f.title).append(" ").append(f.description)
                    }
                    var totalTTC = PayUtil.legalTender2TTC(ttcPrice, totalLegalTender);
                    payInfo.orderContent = sb.toString()
                    payInfo.totalTTCWei = PayUtil.ttc2Wei(totalTTC)
                    if (tokenId == Token.ID_TTC) {
                        checkout_pay_total.setText("Pay " + totalTTC + "TTC")
                    }else if (tokenId == Token.ID_ACN) {
                        checkout_pay_total.setText("Pay " + totalTTC + "ACN")
                    }
                    checkout_pay_total.visibility = View.VISIBLE
                }
            }

            override fun error(errorBean: ErrorBean) {
                Utils.toast(activity, errorBean.getErrorMsg())
            }
        })
    }

    fun payTTC() {
        val now = System.currentTimeMillis()
        val format = SimpleDateFormat("yyyyMMddHHmmss")
        payInfo.merchantOrderNo = format.format(now)
        payInfo.orderCreateTime = now
        payInfo.orderExpireTime = now + 3 * 60 * 1000   //3min
        payInfo.signature = Utils.getSignFromServer(activity, payInfo, MyApplication.APP_ID)
        TTCPay.pay(activity, payInfo, object : PayCallback {
            override fun createTTCOrderOver(ttcOrderId: String) {
                val intent = Intent(activity, PaymentDetailActivity::class.java)
                intent.putExtra(Constant.TTC_ORDER_ID, ttcOrderId)
                startActivityForResult(intent, 0)
                cartViewModel.clear()
                finish()
            }

            override fun error(errorBean: ErrorBean) {
                Utils.toast(activity, errorBean.getErrorMsg())
            }

            override fun payFinish(ttcOrderId: String, txHash: String, orderState: Int) {
            }
        })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            finish()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
    }
}
