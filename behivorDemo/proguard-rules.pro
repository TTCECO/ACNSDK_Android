# Add project specific ProGuard rules here.
# You can control the set of applied configuration files using the
# proguardFiles setting in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

# Uncomment this to preserve the line number information for
# debugging stack traces.
#-keepattributes SourceFile,LineNumberTable

# If you keep the line number information, uncomment this to
# hide the original source file name.
#-renamesourcefileattribute SourceFile

# 混淆基础配置
-optimizationpasses 5                               # 指定代码的压缩级别
-dontusemixedcaseclassnames                         # 混淆时不会产生形形色色的类名
-dontskipnonpubliclibraryclasses                    # 指定不去忽略非公共的库类
-dontskipnonpubliclibraryclassmembers               # 指定不去忽略包可见的库类的成员
-dontpreverify                                      # 不预校验
-ignorewarnings                                     # 屏蔽警告
-verbose                                            # 混淆时记录日志
-optimizations !code/simplification/arithmetic,!field/*,!class/merging/*    #优化
-keepattributes *Annotation*                        # 保护代码中的Annotation不被混淆
-keepattributes Signature                           # 避免混淆泛型, 这在JSON实体映射时非常重要
-keepattributes SourceFile,LineNumberTable          # 抛出异常时保留代码行号

# 不需要混淆android-support-v4.jar
-dontwarn android.support.v4.**
-keep class android.support.v4.** { *; }
-keep interface android.support.v4.app.** { *; }
-keep public class * extends android.support.v4.**
-keep public class * extends android.app.Fragment

# 保留了继承自Activity、Application这些类的子类
# 因为这些子类有可能被外部调用
# 比如第一行就保证了所有Activity的子类不要被混淆
-keep public class * extends android.app.Activity
-keep public class * extends android.app.Application
-keep public class * extends android.content.BroadcastReceiver
-keep public class * extends android.view.View

-keepclasseswithmembernames class * {
    public <init>(android.content.Context, android.util.AttributeSet);
}

-keepclasseswithmembernames class * {
    public <init>(android.content.Context, android.util.AttributeSet, int);
}

# 关闭log
-assumenosideeffects class android.util.Log {
    public static boolean isLoggable(java.lang.String, int);
    public static int v(...);
    public static int i(...);
    public static int w(...);
    public static int d(...);
    public static int e(...);
}

#================本地Model===================
-keep public class com.ttc.wallet.model.**{*;}

#不需要混淆的第三方类库
#================tbs x5内核混淆===================
-dontwarn com.tencent.**
-keep class com.tencent.**{*;}

#==================web3j======================
-dontwarn org.web3j.**
-keep class org.web3j.**{*;}

#==================protobuf======================
-dontwarn com.google.**
-keep class com.google.protobuf.** {*;}

#==================OkHttp======================
# JSR 305 annotations are for embedding nullability information.
-dontwarn javax.annotation.**

# A resource is loaded with a relative path so the package of this class must be preserved.
-keepnames class okhttp3.internal.publicsuffix.PublicSuffixDatabase

# Animal Sniffer compileOnly dependency to ensure APIs are compatible with older versions of Java.
-dontwarn org.codehaus.mojo.animal_sniffer.*

# OkHttp platform used only on JVM and when Conscrypt dependency is available.
-dontwarn okhttp3.internal.platform.ConscryptPlatform

# okio is a dependency of OkHttp
-dontwarn okio.**

#==================Glide======================
-keep public class * implements com.bumptech.glide.module.GlideModule
-keep public class * extends com.bumptech.glide.module.AppGlideModule
-keep public enum com.bumptech.glide.load.ImageHeaderParser$** {
  **[] $VALUES;
  public *;
}

#=================liteORM=====================
-keepclassmembers enum * {
    **[] $VALUES;
    public *;
}

#=================Eventbus=====================
-keepclassmembers class * {
    @org.greenrobot.eventbus.Subscribe <methods>;
}
-keep enum org.greenrobot.eventbus.ThreadMode { *; }


#=================Crashlytics=====================
-keep class com.crashlytics.** { *; }
-dontwarn com.crashlytics.**
-keep public class * extends java.lang.Exception

#=============getui==============
-dontwarn com.igexin.**
-keep class com.igexin.** { *; }
-keep class org.json.** { *; }

