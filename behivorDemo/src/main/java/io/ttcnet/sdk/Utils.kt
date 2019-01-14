package io.ttcnet.sdk

import android.content.Context
import android.content.pm.PackageManager

/**
 * Created by lwq on 2019/1/11.
 */
object Utils {

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
}