# TTCPay Tutorials

## Basics

Download [TTCPay SDK](https://github.com/TTCECO/TTCSDK_Android/releases)

## Integration
Add sdk in libs directory, and add dependency in build.gradle.

```
android {
	repositories {
    	flatDir {
      	  dirs 'libs'
    	}
	}
}

dependencies {
  implementation(name: 'ttc_pay_xxx', ext: 'aar')
}

```
## Initiate
Please invoke it in Application. APP\_ID, TTCPay\_PUBLIC\_KEY and TTCPay\_SECRET\_KEY are given when you apply.

```
@Override
public void onCreate() {
    super.onCreate();
    TTCPay.init(applicationContext, "USER_ID", "APP_ID", "TTCPay_PUBLIC_KEY", "TTCPay_SECRET_KEY")
}
```
## Query exchange rate
Query the rate of TTC to legal tender. The second parameter is legal tender type which are defined in Currency.kt.

```
TTCPay.getExchangeRate(this, Currency.dollar, object : ExchangeCallback {
    override fun done(ttcPrice: String) {
        //Utils.toast(activity, ttcPrice)
    }

    override fun error(errorBean: ErrorBean) {
        //Utils.toast(activity, errorBean.getErrorMsg())
    }
})
```
## Payment
Before invoking this, dapp should get sign from server. The second parameter is order object. The result of success callback is TTC order id.

```
payInfo.signature = Utils.getSignFromServer()
TTCPay.pay(activity, payInfo, object : PayCallback {
    override fun createTTCOrderOver(ttcOrderId: String) {
         //Utils.toast(activity, ttcOrderId)
    }

    override fun error(errorBean: ErrorBean) {
        //Utils.toast(activity, errorBean.getErrorMsg())
    }

    override fun payFinish(ttcOrderId: String, txHash: String, orderState: Int) {
    }
})
```
## Query order detail
Query order detail by TTC order id.

```
TTCPay.getOrderDetail(activity, ttcOrderId, object : GetOrderDetailCallback {
    override fun done(orderInfo: OrderInfo) {
        //display(orderInfo)
    }

    override fun error(errorBean: ErrorBean) {
       // Utils.toast(activity, errorBean.getErrorMsg())
    }
})
```

