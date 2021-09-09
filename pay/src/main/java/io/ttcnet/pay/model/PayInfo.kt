package io.ttcnet.pay.model

/**
 * Created by lwq on 2018/12/11.
 */
class PayInfo {
    companion object {
       const val PAY_TYPE_TTC = 0
        const val PAY_TYPE_ACN = 1
        const val PAY_TYPE_CLAY = 2
        const val PAY_TYPE_CUSD = 3
        const val PAY_TYPE_CCNY = 4
        const val PAY_TYPE_CKRW = 5
    }

    var signature = ""
    var merchantOrderNo = ""
    var orderContent = ""
    var totalTTCWei = ""   //the number of TTC, unit is wei
    var orderCreateTime = 0L
    var orderExpireTime = 0L
    var appId = ""
    var partnerAddress = ""
    var payType = PAY_TYPE_TTC   //见上面的companion object
    var sellerDefinedPage = 0
}