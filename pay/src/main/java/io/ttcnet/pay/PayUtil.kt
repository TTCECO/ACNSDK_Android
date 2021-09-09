package io.ttcnet.pay

import io.ttcnet.pay.util.Util
import java.math.BigDecimal
import java.math.RoundingMode

/**
 * Created by lwq on 2018/12/19.
 */
object PayUtil {

    fun ttc2Wei(ttcCount: String): String {
        try {
            return BigDecimal(ttcCount).multiply(BigDecimal(Util.ONE_18_ZERO)).toBigInteger()
                .toString()
        } catch (e: NumberFormatException) {
            e.printStackTrace()
        }
        return ""
    }

    fun legalTender2TTC(ttcPrice: String, totalLegalTender: Double): String {
        try {
            return BigDecimal(totalLegalTender).divide(
                BigDecimal(ttcPrice),
                6,
                RoundingMode.HALF_UP
            ).toString()
        } catch (e: java.lang.NumberFormatException) {
            e.printStackTrace()
        }
        return ""
    }
}