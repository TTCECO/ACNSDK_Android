package io.ttcnet.ttc_pay_demo_officer

import android.app.Application
import io.ttcnet.pay.TTCPay
import io.ttcnet.ttc_pay_demo_officer.util.Utils

/**
 * Created by lwq on 2018/12/18.
 */
class MyApplication : Application() {

    companion object {
        const val APP_ID_TATAUFO = "tataufo"
        const val APP_ID_MEITUAN = "MeiTuan"

        val ENV_PROD = false           //开发测试，设为false，上线改为true
        //如果更改了appID，需要更改Uitls.getPkcs8Key()中的私钥
        val APP_ID = APP_ID_TATAUFO    //   tataufo,  TTCMallDemo,  MeiTuan
    }

    override fun onCreate() {
        super.onCreate()
        TTCPay.init(
            applicationContext,
            "qi",
            APP_ID,
            Utils.getTTCPublicKey(this),
            Utils.getSymmetricKey(this)
        )
        TTCPay.setEnvProd(this, ENV_PROD)
    }
}