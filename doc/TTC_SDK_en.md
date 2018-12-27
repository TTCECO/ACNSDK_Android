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
```

## Initiate 
Initiate it in Application.java.  

```
@Override
public void onCreate() {
    super.onCreate();
    int errCode = TTCAgent.init(this);
    if (errCode > 0) {
       String msg = TTCError.getMessage(errCode);
    }
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