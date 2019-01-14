package com.ttc.behavior.ui

import android.content.Context
import android.util.Log
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.InterstitialAd
import com.google.android.gms.ads.MobileAds
import com.ttc.behavior.util.Constants
import com.ttc.behavior.util.Utils
import com.ttc.biz.BizApi


/**
 * Created by lwq on 2019/1/7.
 */
class TTCAdsInterstitial {

    private lateinit var context: Context
    private lateinit var interstitialAd: InterstitialAd
    private var callback: TTCAdsCallback? = null


    fun init(context: Context, adMobAppId: String, unitId: String) {
        this.context = context
        MobileAds.initialize(context, adMobAppId)
        interstitialAd = InterstitialAd(context)
        interstitialAd.adUnitId = unitId
        Log.d("lwq", "intertitial unit id:" + unitId)

        interstitialAd.adListener = object : AdListener() {
            override fun onAdImpression() {
                super.onAdImpression()
                callback?.onAdImpression()
            }

            override fun onAdLeftApplication() {
                super.onAdLeftApplication()
                callback?.onAdLeftApplication()
                Log.d("lwq", "upload click interstitial")
                BizApi.uploadAdsEvent(
                    context,
                    interstitialAd.adUnitId,
                    Utils.getLocationCode(context),
                    Constants.TYPE_CLICK
                )
            }

            override fun onAdClicked() {
                super.onAdClicked()
                callback?.onAdClicked()
            }

            override fun onAdFailedToLoad(p0: Int) {
                super.onAdFailedToLoad(p0)
                callback?.onAdFailedToLoad(p0)
                Log.d("lwq", "interstitial failed: " + p0)
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
                Log.d("lwq", "interstitial loaded")
            }
        }
    }

    fun requestAd() {
        if (!interstitialAd.isLoading && !interstitialAd.isLoaded) {
            var adRequest = AdRequest.Builder()
                .build()

            interstitialAd.loadAd(adRequest)
            Log.d("lwq", "interstitial request")
        }
    }

    fun show() {
        if (interstitialAd.isLoaded) {
            Log.d("lwq", "upload interstitial show")
            interstitialAd.show()

            BizApi.uploadAdsEvent(context, interstitialAd.adUnitId, Utils.getLocationCode(context), Constants.TYPE_SHOW)
        }

    }

    fun setAdsCallback(callback: TTCAdsCallback) {
        this.callback = callback
    }
}