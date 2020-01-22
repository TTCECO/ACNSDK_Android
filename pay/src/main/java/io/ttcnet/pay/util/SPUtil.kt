package io.ttcnet.pay.util

import android.content.Context

/**
 * Created by lwq on 2018/12/5.
 */
object SPUtil {
    private val SP_TTC_PAY = "sp_ttc_pay"
    private val USER_ID = "user_id"
    private val APP_ID = "app_id"
    private val TTC_PUBLIC_KEY = "ttc_public_key"
    private val SYMMETRIC_KEY = "symmetric_key"
    private val ENV_TYPE = "env_type"  //0prod, 1-dev

    fun setAppId(context: Context, appId: String) {
        var sp = context.getSharedPreferences(SP_TTC_PAY, Context.MODE_PRIVATE)
        sp.edit().putString(APP_ID, appId).apply()
    }

    @JvmStatic
    fun getAppId(context: Context): String {
        return context.getSharedPreferences(SP_TTC_PAY, Context.MODE_PRIVATE).getString(APP_ID, "")
    }


    fun setUserId(context: Context, userId: String) {
        var sp = context.getSharedPreferences(SP_TTC_PAY, Context.MODE_PRIVATE)
        sp.edit().putString(USER_ID, userId).apply()
    }

    fun getUserId(context: Context): String {
        return context.getSharedPreferences(SP_TTC_PAY, Context.MODE_PRIVATE).getString(USER_ID, "android_ttc_pay")
    }

    fun setTTCPublicKey(context: Context, publicKey: String) {
        var sp = context.getSharedPreferences(SP_TTC_PAY, Context.MODE_PRIVATE)
        sp.edit().putString(TTC_PUBLIC_KEY, publicKey).apply()
    }

    fun getTTCPublicKey(context: Context): String {
        return context.getSharedPreferences(SP_TTC_PAY, Context.MODE_PRIVATE).getString(TTC_PUBLIC_KEY, "")
    }

    fun setSymmetricKey(context: Context, symmetricKey: String) {
        var sp = context.getSharedPreferences(SP_TTC_PAY, Context.MODE_PRIVATE)
        sp.edit().putString(SYMMETRIC_KEY, symmetricKey).apply()
    }

    @JvmStatic
    fun getSymmetricKey(context: Context): String {
        return context.getSharedPreferences(SP_TTC_PAY, Context.MODE_PRIVATE).getString(SYMMETRIC_KEY, "")
    }

    fun setEnvType(context: Context, isProd:Boolean){
        var sp = context.getSharedPreferences(SP_TTC_PAY, Context.MODE_PRIVATE)
        sp.edit().putBoolean(ENV_TYPE, isProd).apply()
    }

    fun isEnvProd(context: Context):Boolean{
        return context.getSharedPreferences(SP_TTC_PAY, Context.MODE_PRIVATE).getBoolean(ENV_TYPE, true)
    }

}