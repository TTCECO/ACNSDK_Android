package com.ttc.behavior.ui

import android.content.Context
import android.widget.FrameLayout
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdSize
import com.google.android.gms.ads.AdView
import com.ttc.behavior.util.Constants
import com.ttc.behavior.util.Utils
import com.ttc.biz.BizApi


/**
 * Created by lwq on 2019/1/2.
 */
class TTCAdsBanner {

    private var isTest = false;     //
    private var callback: TTCAdsCallback? = null

    fun init(context: Context, unitId: String, adSize: Int): FrameLayout {

        var container = FrameLayout(context)
        var adView = AdView(context);

        when (adSize) {
            TTCAdSize.BANNER -> adView.adSize = AdSize.BANNER
            TTCAdSize.LARGE_BANNER -> adView.adSize = AdSize.LARGE_BANNER
            TTCAdSize.MEDIUM_RECTANGLE ->adView.adSize = AdSize.MEDIUM_RECTANGLE
            TTCAdSize.LEADER_BOARD -> AdSize.LEADERBOARD
            TTCAdSize.FULL_BANNER -> adView.adSize = AdSize.FULL_BANNER
            TTCAdSize.SMART_BANNER -> adView.adSize = AdSize.SMART_BANNER
            else -> adView.adSize = AdSize.SMART_BANNER
        }
        adView.adUnitId = unitId

        container.addView(adView)
        var requestBuilder = AdRequest.Builder()

        if (isTest) {
            requestBuilder.addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
        }

        adView.loadAd(requestBuilder.build())

        adView.adListener = object : AdListener() {
            override fun onAdImpression() {
                super.onAdImpression()
                callback?.onAdImpression()
            }

            override fun onAdLeftApplication() {
                super.onAdLeftApplication()
                callback?.onAdLeftApplication()
                BizApi.uploadAdsEvent(context, adView.adUnitId, Utils.getLocationCode(context), Constants.TYPE_CLICK)
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
                BizApi.uploadAdsEvent(context, adView.adUnitId, Utils.getLocationCode(context), Constants.TYPE_SHOW)
            }
        }
        return container
    }


    fun setBannerCallback(ttcAdsCallback: TTCAdsCallback) {
        this.callback = ttcAdsCallback
    }

}