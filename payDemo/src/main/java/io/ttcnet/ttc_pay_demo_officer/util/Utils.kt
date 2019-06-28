package io.ttcnet.ttc_pay_demo_officer.util

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.util.Base64
import android.util.Log
import android.widget.Toast
import io.ttcnet.pay.model.PayInfo
import io.ttcnet.ttc_pay_demo_officer.BuildConfig
import io.ttcnet.ttc_pay_demo_officer.R
import io.ttcnet.ttc_pay_demo_officer.constant.Constant
import java.security.KeyFactory
import java.security.Signature
import java.security.spec.PKCS8EncodedKeySpec
import java.text.DecimalFormat
import java.util.*

/**
 * Created by lwq on 2018/12/18.
 */
object Utils {

    fun getCurrencySymbol(type: Int): String {
        var symbol = ""
        when (type) {
            Constant.PRICE_TYPE_DOLLAR -> symbol = "$"
        }
        return symbol
    }

    //e.g.: 12,567.67
    fun displayDecimal(numble: Double): String {
        var format = DecimalFormat("#,##0.###")
        return format.format(numble)
    }

    fun showDialog(context: Context?, tipsResId: Int, confirmListener: DialogInterface.OnClickListener): AlertDialog {
        var alertDialog = AlertDialog.Builder(context)
            .setMessage(tipsResId)
            .setCancelable(true)
            .setPositiveButton(R.string.confirm, confirmListener)
            .setNegativeButton(R.string.cancel, null).create()

        alertDialog.show()

        return alertDialog
    }

    fun toast(context: Context?, content: String?) {
        Toast.makeText(context, content, Toast.LENGTH_SHORT).show()
    }

    fun getSignFromServer(
        context: Context,
        payInfo: PayInfo,
        appId: String
    ): String {
        val map = HashMap<String, Any>()

        map["outTradeNo"] = payInfo.merchantOrderNo
        map["description"] = payInfo.orderContent
        map["totalFee"] = payInfo.totalTTCWei
        map["partnerAddress"] = payInfo.partnerAddress
        map["createTime"] = payInfo.orderCreateTime
        map["expireTime"] = payInfo.orderExpireTime
        map["payType"] = payInfo.payType
        map["sellerDefinedPage"] = payInfo.sellerDefinedPage
        map["appId"] = appId

        var sb = StringBuilder()
        var sortedMap = map.toSortedMap()
        for (temp in sortedMap) {
            if (!sb.isEmpty()) {
                sb.append("&")
            }
            sb.append(temp.key).append("=").append(temp.value)
        }

        Log.d("lwq", sb.toString())
        return rsaSignSha1(context, sb.toString())
    }

    private fun rsaSignSha1(context: Context, content: String): String {

        var priPKCS8 = PKCS8EncodedKeySpec(Base64.decode(getPkcs8Key(), Base64.DEFAULT));

        var keyf = KeyFactory.getInstance("RSA");
        var priKey = keyf.generatePrivate(priPKCS8);
        var signature = Signature.getInstance("SHA1withRSA");

        signature.initSign(priKey);
        signature.update(content.toByteArray());

        var signed = signature.sign();

        return Base64.encodeToString(signed, Base64.NO_WRAP);   //no_wrap or default?
    }


