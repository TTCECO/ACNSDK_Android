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
        map["partnerAddress"] = ""
        map["createTime"] = payInfo.orderCreateTime
        map["expireTime"] = payInfo.orderExpireTime
        map["payType"] = 1
        map["sellerDefinedPage"] = 0
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
            return "MIICeAIBADANBgkqhkiG9w0BAQEFAASCAmIwggJeAgEAAoGBALN7iT8Y7P4CM3Tn\n" +
                    "cfMtD0r1sx6y0FSgEXhnu5UvEyGP5uj6R4Ijtf4sj1w+MjQhaY5Az6Bjn+qAGdvk\n" +
                    "Srir00BE+H9i3I1AaK4tW2R2X7eUFMcZhjt6doFcb+mwLm0W779H0ZydNAvyoXU/\n" +
                    "Mqnsy6bX0tdkr+E1NADR6ED6HwmRAgMBAAECgYEAqy85YPf6Ge6pe2fAFCq2+kzL\n" +
                    "sYzlDMG6x/dQP2KtwYYJu83yZBqBW1LOzTD5GyV/EtGX9ibvAG97unHMHsSrb3kE\n" +
                    "mbqQz2K5nqFjW5UAjWRXG6Ckc7mL7XTo1GdUadcATbJ19WnD+CxIz9wmX4vxGeUT\n" +
                    "d/KpqCKg8yitsUTTMw0CQQDc7KGcbNROK51+80jvmkrybS5muIBDvb86IuF+UMxJ\n" +
                    "q0EiCkLwGCXSKSiMKHmI+eOdVAIJV/5B5Fps5V3EC1kvAkEAz/qF0ZDeLCp1KUf5\n" +
                    "Wg42ePN9t9ICd84tkaMT+ymPC8PR/hHbXeMp5Qw63yO/Fu5e4gay5XmpzNIlpxsR\n" +
                    "XamZPwJBAMaFUNgqp0CzSqyIW3MLd+uOltKxWnkE6fCI5Y5HdruAS2AWTn5HhLzs\n" +
                    "RUZlHIks3A9FLStOSejWFTejiHWhKPsCQCrvA5r8dfDWHvOTqRT/JB2Z3lUJJHtI\n" +
                    "871B1gkeTipWlU4Gr6tVDrv651hTD8qTEMFGMr4OfJAMvbsealpUfY8CQQDEhhAC\n" +
                    "HjNqXBNu21kD3CS8aQP2etakY2i2DKyq40Iri1pgbGMboJrJsqMXP1Td2uq4k625\n" +
                    "vnncE7XSOEPT5lPE"
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
            return "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDHJkr/owAZT0aUa2yTkcb0g8agInoAMLHeliCBkYdKUyYmJqp7MWf4t7ZC72wYl2+9Tob7ll5IgrrzpqITBva6NYhPflv2kP7mT+GTOMay5f0CEUgJtAGOH4oSBwEilMwnSMnn+xgQNgXiMjn3Z3mosQd12DLvxeIumB50qcYJewIDAQAB"
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