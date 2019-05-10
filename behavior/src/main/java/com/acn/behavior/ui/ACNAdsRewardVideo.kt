package com.acn.behavior.ui

import android.app.Activity
import android.content.Context
import android.util.Log
import com.acn.behavior.util.Constants
import com.acn.behavior.util.Utils
import com.acn.biz.BizApi
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.MobileAds
import com.google.android.gms.ads.reward.RewardItem
import com.google.android.gms.ads.reward.RewardedVideoAd
import com.google.android.gms.ads.reward.RewardedVideoAdListener


/**
 * Created by lwq on 2019/1/7.
 */
class ACNAdsRewardVideo {

    private lateinit var activity: Activity
    private lateinit var rewardedVideoAd: RewardedVideoAd
    private var rewardCallback: ACNRewardCallback? = null
    private var unitId = ""


    fun init(activity: Activity, rewardUnitId: String) {
        this.activity = activity
        this.unitId = rewardUnitId

        rewardedVideoAd = MobileAds.getRewardedVideoAdInstance(activity)

        rewardedVideoAd.rewardedVideoAdListener = object : RewardedVideoAdListener {
            override fun onRewardedVideoAdClosed() {
                rewardCallback?.onRewardedVideoAdClosed()
            }

            override fun onRewardedVideoAdLeftApplication() {
                rewardCallback?.onRewardedVideoAdLeftApplication()
                Log.d("lwq", "upload click reward")
                BizApi.uploadAdsEvent(activity, unitId, Utils.getLocationCode(activity), Constants.TYPE_CLICK)
            }

            override fun onRewardedVideoAdLoaded() {
                rewardCallback?.onRewardedVideoAdLoaded()
                Log.d("lwq", "reward loaded")
            }

            override fun onRewardedVideoAdOpened() {
                rewardCallback?.onRewardedVideoAdOpened()
                Log.d("lwq", "reward loaded")
                BizApi.uploadAdsEvent(activity, unitId, Utils.getLocationCode(activity), Constants.TYPE_SHOW)
            }

            override fun onRewardedVideoCompleted() {
                rewardCallback?.onRewardedVideoCompleted()
                Log.d("lwq", "reward loaded")
            }

            override fun onRewarded(p0: RewardItem?) {
                rewardCallback?.onRewarded(p0?.type, p0?.amount)
                BizApi.uploadAdsEvent(activity, unitId, Utils.getLocationCode(activity), Constants.TYPE_VIDEO_OVER)
            }

            override fun onRewardedVideoStarted() {
                rewardCallback?.onRewardedVideoStarted()
            }

            override fun onRewardedVideoAdFailedToLoad(p0: Int) {
                rewardCallback?.onRewardedVideoAdFailedToLoad(p0)
                Log.d("lwq", "reward failed: " + p0)
            }
        }
    }

    fun requestAds() {
        if (!rewardedVideoAd.isLoaded) {
            rewardedVideoAd.loadAd(unitId, AdRequest.Builder().build())
            Log.d("lwq", "reward request:" + unitId)
        }
    }

    fun show() {
        if (rewardedVideoAd.isLoaded) {
            rewardedVideoAd.show()
            Log.d("lwq", "upload reward show")
        }
    }


    fun pause(context: Context) {
        rewardedVideoAd.pause(context)
    }

    fun resume(context: Context) {
        rewardedVideoAd.resume(context)
    }

    fun setRewardedCallback(rewardCallback: ACNRewardCallback) {
        this.rewardCallback = rewardCallback
    }
}