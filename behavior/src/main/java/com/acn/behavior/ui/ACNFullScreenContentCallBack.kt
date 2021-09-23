package com.acn.behavior.ui

import com.google.android.gms.ads.AdError

/**
 * Created by tataUFO on 2021/9/17.
 */
open class ACNFullScreenContentCallBack {

    open fun onAdFailedToShowFullScreenContent(p0: AdError) {
        // Called when the ad failed to show full screen content.
    }

    open fun onAdShowedFullScreenContent() {
        // Called when the ad showed the full screen content.
    }

    open fun onAdDismissedFullScreenContent() {
        // Called when the ad dismissed full screen content.
    }

    open fun onAdImpression() {
        // Called when an impression is recorded for an ad.
    }
}