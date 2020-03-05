package io.ttcnet.ttc_pay_demo_officer

import android.app.Application
import io.ttcnet.pay.TTCPay
import io.ttcnet.ttc_pay_demo_officer.util.Utils

/**
 * Created by lwq on 2018/12/18.
 */
class MyApplication : Application() {

    companion object {
        val ENV_PROD = false           //开发测试，设为false，上线改为true
    }

    override fun onCreate() {
        super.onCreate()
        TTCPay.init(
            applicationContext,
            "qi",
            Utils.getAppId(),
            Utils.getTTCPublicKey(this),
            Utils.getSymmetricKey(this)
        )

        TTCPay.setEnvProd(this, ENV_PROD)
    }
}