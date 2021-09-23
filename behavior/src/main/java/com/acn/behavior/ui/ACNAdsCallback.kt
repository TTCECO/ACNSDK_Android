package com.acn.behavior.ui

import com.google.android.gms.ads.LoadAdError

/**
 * Created by lwq on 2019/1/3.
 */
open class ACNAdsCallback {

    open fun onAdImpression() {}

    open fun onAdClicked() {}

    open fun onAdFailedToLoad(p0: LoadAdError) {}

    open fun onAdClosed() {}

    open fun onAdOpened() {}

    open fun onAdLoaded() {}
}