package io.ttcnet.pay.model

/**
 * Created by lwq on 2018/12/6.
 */
class OrderInfo {
    var ttcOrderId:String = ""
    var outOrderId :String = ""
    var description = ""
    var totalTTC = ""
    var partnerAddress = ""
    var partnerName = ""
    var createTime: Long = 0
    var expireTime: Long = 0
    var state: Int = 0
    var txHash = ""
    var payGasLimit = 0
    var payGasPrice = ""
    var tokenId = 0    //0:MARO, ...

}