# ACN SDK教程
## 准备工作

下载 [SDK](https://github.com/TTCECO/ACNSDK_Android/releases)

## 快速集成
将sdk放入libs目录下，在build.gradle中添加依赖，sdk的名称请根据相应的版本号填写。需要支持java8.
如果您的工程没有使用kotlin，请通过android studio添加kotlin依赖。

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
    
  //广告功能，需要添加此依赖
  implementation 'com.google.android.gms:play-services-ads:17.2.0'
}

```
在AndroidManifest中加入下面的配置， 将“应用包名”替换为您应用的包名，appID和secretKey替换为您之前申请的值，其它的请勿修改。  
如需接广告，开发期间adMobAppId、bannerUnitId、interstitialUnitId、rewardUnitId使用我们给定的测试Id，开发完成之后换成正式的Id；
  
测试的ID：  
com.google.android.gms.ads.APPLICATION_ID： ca-app-pub-3940256099942544~3347511713  

| 广告名称 | 单元ID
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
            android:scheme="应用包名" />
    </intent-filter>

</activity-alias>

<meta-data
    android:name="ACN_DAPP_ID"
    android:value="appID"/>
<meta-data
    android:name="ACN_DAPP_SECRET_KEY"
    android:value="secretKey"/>
    
    <!--接入广告，需要添加-->
<meta-data
    android:name="com.google.android.gms.ads.APPLICATION_ID"
    android:value="ca-app-pub-3940256099942544~3347511713"/>
```

## 初始化接口
在Application中初始化, 行为和广告都需要初始化;  
在开发和测试期间, ACNAgent.setEnvProd(false)；上线时，再设为true，默认是true；

```
@Override
public void onCreate() {
    super.onCreate();
    ACNAgent.init(this);
    
    //开发和测试期间请设为false；上线时，改为true;
    ACNAgent.setEnvProd(false);
}
```
## 注册用户接口

注册用户， 传入要注册的userId， 注册成功后返回用户所有信息；  

```
ACNAgent.register(String userId, IManager.UserInfoCallback callback)
```  
例如：

```
ACNAgent.register("123", new IManager.UserInfoCallback() {
           @Override
           public void success(Map<String, String> map) {
               //常用属性在UserAttr中已定义
                  String userId = map.get(UserAttr.USER_ID);
                  String nickname = map.get(UserAttr.NICK_NAME);
                  ...
           }

           @Override
           public void error(String msg) {
           }
       });

```

## 用户行为接口
behaviorType为行为类型，Dapp可自定义；  
extra为可选项。   

```
ACNAgent.onEvent(int behaviorType, String extra)
```


## 记录用户登录
如果您需要记录每天用户的登录信息，登录的类型在CommonType中已定义，请在父activity中调用:

```
ACNAgent.onEvent(CommonType.OPEN_DAPP, "");
```

## 注销用户接口
用户退出时务必调用， 清空在本地保留的用户信息    

```
ACNAgent.unregister()
```
## 更新用户信息接口
参数info为用户要更新的信息，常用属性在UserAttr中已定义，只需上传需要更新的即可。更新成功后在回调中返回用户已上传的所有信息。    
 
```
ACNAgent.updateUserInfo(Map<String, String> info,IManager.UserInfoCallback callback)
```

## 绑定钱包
该方法的返回可以在onActivityResult(int requestCode, int resultCode, @Nullable Intent data)中接收返回值:    
返回 resultCode = RESULT\_OK  
绑定是否成功 isBindSuc = data.getBooleanExtra(ACNKey.BIND\_STATE, false);     
绑定的奖励数量 reward = data.getIntExtra(ACNKey.BIND\_REWARD, 0);  
绑定奖励的虚拟币名称 rewardSymbol = data.getStringExtra(ACNKey.BIND\_REWARD\_SYMBOL);  
错误信息 errMsg = data.getStringExtra(ACNKey.ERROR\_MSG);  

```
bindApp(Activity activity, String appIconUrl, int reqCode)
```

## 解绑钱包
解绑后，无法将TTC转入钱包。    

```
ACNAgent.unbindApp(IManager.UnbindCallback callback)
```
## 待提取的ACN接口
在此app中，还未转入钱包的所有ACN；

```
ACNAgent.getAppBalance(IManager.BalanceCallback callback)
```
例如：

```
ACNAgent.getAppBalance(new IManager.BalanceCallback() {
    @Override
    public void success(BigDecimal balance) {
    }

    @Override
    public void error(String msg) {
    }
});
```

## 钱包的ACN余额接口
查询与此app绑定的钱包的余额；    

```
ACNAgent.getWalletBalance(IManager.BalanceCallback callback)
```
例如：

```
ACNAgent.getWalletBalance(new IManager.BalanceCallback() {
    @Override
        public void success(BigDecimal balance) {
        }

    @Override
    public void error(String msg) {
    }
});
```


## sdk设置
设置日志开关、 sdk功能开关（通过serverEnabled设置）

```
ACNAgent.configure(ACNConfigure configure)
```

例如：

```
ACNConfigure.Builder builder = new ACNConfigure.Builder();
builder.logEnabled(false);
builder.serverEnabled(true);
ACNAgent.configure(builder.build());
```

## sdk版本信息
获取sdk的版本名称和版本号：

```
ACNAgent.getVersionName()
ACNAgent.getVersionCode()
```

## 混淆配置
如果您的应用使用了混淆，请添加如下配置：

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
## 错误码文档
错误码文档[详情](./error_code/)


## 显示banner广告
ACNAdsBanner实现了此功能，首先调用init()方法，然后设置回调；adSize在ACNAdSize.kt中有枚举;

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
## 显示插页广告
在ACNAdsInterstitial中实现此功能，首先传入unitId初始化，然后请求广告requestAd(), 显示时调用show(),同样可以设置回调；

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

## 展示激励视频广告
创建ACNAdsRewardVideo对象，传入unitId初始化init()，请求广告requestAds(), 展示show(), 可以暂停pause()和恢复resume()；

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
