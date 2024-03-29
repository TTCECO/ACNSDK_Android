# MAROPay教程

## 准备工作

下载 [MAROPay SDK](https://github.com/TTCECO/TTCSDK_Android/releases)

## 快速集成
将sdk放入libs目录下，在build.gradle中添加依赖，sdk的名称请根据相应的版本号填写。

```
android {
	repositories {
    	flatDir {
      	  dirs 'libs'
    	}
	}
}

dependencies {
  implementation(name: 'maro_pay_xxx', ext: 'aar')
}

```
## 初始化接口
在Application中初始化，USER\_ID为Dapp的user_id，APP\_ID、MAROPay\_PUBLIC\_KEY和MAROPay\_SECRET\_KEY的值在申请时确定；

```
@Override
public void onCreate() {
    super.onCreate();
    MaroPay.init(applicationContext, "USER_ID", "APP_ID", "MaroPay_PUBLIC_KEY", "MaroPay_SECRET_KEY")
}
```
## 查询汇率
查询当时MARO与各种法币的汇率，第二个参数为法币类型，已经在Currency.kt文件中定义；获取成功后，回调中返回1MARO等于多少该法币。

```
MaroPay.getExchangeRate(this, Currency.dollar, object : ExchangeCallback {
    override fun done(maroPrice: String) {
        //Utils.toast(activity, maroPrice)
    }

    override fun error(errorBean: ErrorBean) {
        //Utils.toast(activity, errorBean.getErrorMsg())
    }
})
```
## 支付
在调用支付接口前，Dapp需要得到签名（建议服务器签名，不要放在客户端），赋值给payInfo。sig签名规则详见最后；
第二个参数为Dapp需要传入的参数，该类的字段都需要赋值；支付的时候，首先请求MARO服务器生成订单，生成订单后，返回MARO的订单号maroOrderId，该字段用于后续查询订单的信息；

```
MaroPay.pay(activity, payInfo, object : PayCallback {
    override fun createOrderOver(maroOrderId: String) {
         //Utils.toast(activity, maroOrderId)
    }

    override fun error(errorBean: ErrorBean) {
        //Utils.toast(activity, errorBean.getErrorMsg())
    }

    override fun payFinish(maroOrderId: String, txHash: String, orderState: Int) {
    }
})
```
## 查询订单
根据MARO的订单号，查询该订单的详细信息

```
MaroPay.getOrderDetail(activity, maroOrderId, object : GetOrderDetailCallback {
    override fun done(orderInfo: OrderInfo) {
        //display(orderInfo)
    }

    override fun error(errorBean: ErrorBean) {
       // Utils.toast(activity, errorBean.getErrorMsg())
    }
})
```

##签名规则
将PayInfo.kt中所有的字段赋予相应的值，其中partnerAddress、payType和sellerDefinedPage使用默认值，请勿修改。将所有的字段按照key排序，然后转化成key=value，中间用&连接，用sha1withRsa签名（注意是签名不是加密）。

