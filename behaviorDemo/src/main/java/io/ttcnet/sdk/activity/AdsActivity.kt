package io.ttcnet.sdk.activity

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.acn.behavior.ui.*
import com.google.android.gms.ads.AdError
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.rewarded.RewardItem
import io.ttcnet.sdk.R
import io.ttcnet.sdk.utils.Utils
import kotlinx.android.synthetic.main.activity_ads.*

class AdsActivity : AppCompatActivity() {

    private var activity = this

    //    private var appId = ""
    private lateinit var interstitialAd: ACNInterstitialAd
    private lateinit var rewardAd: ACNRewardedAd
    private var interstitialAdLoading = false
    private var rewardAdLoading = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ads)

//        Utils.useProdId = true

        ads_banner.setOnClickListener {
            startActivity(Intent(activity, BannerActivity::class.java))
        }

        ads_interstitial.setOnClickListener {
            requestInterstitial()
        }

        ads_reward_video.setOnClickListener {
            initRewardVideo()
        }
    }

    private fun requestInterstitial() {
        if (!interstitialAdLoading) {
            interstitialAdLoading = true
            interstitialAd = ACNInterstitialAd()
            interstitialAd.load(
                activity,
                Utils.getInterstitialUnitId(),
                object : ACNInterstitialAdLoadedCallBack() {
                    override fun onAdLoaded(p0: ACNInterstitialAd) {

                        Toast.makeText(activity, "interstitial ad Loaded", Toast.LENGTH_SHORT)
                            .show()
                        interstitialAd.setFullScreenContentCallBack(object :
                            ACNFullScreenContentCallBack() {
                            override fun onAdShowedFullScreenContent() {

                            }

                            override fun onAdDismissedFullScreenContent() {
                                interstitialAdLoading = false
                            }

                            override fun onAdFailedToShowFullScreenContent(p0: AdError) {
                                interstitialAdLoading = false
                            }
                        })
                        interstitialAd.show(this@AdsActivity)
                    }

                    override fun onAdFailedToLoad(p0: LoadAdError) {
                        interstitialAdLoading = false
                        Toast.makeText(activity, "failed to loaded", Toast.LENGTH_SHORT).show()
                    }
                })
        }
    }

    private fun initRewardVideo() {
        if (!rewardAdLoading) {
            rewardAdLoading = true
            rewardAd = ACNRewardedAd()
            rewardAd.load(
                activity,
                Utils.getRewardUnitId(),
                object : ACNRewardedAdLoadedCallBack() {
                    override fun onAdLoaded(p0: ACNRewardedAd) {
                        Toast.makeText(activity, "rewarded ad Loaded", Toast.LENGTH_SHORT).show()
                        rewardAd.setFullScreenContentCallBack(object :
                            ACNFullScreenContentCallBack() {
                            override fun onAdShowedFullScreenContent() {

                            }

                            override fun onAdDismissedFullScreenContent() {
                                rewardAdLoading = false
                            }

                            override fun onAdFailedToShowFullScreenContent(p0: AdError) {
                                rewardAdLoading = false
                            }
                        })
                        rewardAd.show(this@AdsActivity, object : OnAcnUserEarnedRewardListener {
                            override fun onAcnUserEarnedReward(p0: RewardItem?) {
                                Toast.makeText(
                                    activity,
                                    "user has got the rewarded",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }

                        })
                    }

                    override fun onAdFailedToLoad(p0: LoadAdError) {
                        rewardAdLoading = false
                        Toast.makeText(activity, "failed to loaded", Toast.LENGTH_SHORT).show()
                    }
                })
        }
    }

}
