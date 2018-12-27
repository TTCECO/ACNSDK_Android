package io.ttcnet.ttc_pay_demo_officer

import android.app.Application
import io.ttcnet.pay.TTCPay
import io.ttcnet.ttc_pay_demo_officer.util.Utils

/**
 * Created by lwq on 2018/12/18.
 */
class Application : Application() {

    override fun onCreate() {
        super.onCreate()
        TTCPay.init(applicationContext, "", Utils.getAppId(), Utils.getTTCPublicKey(this), Utils.getSymmetricKey(this))
    }
}