package io.ttcnet.ttc_pay_demo_officer.view

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import io.ttcnet.ttc_pay_demo_officer.constant.Constant

class SplashActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        startActivity(Intent(activity, MainActivity::class.java))

//        order_id
        var orderId =intent.getStringExtra("order_id")
        if (!TextUtils.isEmpty(orderId)) {
            val intent = Intent(activity, PaymentDetailActivity::class.java)
            intent.putExtra(Constant.TTC_ORDER_ID, orderId)
            startActivityForResult(intent, 0)
        }

        finish()
    }
}