    private fun getPkcs8Key(): String {
        if (BuildConfig.BUILD_TYPE == "release") {
            return "MIICdQIBADANBgkqhkiG9w0BAQEFAASCAl8wggJbAgEAAoGBAL7FIZZ45Q2A2ame\n" +
                    "ztfMdXr8Bip48NcbdhfYeummefDM6UZPg3EcmEu3TojxiNp0dQaK10M12IZnLA6Z\n" +
                    "T13a5oclEupSh6WuFrRcUkuhKmOvEjwnZGFJnQfpnmWQGdkgN1siYdzDof1TpVkp\n" +
                    "HR+/qjd8pIZvh62pDTqqivGYJktVAgMBAAECgYBED+UZ4GwoUy5VPBmkGIhZpo00\n" +
                    "ng2fUWbivxOrRQAAj7syoK/OrVsQuIfGiBZBiocuHF8M1as42Jp5Yu1UGQNh2l0/\n" +
                    "o9Q7Er2Yr8EVDRWjc9Y8c3Iu045HC2mVQpKQjAFQtEuI7LKIszGAfT6SV2QioILV\n" +
                    "2ZOtqve3Sv+Kr2DXCQJBAOaMlbuGDZKWe+iZ6C2x4AB2KF7WfKLIyb6EFzsI+cK7\n" +
                    "3CB4wxePG5FPs3xEyJsUzaui1k2H18vbu9HlsFs7A0MCQQDT1F84WJqMnUjLYJJr\n" +
                    "L6J5qmqyYys1D6Sxw3ntibX5Jqidy0NRbOLKkNEca+j6g5Ei/tv9BSopP79pyvcb\n" +
                    "HXGHAkBn/NEsm/JTQ/zvlTvcIHbgvmrHHAdrhRU6EWpI/mtpIkLPgqi8X5gXmtNi\n" +
                    "IcQ57tA1smTm43a6RIoayncZLyzxAkBBrosr7IVXbnmg3jvoUmfds9LLp2ZBWK1n\n" +
                    "lUYcOmQbcjo8W0dvWwTjbI36cxdVfjAlmsLZrJ1LFwIiKpX+aV3HAkB6FxLCpOfi\n" +
                    "kLLZDL7EDQkxdCpOEqCUOKhRknumqhnZxKiN7oZOH++jAo0gNcWEvrJ9/uoUrCKS\n" +
                    "5HAl4aZfLkGd"
        } else {
            return "MIICdgIBADANBgkqhkiG9w0BAQEFAASCAmAwggJcAgEAAoGBAOb5RYqtnX6A7f7x\n" +
                    "QzPgQtH6+wrFHF8frWEGWEh2xOqkJ7LGuDIo59p0QhNrao0rm4JdHpn/xPEwqeqG\n" +
                    "iMTQWCcb2ffonbLPdoAmUb1xDqRj7UPx1AmgBxMEciMg6P7oRhwKx93s/wKGQqNF\n" +
                    "MXnvftrMWimZ6LLLbwF8Szj9vIcTAgMBAAECgYBaD4RQKJYqh3dFWymLBrzHR4ev\n" +
                    "kWAYry9Zj7HIv5bUryFTkiysUkrlYUOKcu3fEcvXLxwJjDNsoD8A1Whq5ZoZGB4G\n" +
                    "9DKCunxciMvX92bw7p2A0jaSvlDFnS4Wzozwn4ruCezDtMi2lvQGfPFMO0Kw5eij\n" +
                    "wu1mv9HeO3/06vhusQJBAPUB8OOiOD+O81zpB391s4DHkLp+yaluqEjBupfYAaGb\n" +
                    "q/jh3s/hDX22eZuYrQ6OD9f22x6M0epA87RRp3FMJJcCQQDxViSwxsE4GIca3yLJ\n" +
                    "b6nYFUGTJN+UyNMZ5r8BH+eIOmFvYNxqeMlg1wr6SLRjmy1n0eiaZy6WR3V+a7rA\n" +
                    "3BTlAkALr7oDwZsZPQJSrjLTW5PiUqKOormPwV15ivQRhhYd1UUQrAVquPthwbBv\n" +
                    "QYsPpKsQzA+Ll3/zwoFdWn+4Ib+lAkA9k+9Us8IFYCzI7Hphz34Uxoeu1c++lOdY\n" +
                    "Sood7VgUaGEIHDzhZeRsMzJ33ik46RVS0jp5ey5l5eHS2gYSw5UJAkEA4kSNTUnB\n" +
                    "VrQ+KXT7/KJAze6zY3thHTKT8s9Q4ryX3VEkr1LFTTU3wbfoMpYFAzIrl1bRayET\n" +
                    "B7POIWeUIE64nw==\n"
        }
    }

    fun getAppId(): String {
        if (BuildConfig.BUILD_TYPE == "release") {
            return "TTCMallDemo"
        } else {
            return "MeiTuan"
        }
    }

    fun getTTCPublicKey(context: Context): String {
        if (BuildConfig.BUILD_TYPE == "release") {
            return "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDEBR/CV1B37hdOkNDUUaUEmWFvRFZkTue4x9XWCH/kfW+9BHtOTa/Zy" +
                    "+dwYxEvZXTqyfAAZyFU8yvkAJ4oSYy/AZ5Hw0ONli90KVrmfVW+V1SoRVhOmLfQGRf+bD3JD+BgEydwzT4+c" +
                    "+Sjc7GAEbghlhYNhkPCzI47MH2hPR/uOwIDAQAB"
        } else {
            return "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDb7Nm+27mtMHbynhtmcCCXVjkh\n" +
                    "X2A5XfTHDktIzCiHKoVQLwNfdCtb8SN+bwsG4zXkAY0HFxmIOOd23gtIsE0PKcD4\n" +
                    "nvu3wGXcSiIk5+46iOxtsNiE9/xsFLAWtdUj0FTp8H2LGYRC2tmas+cNov1U52vn\n" +
                    "ebTeEF+GuqdPJq/zgQIDAQAB\n"
        }
    }

    fun getSymmetricKey(context: Context): String {
        if (BuildConfig.BUILD_TYPE == "release") {
            return "6d1b957a8df22a935987b7e761b3d368"
        } else {
            return "f0ec9e54bded7d60838f8d39c12e1db2"
        }
    }

}