package com.acn.behavior.ui

import android.app.Activity
import android.content.Context
import com.acn.behavior.util.Constants
import com.acn.behavior.util.SDKLogger
import com.acn.behavior.util.Utils
import com.acn.biz.BizApi
import com.google.android.gms.ads.*
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback


/**
 * Created by tataUFO on 2021/9/17.
 */
class ACNInterstitialAd {

    private var interstitialAd: InterstitialAd? = null
    private var adUnitId: String = ""
    private var locateCode: String = ""

    fun load(context: Context, adUnitId: String, loadedCallBack: ACNInterstitialAdLoadedCallBack) {
        this.adUnitId = adUnitId
        this.locateCode = Utils.getLocationCode(context)
        val adRequest = AdRequest.Builder().build()
        InterstitialAd.load(context, adUnitId, adRequest, object : InterstitialAdLoadCallback() {
            override fun onAdFailedToLoad(adError: LoadAdError) {
                interstitialAd = null
                val error =
                    "Ad was failed to load: domain: ${adError.domain}, code: ${adError.code}, " +
                            "message: ${adError.message}"
                SDKLogger.d(error)
                loadedCallBack.onAdFailedToLoad(adError)
            }

            override fun onAdLoaded(interstitialAd: InterstitialAd) {
                this@ACNInterstitialAd.interstitialAd = interstitialAd
                SDKLogger.d("Interstitial: Ad was loaded.")
                loadedCallBack.onAdLoaded(this@ACNInterstitialAd)
            }
        })
    }

    fun setFullScreenContentCallBack(callback: ACNFullScreenContentCallBack) {
        interstitialAd?.fullScreenContentCallback = object : FullScreenContentCallback() {
            override fun onAdShowedFullScreenContent() {
                // Called when ad is shown.
                SDKLogger.d("Interstitial: Ad was shown.")
                BizApi.getInstance().uploadAdsEvent(
                    adUnitId,
                    locateCode,
                    Constants.TYPE_SHOW
                )
                callback.onAdShowedFullScreenContent()
            }

            override fun onAdFailedToShowFullScreenContent(adError: AdError) {
                // Called when ad fails to show.
                SDKLogger.d("Interstitial: Ad failed to show.")
                // Don't forget to set the ad reference to null so you
                interstitialAd = null
                callback.onAdFailedToShowFullScreenContent(adError)
            }

            override fun onAdDismissedFullScreenContent() {
                // Called when ad is dismissed.
                // Set the ad reference to null so you don't show the ad a second time.
                SDKLogger.d("Interstitial: Ad was dismissed.")
                // Don't forget to set the ad reference to null so you
                interstitialAd = null
                callback.onAdDismissedFullScreenContent()
            }

            override fun onAdImpression() {
                // Called when an impression is recorded for an ad.
                SDKLogger.d("Interstitial: an impression is recorded.")
                callback.onAdImpression()
            }
        }
    }

    fun show(activity: Activity) {
        interstitialAd?.let {
            BizApi.getInstance().uploadAdsEvent(
                adUnitId,
                locateCode,
                Constants.TYPE_CLICK
            )
            interstitialAd!!.show(activity)
        }
    }
}