package com.acn.behavior.ui

/**
 * Created by lwq on 2019/1/3.
 */
open class ACNAdsCallback {

    open fun onAdImpression() {}

    open fun onAdLeftApplication() {}

    open fun onAdClicked() {}

    open fun onAdFailedToLoad(p0: Int) {}

    open fun onAdClosed() {}

    open fun onAdOpened() {}

    open fun onAdLoaded() {}
}