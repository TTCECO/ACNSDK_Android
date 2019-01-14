package com.ttc.behavior.ui

import android.content.Context
import android.util.Log
import android.widget.FrameLayout
import com.google.android.gms.ads.*
import com.ttc.behavior.util.Constants
import com.ttc.behavior.util.Utils
import com.ttc.biz.BizApi


/**
 * Created by lwq on 2019/1/2.
 */
class TTCAdsBanner {

    private var callback: TTCAdsCallback? = null

    fun init(context: Context, adMobAppId: String, unitId: String, adSize: Int): FrameLayout {
        MobileAds.initialize(context, adMobAppId)

        var container = FrameLayout(context)
        var adview = AdView(context);

        if (adSize == TTCAdSize.BANNER) {
            adview.adSize = AdSize.BANNER;
        } else {
            adview.adSize = AdSize.SMART_BANNER
        }
        Log.d("lwq", "banner unit id:" + unitId)
        adview.adUnitId = unitId

        container.addView(adview)
        var adRequest = AdRequest.Builder().build()

        adview.loadAd(adRequest)

        adview.adListener = object : AdListener() {
            override fun onAdImpression() {
                super.onAdImpression()
                callback?.onAdImpression()
            }

            override fun onAdLeftApplication() {
                super.onAdLeftApplication()
                callback?.onAdLeftApplication()
                Log.d("lwq", "upload click banner")
                BizApi.uploadAdsEvent(context, adview.adUnitId, Utils.getLocationCode(context), Constants.TYPE_CLICK)
            }

            override fun onAdClicked() {
                super.onAdClicked()
                callback?.onAdClicked()
            }

            override fun onAdFailedToLoad(p0: Int) {
                super.onAdFailedToLoad(p0)
                callback?.onAdFailedToLoad(p0)
            }

            override fun onAdClosed() {
                super.onAdClosed()
                callback?.onAdClosed()
            }

            override fun onAdOpened() {
                super.onAdOpened()
                callback?.onAdOpened()
            }

            override fun onAdLoaded() {
                super.onAdLoaded()
                callback?.onAdLoaded()
                Log.d("lwq", "upload banner show")
                BizApi.uploadAdsEvent(context, adview.adUnitId, Utils.getLocationCode(context), Constants.TYPE_SHOW)
            }
        }
        return container
    }


    fun setBannerCallback(ttcAdsCallback: TTCAdsCallback) {
        this.callback = ttcAdsCallback
    }
}