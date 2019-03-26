# TTC SDK Tutorials

## Basics

Download [SDK](https://github.com/TTCECO/TTCSDK_Android/releases)

## Integration 
Put sdk in libs directory, add dependencies in build.gradle and replace “ttc\_sdk\_xxx” and "ttc\_sdk\_biz\_xxx" with the real name; Need Java8 support. If your project don't use kotlin, please add kotlin dependency.

```
android {
	repositories {
    	flatDir {
      	  dirs 'libs'
    	}
	}
}

dependencies {
  implementation 'org.web3j:core:4.2.0-android'
  implementation 'com.google.protobuf.nano:protobuf-javanano:3.0.0-alpha-5'
  implementation(name: 'ttc_sdk_xxx', ext: 'aar')
  implementation(name: 'ttc_sdk_biz_xxx', ext: 'aar')
  
  //kotlin
  implementation "org.jetbrains.kotlin:kotlin-stdlib-jdk7:$kotlin_version"
    
  //add it if use ad function
  implementation 'com.google.android.gms:play-services-ads:17.2.0'
}

``` 
Add the following codes in AndroidManifest.xml, and replace "packageName" & "appID" & "secretKey" with yours; If use ad function, add "com.google.android.gms.ads.APPLICATION_ID". During development, please use test Ids. 
  
test ID：  
com.google.android.gms.ads.APPLICATION_ID： ca-app-pub-3940256099942544~3347511713  

| AD format | unit Id
| ---- | ----           
| Banner | ca-app-pub-3940256099942544/6300978111    
| Interstitial | ca-app-pub-3940256099942544/1033173712    
| Interstitial Video	| ca-app-pub-3940256099942544/8691691433    
| Rewarded Video | ca-app-pub-3940256099942544/5224354917   

```
<activity-alias
    android:name="bind"
    android:targetActivity="com.ttc.behavior.ui.BindActivity">

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
    
    <!--add it if use ad function -->
<meta-data
    android:name="com.google.android.gms.ads.APPLICATION_ID"
    android:value="ca-app-pub-3940256099942544~3347511713"/>
```

## Initiate 
Initiate it in Application.java. During dev, please set envProd is false. If you  don't set it, it is ture default.

```
@Override
public void onCreate() {
    super.onCreate();
    TTCAgent.init(this, adMobAppId);
    
     //during dev, set false. The default value is true.
    TTCAgent.setEnvProd(false);
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

## Upload user behaviors  
The actionType must be greater than 100. Extra is optional.

```
TTCAgent.onEvent(int actionType, String extra)
```

## Upload user login
If need user login info, you can add it in parent Activty

```
TTCAgent.onEvent(CommonType.OPEN_DAPP, "");
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
Error code [document](./error_code/)



## Show banner ad
It is completed in TTCAdsBanner. Method init() must be invoked first, adSize is defined in TTCAdSize. Method setBannerCallback() is optional.

```
var banner = TTCAdsBanner()
var bannerView = banner.init(activity,  Utils.getBannerUnitId(), TTCAdSize.BANNER)
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
