# MaroPay Tutorials

## Basics

Download [MaroPay SDK](https://github.com/TTCECO/TTCSDK_Android/releases)

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
  implementation(name: 'maro_pay_xxx', ext: 'aar')
}

```
## Initiate
Please invoke it in Application. APP\_ID, MaroPay\_PUBLIC\_KEY and MaroPay\_SECRET\_KEY are given when you apply.

```
@Override
public void onCreate() {
    super.onCreate();
    MaroPay.init(applicationContext, "USER_ID", "APP_ID", "MaroPay_PUBLIC_KEY", "MaroPay_SECRET_KEY")
}
```
## Query exchange rate
Query the rate of MARO to legal tender. The second parameter is legal tender type which are defined in Currency.kt.

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
## Payment
Before invoking this, dapp should get sign from server. The second parameter is order object. The result of success callback is MARO order id.

```
payInfo.signature = Utils.getSignFromServer()
MaroPay.pay(activity, payInfo, object : PayCallback {
    override fun createMaroOrderOver(maroOrderId: String) {
         //Utils.toast(activity, maroOrderId)
    }

    override fun error(errorBean: ErrorBean) {
        //Utils.toast(activity, errorBean.getErrorMsg())
    }

    override fun payFinish(maroOrderId: String, txHash: String, orderState: Int) {
    }
})
```
## Query order detail
Query order detail by MARO order id.

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

