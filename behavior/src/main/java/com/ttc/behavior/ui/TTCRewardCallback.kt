package com.ttc.behavior.ui

/**
 * Created by lwq on 2019/1/7.
 */
open class TTCRewardCallback {

    open fun onRewardedVideoAdClosed() {}

    open fun onRewardedVideoAdLeftApplication() {}

    open fun onRewardedVideoAdLoaded() {}

    open fun onRewardedVideoAdOpened() {}

    open fun onRewardedVideoCompleted() {}

    open fun onRewarded(type: String?, amount:Int?) {}

    open fun onRewardedVideoStarted() {}

    open fun onRewardedVideoAdFailedToLoad(p0: Int) {}

}