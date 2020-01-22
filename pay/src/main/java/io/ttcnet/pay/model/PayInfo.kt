package io.ttcnet.pay.model

/**
 * Created by lwq on 2018/12/11.
 */
class PayInfo {
    var signature = ""
    var merchantOrderNo = ""
    var orderContent = ""
    var totalTTCWei = ""   //the number of TTC, unit is wei
    var orderCreateTime: Long = 0
    var orderExpireTime: Long = 0
    var appId = ""
    var partnerAddress = ""
    var payType = 1  //1-TTC 2-acn
    var sellerDefinedPage = 0
}