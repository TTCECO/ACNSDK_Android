package io.ttcnet.sdk

import android.content.Context
import android.content.pm.PackageManager

/**
 * Created by lwq on 2019/1/11.
 */
object Utils {

    const val G_BANNER_UNIT_ID = "ca-app-pub-3940256099942544/6300978111"
    const val G_INTERSTITIAL_UNIT_ID = "ca-app-pub-3940256099942544/8691691433"
    const val G_REWARD_UNIT_ID = "ca-app-pub-3940256099942544/5224354917"

    const val BANNER_UNIT_ID = "ca-app-pub-3081086010287406/1197232103"
    const val INTERSTITIAL_UNIT_ID = "ca-app-pub-3940256099942544/6837367756"
    const val REWARD_UNIT_ID = "ca-app-pub-3940256099942544/8541233601"

    //true-采用线上的广告id，false-采用google提供的开发id。如果不设置，默认是true
    var useProdId = true;  //是否使用线上的各种id

    fun getAdmobAppId(context: Context): String {
        try {
            val packageInfo = context.getPackageManager().getPackageInfo(
                context.getPackageName(), PackageManager
                    .GET_META_DATA
            )
            var appId = packageInfo.applicationInfo.metaData.getString("com.google.android.gms.ads.APPLICATION_ID")
            return appId
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
        }

        return ""
    }


    fun getBannerUnitId(): String {
        if (useProdId) {
            return BANNER_UNIT_ID;
        } else {
            return G_BANNER_UNIT_ID;
        }
    }

    fun getInterstitialUnitId(): String {
        if (useProdId) {
            return INTERSTITIAL_UNIT_ID;
        } else {
            return G_INTERSTITIAL_UNIT_ID;
        }
    }

    fun getRewardUnitId(): String {
        if (useProdId) {
            return REWARD_UNIT_ID;
        } else {
            return G_REWARD_UNIT_ID;
        }
    }


}