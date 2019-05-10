package com.acn.behavior.ui

import android.content.Context
import android.util.Log
import com.acn.behavior.util.Constants
import com.acn.behavior.util.Utils
import com.acn.biz.BizApi
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.InterstitialAd


/**
 * Created by lwq on 2019/1/7.
 */
class ACNAdsInterstitial {

    private lateinit var context: Context
    private lateinit var interstitialAd: InterstitialAd
    private var callback: ACNAdsCallback? = null


    fun init(context: Context, unitId: String) {
        this.context = context
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
                BizApi.uploadAdsEvent(
                    context,
                    interstitialAd.adUnitId,
                    Utils.getLocationCode(context),
                    Constants.TYPE_SHOW
                )
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
        }

    }

    fun setAdsCallback(callback: ACNAdsCallback) {
        this.callback = callback
    }
}