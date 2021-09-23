package com.acn.behavior.ui

import com.google.android.gms.ads.LoadAdError

/**
 * Created by tataUFO on 2021/9/17.
 */
open class ACNInterstitialAdLoadedCallBack {

    open fun onAdLoaded(p0: ACNInterstitialAd) {
        // Called when an ad successfully loads.
    }

    open fun onAdFailedToLoad(p0: LoadAdError) {
        // Called when an ad fails to load.
    }

}