# TTCSDK_Android

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
