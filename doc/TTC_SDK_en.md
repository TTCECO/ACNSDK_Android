# TTC SDK Tutorials

## Basics

Download [SDK](https://github.com/TTCECO/TTCSDK_Android)

## Integration 
Put sdk in libs directory, add dependencies in build.gradle and replace “ttc\_sdk\_xxx” and "ttc\_sdk\_biz\_xxx" with the real name;

```
android {
	repositories {
    	flatDir {
      	  dirs 'libs'
    	}
	}
}

dependencies {
  implementation 'org.web3j:core:3.3.1-android'
  implementation 'com.google.protobuf.nano:protobuf-javanano:3.0.0-alpha-5'
  implementation 'com.google.android.gms:play-services-ads:17.1.2'
  implementation(name: 'ttc_sdk_xxx', ext: 'aar')
  implementation(name: 'ttc_sdk_biz_xxx', ext: 'aar')
}

``` 
Add the following codes in AndroidManifest.xml, and replace "packageName" & "appID" & "seceretKey" with yours;

```
<activity-alias
    android:name="bind"
    android:targetActivity="com.ttc.sdk.ui.BindActivity">

    <intent-filter>
        <action android:name="com.ttc.wallet.BINDACTION" />
        <category android:name="android.intent.category.DEFAULT"/>
        <data
            android:scheme="packageName" />
    </intent-filter>

</activity-alias>

<meta-data
    android:name="TTC_APP_ID"
    android:value="appID"/>
<meta-data
    android:name="TTC_APP_SECRET_KEY"
    android:value="secretKey"/>
<meta-data
    android:name="com.google.android.gms.ads.APPLICATION_ID"
    android:value="ca-app-pub-3081086010287406~9085005576"/>
```

## Initiate 
Initiate it in Application.java. If not used advertise, ***adMobAppId*** can be empty.  

```
@Override
public void onCreate() {
    super.onCreate();
    TTCAgent.init(this, adMobAppId);
}
```
## Register
If registeration succeeds, the callback method's parameter contains all of the user's information; 

```
TTCAgent.register(String userId, IManager.UserInfoCallback callback)
```  
e.g.：

```
TTCAgent.register("123", new IManager.UserInfoCallback() {
           @Override
           public void success(Map<String, String> map) {
               //normal attributes are defined in UserAttr.java
                String address = map.get(UserAttr.ADDRESS);
                String userId = map.get(UserAttr.USER_ID);
                ...
           }

           @Override
           public void error(String msg) {
           }
       });

```
## Log out
When the user logs out, make sure to  call the method to clear local information; 

```
TTCAgent.unregister()
```
## Update user information  
The ***info*** is what the user update. The common keys are defined in UserAttr.java. The callback method's will contain the user information if successful;  
 
```
TTCAgent.updateUserInfo(Map<String, String> info, IManager.UserInfoCallback callback)
```
## Unbind
It is used to unbind the TTC Wallet.

```
TTCAgent.unbindApp()
```
## Get account balance
The account balance is the balance in the dapp that is not synchronized to the TTC Wallet.

```
TTCAgent.getAppBalance(IManager.BalanceCallback callback)
```

## Get wallet balance

```
TTCAgent.getWalletBalance(IManager.BalanceCallback callback)
```

## Upload user behaviors  
The ___actionType__ must be greater than 100. The ___extra___ must be a json structure string.

```
TTCAgent.onEvent(int actionType, String extra)
```
## Configure 
Configure log & sdk function switcher

```
TTCAgent.configure(TTCConfigure configure)
```

e.g.：

```
TTCConfigure.Builder builder = new TTCConfigure.Builder();
builder.logEnabled(false);
builder.serverEnabled(true);
TTCAgent.configure(builder.build());
```


## Show banner ad
It is completed in TTCAdsBanner. Method init() must be invoked first, adSize is defined in TTCAdSize. Method setBannerCallback() is optional.

```
var banner = TTCAdsBanner()
var bannerView = banner.init(activity,  BuildConfig.bannerUnitId, TTCAdSize.BANNER)
ads_banner_container_fl.addView(bannerView)

banner.setBannerCallback(object : TTCAdsCallback() {

    override fun onAdImpression() {
        super.onAdImpression()
    }

    override fun onAdLeftApplication() {
        super.onAdLeftApplication()
        Toast.makeText(activity, "banner left", Toast.LENGTH_SHORT).show()
    }

    override fun onAdClicked() {
        super.onAdClicked()
    }

    override fun onAdFailedToLoad(p0: Int) {
        super.onAdFailedToLoad(p0)
        Toast.makeText(activity, "banner failed:" + p0, Toast.LENGTH_SHORT).show()
    }

    override fun onAdClosed() {
        super.onAdClosed()
    }

    override fun onAdOpened() {
        super.onAdOpened()
    }

    override fun onAdLoaded() {
        super.onAdLoaded()
        Toast.makeText(activity, "banner adloaded", Toast.LENGTH_SHORT).show()
    }
});
```
## show interstitial ad
Create TTCAdsInterstitial object, and init it with unit id. After requesting ad, then you can show it. 

```
interstitial = TTCAdsInterstitial()
interstitial.init(activity, BuildConfig.interstitialUnitId)

interstitial.setAdsCallback(object : TTCAdsCallback() {

    override fun onAdImpression() {
        super.onAdImpression()
        Toast.makeText(activity, "interstitial impression", Toast.LENGTH_SHORT).show()
    }

    override fun onAdLeftApplication() {
        super.onAdLeftApplication()
        Toast.makeText(activity, "interstitial left", Toast.LENGTH_SHORT).show()
    }

    override fun onAdClicked() {
        super.onAdClicked()
    }

    override fun onAdFailedToLoad(p0: Int) {
        super.onAdFailedToLoad(p0)
        Toast.makeText(activity, "interstitial failed loaded:" + p0, Toast.LENGTH_SHORT).show()
    }

    override fun onAdClosed() {
        super.onAdClosed()
    }

    override fun onAdOpened() {
        super.onAdOpened()
    }

    override fun onAdLoaded() {
        super.onAdLoaded()
        interstitial.show()  //加载完成，显示
        Toast.makeText(activity, "interstitial adLoaded", Toast.LENGTH_SHORT).show() 
    }
})
```

## Show rewarded ad
Create TTCAdsRewardVideo object, and init it with unit id. After requesting ad, then you can show it. Also, you can pause and resume it.

```
rewardVideo = TTCAdsRewardVideo()
rewardVideo.init(activity,  BuildConfig.rewardUnitId)
rewardVideo.setRewardedCallback(object : TTCRewardCallback() {
    override fun onRewardedVideoAdClosed() {
        super.onRewardedVideoAdClosed()
    }

    override fun onRewardedVideoAdLeftApplication() {
        super.onRewardedVideoAdLeftApplication()
        Toast.makeText(activity, "reward left", Toast.LENGTH_SHORT).show()
    }

    override fun onRewardedVideoAdLoaded() {
        super.onRewardedVideoAdLoaded()
        rewardVideo.show()
        Toast.makeText(activity, "reward loaded", Toast.LENGTH_SHORT).show()
    }

    override fun onRewardedVideoAdOpened() {
        super.onRewardedVideoAdOpened()
    }

    override fun onRewardedVideoCompleted() {
        super.onRewardedVideoCompleted()
        Toast.makeText(activity, "reward complete", Toast.LENGTH_SHORT).show()
    }

    override fun onRewarded(type: String?, amount: Int?) {
        super.onRewarded(type, amount)
    }

    override fun onRewardedVideoStarted() {
        super.onRewardedVideoStarted()
    }

    override fun onRewardedVideoAdFailedToLoad(p0: Int) {
        super.onRewardedVideoAdFailedToLoad(p0)
        Toast.makeText(activity, "reward failed loaded:" + p0, Toast.LENGTH_SHORT).show()
    }
})


override fun onResume() {
    super.onResume()
    rewardVideo.resume(activity)
}

override fun onPause() {
    super.onPause()
    rewardVideo.pause(activity)
}

```

## Get SDK information

```
TTCAgent.getVersionName()
TTCAgent.getVersionCode()
```

## Minify app
If you minify your app, please add the following codes:

```
-keep class org.web3j.** {*;}
-keep class com.fasterxml.**{*;}
-keep class okhttp3.**{*;}

-dontwarn  com.fasterxml.**
-dontwarn  org.w3c.dom.**
-dontwarn  okio.**
-dontwarn  rx.**
-dontwarn  javax.**
-dontwarn  org.slf4j.**
-dontwarn sun.misc.**
```

## Error code
Error code [document](SDK_error_code_en.md)