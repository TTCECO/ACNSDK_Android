package io.ttcnet.pay.util

import android.app.Activity
import android.app.ActivityManager
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.text.TextUtils
import android.util.Log
import android.view.View
import eco.ttc.proto.nano.Sdk
import io.ttcnet.pay.R
import io.ttcnet.pay.model.ErrorBean
import io.ttcnet.pay.model.OrderInfo
import java.math.BigDecimal
import java.math.RoundingMode
import kotlin.random.Random

/**
 * Created by lwq on 2018/12/4.
 */
object Util {
    val ONE_18_ZERO = "1000000000000000000"  //1*10^18
    val TTC_CONNECT_PK_NAME = "com.ttc.wallet"
    val ACORN_BOX_PK_NAME = "eco.acorn"
    val TTC_CONNECT_DOWNLOAD_URL = "https://www.ttc.eco/TTCConnect/download"
    val ACORN_BOX_DOWNLOAD_URL = "https://acn.eco/acornbox/download"


    fun openTTCConnect(context: Context, orderId: String): Int {
        var openRes = 0
        var isInstalled = isAppInstalled(context, TTC_CONNECT_PK_NAME)
        var content =
            "order_id=$orderId&merchant_pk_name=${context.packageName}&random=${Random.nextInt()}"
        var intent = Intent(Intent.ACTION_VIEW, Uri.parse("ttc://pay?" + content))
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        if (isInstalled) {
            if (intent.resolveActivity(context.packageManager) != null) {
                context.startActivity(intent)
            } else {
                openRes = ErrorBean.TTC_CONENCT_VERSION_LOW
            }
        } else {
            openRes = ErrorBean.TTC_CONNECT_NOT_INSTALLED
        }
        return openRes
    }

    //调用之前，请先检查是否安装AcornBox；
    // return: 0-正常打开，1-未安装， 2-版本过低
    fun openAcornBox(activity: Activity, orderId: String): Int {
        var openRes = 0
        var isInstalled = isAppInstalled(activity, ACORN_BOX_PK_NAME)
        var content =
            "order_id=$orderId&merchant_pk_name=${activity.packageName}&random=${Random.nextInt()}"
        var intent = Intent(Intent.ACTION_VIEW, Uri.parse("acn://pay?" + content))
//        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)  //加了这句后，无法使用onActivityResult

        if (isInstalled) {
            if (intent.resolveActivity(activity.packageManager) != null) {
                activity.startActivityForResult(intent, 0)
            } else {
                openRes = ErrorBean.ACORN_BOX_VERSION_LOW
            }
        } else {
            openRes = ErrorBean.ACORN_BOX_NOT_INSTALLED
        }
        return openRes;
    }

    fun isTTCBackground(context: Context): Boolean {
        if (context is Activity) {
            var activity = context
            val manager = activity.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
            val runningAppProcesses = manager.runningAppProcesses
            for (process in runningAppProcesses) {
                return process.processName.startsWith(TTC_CONNECT_PK_NAME)
            }
        }
        return false
    }

    fun genRes23506DataKVString(data: Sdk.Response23506.Data?): String {
        val map = HashMap<String, Any>()
        data?.let {
            map["orderId"] = it.orderId
            map["qrcodeUrl"] = it.qrcodeUrl
        }
        return genKeyValueString(map);
    }


    fun genReq23506ParamKVString(param: Sdk.Request23506.Params?): String {
        val map = HashMap<String, Any>()
        param?.let {
            map["outTradeNo"] = it.outTradeNo
            map["description"] = it.description
            map["totalFee"] = it.totalFee
            map["partnerAddress"] = it.partnerAddress
            map["createTime"] = it.createTime
            map["expireTime"] = it.expireTime
            map["payType"] = it.payType
            map["sellerDefinedPage"] = it.sellerDefinedPage
            map["appId"] = it.appId
        }
        return genKeyValueString(map);
    }


    fun genRes23507DataKVString(data: Sdk.Response23507.Data?): String {
        var res = ""
        data?.let {
            val map = HashMap<String, Any>()
            map["value"] = it.value
            res = genKeyValueString(map)
        }
        return res
    }


