package io.ttcnet.ttc_pay_demo_officer

import android.app.Application
import io.ttcnet.pay.TTCPay
import io.ttcnet.ttc_pay_demo_officer.util.Utils

/**
 * Created by lwq on 2018/12/18.
 */
class MyApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        TTCPay.init(applicationContext, "", Utils.getAppId(), Utils.getTTCPublicKey(this), Utils.getSymmetricKey(this))
        TTCPay.setEnvProd(this, BuildConfig.BUILD_TYPE.equals("release"))
    }
}