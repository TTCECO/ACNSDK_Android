# ACN SDK Tutorials

## Basics

Download [SDK](https://github.com/TTCECO/ACNSDK_Android/releases)

## Integration 
Put sdk in libs directory, add dependencies in build.gradle and replace “acn\_sdk\_xxx” and "acn\_sdk\_biz\_xxx" with the real name; Need Java8 support. If your project don't use kotlin, please add kotlin dependency.

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
  implementation(name: 'acn_sdk_xxx', ext: 'aar')
  implementation(name: 'acn_sdk_biz_xxx', ext: 'aar')
  
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
    android:targetActivity="com.acn.behavior.ui.BindActivity">

    <intent-filter>
        <action android:name="com.acn.wallet.BINDACTION" />
        <category android:name="android.intent.category.DEFAULT"/>
        <data
            android:scheme="packageName" />
    </intent-filter>

</activity-alias>

<meta-data
    android:name="ACN_DAPP_ID"
    android:value="dappID"/>
<meta-data
    android:name="ACN_DAPP_SECRET_KEY"
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
    ACNAgent.init(this, adMobAppId);
    
     //during dev, set false. The default value is true.
    ACNAgent.setEnvProd(false);
}
```
## Register
If registeration succeeds, the callback method's parameter contains all of the user's information; 

```
ACNAgent.register(String userId, IManager.UserInfoCallback callback)
```  
e.g.：

```
ACNAgent.register("123", new IManager.UserInfoCallback() {
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
actionType: Dapp can define it.  
extra: optional.

```
ACNAgent.onEvent(int actionType, String extra)
```

## Upload user login
If need user login info, you can add it in parent Activty

```
ACNAgent.onEvent(CommonType.OPEN_DAPP, "");
```


## Log out
When the user logs out, make sure to  call the method to clear local information; 

```
ACNAgent.unregister()
```
## Update user information  
The ***info*** is what the user update. The common keys are defined in UserAttr.java. The callback method's will contain the user information if successful;  
 
```
ACNAgent.updateUserInfo(Map<String, String> info, IManager.UserInfoCallback callback)
```

## Bind
The mothod result can received onActivityResult(int requestCode, int resultCode, @Nullable Intent data):  
resultCode = RESULT\_OK  
The bind result:  
isBindSuc = data.getBooleanExtra(ACNKey.BIND\_STATE, false);     
if has bound reward, return the count.  
reward = data.getIntExtra(ACNKey.BIND\_REWARD, 0);  
The symbol of reward Token.  
rewardSymbol = data.getStringExtra(ACNKey.BIND\_REWARD\_SYMBOL);  
If bind fail, return the error message.  
errMsg = data.getStringExtra(ACNKey.ERROR\_MSG);  

```
bindApp(Activity activity, String appIconUrl, int reqCode)
```


## Get bound wallet address
If boud, return wallet address. otherwise, return empty.

```
String getBoundWalletAddress()
```

## Unbind
It is used to unbind the TTC Connect. 

```
ACNAgent.unbindApp(IManager.UnbindCallback callback)
```
## Get account balance
The account balance is the balance in the dapp that is not synchronized to the TTC Connect.

```
ACNAgent.getAppBalance(IManager.BalanceCallback callback)
```

## Get wallet balance

```
ACNAgent.getWalletBalance(IManager.BalanceCallback callback)
```


## Configure 
Configure log & sdk function switcher

```
ACNAgent.configure(ACNConfigure configure)
```

e.g.：

```
ACNConfigure.Builder builder = new ACNConfigure.Builder();
builder.logEnabled(false);
builder.serverEnabled(true);
ACNAgent.configure(builder.build());
```

## Get SDK information

```
ACNAgent.getVersionName()
ACNAgent.getVersionCode()
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
It is completed in ACNAdsBanner. Method init() must be invoked first, adSize is defined in ACNAdSize. Method setBannerCallback() is optional.

```
var banner = ACNAdsBanner()
var bannerView = banner.init(activity,  Utils.getBannerUnitId(), ACNAdSize.BANNER)
ads_banner_container_fl.addView(bannerView)

banner.setBannerCallback(object : ACNAdsCallback() {

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
Create ACNAdsInterstitial object, and init it with unit id. After requesting ad, then you can show it. 

```
interstitial = ACNAdsInterstitial()
interstitial.init(activity, BuildConfig.interstitialUnitId)

interstitial.setAdsCallback(object : ACNAdsCallback() {

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
Create ACNAdsRewardVideo object, and init it with unit id. After requesting ad, then you can show it. Also, you can pause and resume it.

```
rewardVideo = ACNAdsRewardVideo()
rewardVideo.init(activity,  BuildConfig.rewardUnitId)
rewardVideo.setRewardedCallback(object : ACNRewardCallback() {
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
