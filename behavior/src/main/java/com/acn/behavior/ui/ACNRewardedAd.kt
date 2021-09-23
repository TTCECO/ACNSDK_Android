package com.acn.behavior.ui

import android.app.Activity
import android.content.Context
import com.acn.behavior.util.Constants
import com.acn.behavior.util.SDKLogger
import com.acn.behavior.util.Utils
import com.acn.biz.BizApi
import com.google.android.gms.ads.*
import com.google.android.gms.ads.rewarded.RewardItem
import com.google.android.gms.ads.rewarded.RewardedAd
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback


/**
 * Created by tataUFO on 2021/9/17.
 */
class ACNRewardedAd {
    private var rewardedAd: RewardedAd? = null
    private var adUnitId: String = ""
    private var locateCode: String = ""

    fun load(context: Context, adUnitId: String, loadedCallBack: ACNRewardedAdLoadedCallBack) {
        this.adUnitId = adUnitId
        this.locateCode = Utils.getLocationCode(context)
        val adRequest = AdRequest.Builder().build()

        RewardedAd.load(context, adUnitId, adRequest, object : RewardedAdLoadCallback() {
            override fun onAdFailedToLoad(adError: LoadAdError) {
                rewardedAd = null
                val error =
                    "RewardedAd was failed to load: domain: ${adError.domain}, code: ${adError.code}, " +
                            "message: ${adError.message}"
                SDKLogger.d(error)
                loadedCallBack.onAdFailedToLoad(adError)
            }

            override fun onAdLoaded(rewardedAd: RewardedAd) {
                this@ACNRewardedAd.rewardedAd = rewardedAd
                SDKLogger.d("Rewarded: Ad was loaded.")
                loadedCallBack.onAdLoaded(this@ACNRewardedAd)
            }
        })
    }

    fun setFullScreenContentCallBack(callback: ACNFullScreenContentCallBack) {
        rewardedAd?.fullScreenContentCallback = object : FullScreenContentCallback() {
            override fun onAdShowedFullScreenContent() {
                // Called when ad is shown.
                SDKLogger.d("Rewarded: Ad was shown.")
                BizApi.getInstance().uploadAdsEvent(
                    adUnitId,
                    locateCode,
                    Constants.TYPE_SHOW
                )
                callback.onAdShowedFullScreenContent()
            }

            override fun onAdFailedToShowFullScreenContent(adError: AdError) {
                // Called when ad fails to show.
                SDKLogger.d("Rewarded: Ad failed to show.")
                // Don't forget to set the ad reference to null so you
                rewardedAd = null
                callback.onAdFailedToShowFullScreenContent(adError)
            }

            override fun onAdDismissedFullScreenContent() {
                // Called when ad is dismissed.
                // Set the ad reference to null so you don't show the ad a second time.
                SDKLogger.d("Rewarded: Ad was dismissed.")
                // Don't forget to set the ad reference to null so you
                rewardedAd = null
                callback.onAdDismissedFullScreenContent()
            }

            override fun onAdImpression() {
                // Called when an impression is recorded for an ad.
                SDKLogger.d("Rewarded: an impression is recorded.")
                callback.onAdImpression()
            }
        }
    }

    fun show(activity: Activity, onAcnUserEarnedRewardListener: OnAcnUserEarnedRewardListener) {
        rewardedAd?.let {
            BizApi.getInstance().uploadAdsEvent(
                adUnitId,
                locateCode,
                Constants.TYPE_CLICK
            )

            rewardedAd!!.show(activity, object : OnUserEarnedRewardListener {
                override fun onUserEarnedReward(p0: RewardItem) {
                    // User earned the reward.
                    SDKLogger.d("Rewarded: User earned the reward.")
                    onAcnUserEarnedRewardListener.onAcnUserEarnedReward(p0)
                }

            })
        }
    }
}