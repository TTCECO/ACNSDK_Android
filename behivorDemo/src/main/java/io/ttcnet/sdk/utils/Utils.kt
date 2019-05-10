package io.ttcnet.sdk.utils

import com.acn.behavior.ACNAgent

/**
 * Created by lwq on 2019/1/11.
 */
object Utils {

    const val G_BANNER_UNIT_ID = "ca-app-pub-3940256099942544/6300978111"
    const val G_INTERSTITIAL_UNIT_ID = "ca-app-pub-3940256099942544/8691691433"
    const val G_REWARD_UNIT_ID = "ca-app-pub-3940256099942544/5224354917"

    const val BANNER_UNIT_ID = "ca-app-pub-3081086010287406/1197232103"
    const val INTERSTITIAL_UNIT_ID = "ca-app-pub-3940256099942544/6837367756"
    const val REWARD_UNIT_ID = "ca-app-pub-3940256099942544/8541233601"

    fun getBannerUnitId(): String {
        if (ACNAgent.isEnvProd()) {
            return BANNER_UNIT_ID;
        } else {
            return G_BANNER_UNIT_ID;
        }
    }

    fun getInterstitialUnitId(): String {
        if (ACNAgent.isEnvProd()) {
            return INTERSTITIAL_UNIT_ID;
        } else {
            return G_INTERSTITIAL_UNIT_ID;
        }
    }

    fun getRewardUnitId(): String {
        if (ACNAgent.isEnvProd()) {
            return REWARD_UNIT_ID;
        } else {
            return G_REWARD_UNIT_ID;
        }
    }


}