    fun genRes23508DataKVString(data: Sdk.Response23508.Data?): String {
        val map = HashMap<String, Any>()
        data?.order?.let {
            map["orderId"] = it.orderId
            map["outTradeNo"] = it.outTradeNo
            map["description"] = it.description
            map["totalFee"] = it.totalFee
            map["partnerAddress"] = it.partnerAddress
            map["partnerName"] = it.partnerName
            map["createTime"] = it.createTime
            map["expireTime"] = it.expireTime
            map["state"] = it.state
            map["txHash"] = it.txHash
            map["payGasLimit"] = it.payGasLimit
            map["payGasPrice"] = it.payGasPrice
            map["tokenId"] = it.tokenId
        }
        return genKeyValueString(map);
    }


    private fun genKeyValueString(map: Map<String, Any>): String {
        var sb = StringBuilder()
        val sortedMap = map.toSortedMap()
        for (temp in sortedMap) {
            if (!sb.isEmpty()) {
                sb.append("&")
            }
            sb.append(temp.key).append("=").append(temp.value)
        }
        Log.d("lwq", sb.toString())
        return sb.toString()
    }

    fun checkInit(context: Context): ErrorBean? {
        var errorBean = ErrorBean()
        var appId = SPUtil.getAppId(context)
        if (TextUtils.isEmpty(appId)) {
            errorBean.setErrorId(ErrorBean.APP_ID_IS_EMPTY_ID)
            errorBean.setErrorMsg(ErrorBean.APP_ID_IS_EMPTY_MSG)
            return errorBean
        }
        var publicKey = SPUtil.getTTCPublicKey(context)
        if (TextUtils.isEmpty(publicKey)) {
            errorBean.setErrorId(ErrorBean.PUBLICK_KEY_IS_EMPTY_ID)
            errorBean.setErrorMsg(ErrorBean.PUBLIC_KEY_IS_EMPTY_MSG)
            return errorBean
        }
        var symmetricKey = SPUtil.getSymmetricKey(context)
        if (TextUtils.isEmpty(symmetricKey)) {
            errorBean.setErrorId(ErrorBean.SYMMETRIC_KEY_IS_EMPTY_ID)
            errorBean.setErrorMsg(ErrorBean.SYMMERRIC_KEY_IS_EMPTY_MSG)
            return errorBean
        }
        return null
    }

    fun order2OrderInfo(data: Sdk.Response23508.Data): OrderInfo {
        var orderInfo = OrderInfo()
        var order = data.order;
        orderInfo.ttcOrderId = order.orderId
        orderInfo.outOrderId = order.outTradeNo
        orderInfo.description = order.description
        orderInfo.totalTTC = wei2TTC(order.totalFee)
        orderInfo.partnerAddress = order.partnerAddress
        orderInfo.partnerName = order.partnerName
        orderInfo.createTime = order.createTime
        orderInfo.expireTime = order.expireTime
        orderInfo.state = order.state
        orderInfo.txHash = order.txHash
        orderInfo.payGasLimit = order.payGasLimit
        orderInfo.payGasPrice = order.payGasPrice
        orderInfo.tokenId = order.tokenId
        return orderInfo
    }


    fun showProgressDialog(activity: Activity, cancelable: Boolean): AlertDialog {
        val view = View.inflate(activity, R.layout.dialog_loading, null)
        var dialog = AlertDialog.Builder(activity).setCancelable(cancelable).setView(view).create()
        dialog.show()

        return dialog;
    }

    fun wei2TTC(ttcWei: String): String {
        try {
            return BigDecimal(ttcWei).divide(BigDecimal(ONE_18_ZERO), 6, RoundingMode.HALF_UP)
                .toString()
        } catch (e: NumberFormatException) {
            e.printStackTrace()
        }
        return ""
    }

    fun getServerUrl(context: Context): String {
        return if (SPUtil.isEnvProd(context)) {
            "http://sdk.ttcnet.io/v1/"
        } else {
            "http://sdk-ft.ttcnet.io/v1/"
        }
    }

    private fun isAppInstalled(context: Context, packageName: String): Boolean {
        var isInstalled = false
        val packageManager = context.packageManager
        val installedPackages = packageManager.getInstalledPackages(0)
        for (info in installedPackages) {
            if (info.packageName == packageName) {
                isInstalled = true
                break
            }
        }
        return isInstalled
    }

}