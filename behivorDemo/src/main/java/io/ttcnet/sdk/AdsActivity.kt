package io.ttcnet.sdk

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.Toast
import com.ttc.behavior.ui.TTCAdsCallback
import com.ttc.behavior.ui.TTCAdsInterstitial
import com.ttc.behavior.ui.TTCAdsRewardVideo
import com.ttc.behavior.ui.TTCRewardCallback
import kotlinx.android.synthetic.main.activity_ads.*

class AdsActivity : AppCompatActivity() {

    private var activity = this
    //    private var appId = ""
    private lateinit var interstitial: TTCAdsInterstitial
    private lateinit var rewardVideo: TTCAdsRewardVideo

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ads)

        initInterstitial()
        initRewardVideo()

        ads_banner.setOnClickListener {
            startActivity(Intent(activity, BannerActivity::class.java))
        }

        ads_interstitial.setOnClickListener {
            interstitial.requestAd()
        }

        ads_reward_video.setOnClickListener {
            rewardVideo.requestAds()
        }
    }


    private fun initInterstitial() {
        interstitial = TTCAdsInterstitial()
        interstitial.init(activity, Utils.getAdmobAppId(activity), BuildConfig.interstitialUnitId)

        interstitial.setAdsCallback(object : TTCAdsCallback() {
            override fun onLoaded() {
                super.onLoaded()
                Toast.makeText(activity, "interstitial loaded", Toast.LENGTH_SHORT).show()
            }

            override fun onAdImpression() {
                super.onAdImpression()
                Toast.makeText(activity, "interstitial impression", Toast.LENGTH_SHORT).show()
            }

            override fun onAdLeftApplication() {
                super.onAdLeftApplication()
                Toast.makeText(activity, "interstitial left", Toast.LENGTH_SHORT).show()
            }

            override fun onAdClicked() {
                super.onAdClicked()
            }

            override fun onAdFailedToLoad(p0: Int) {
                super.onAdFailedToLoad(p0)
                Toast.makeText(activity, "interstitial failed loaded:" + p0, Toast.LENGTH_SHORT).show()
            }

            override fun onAdClosed() {
                super.onAdClosed()
            }

            override fun onAdOpened() {
                super.onAdOpened()
            }

            override fun onAdLoaded() {
                super.onAdLoaded()
                interstitial.show()
                Toast.makeText(activity, "interstitial adLoaded", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun initRewardVideo() {
        rewardVideo = TTCAdsRewardVideo()
        rewardVideo.init(activity, Utils.getAdmobAppId(activity), BuildConfig.rewardUnitId)
        rewardVideo.setRewardedCallback(object : TTCRewardCallback() {
            override fun onRewardedVideoAdClosed() {
                super.onRewardedVideoAdClosed()
            }

            override fun onRewardedVideoAdLeftApplication() {
                super.onRewardedVideoAdLeftApplication()
                Toast.makeText(activity, "reward left", Toast.LENGTH_SHORT).show()
            }

            override fun onRewardedVideoAdLoaded() {
                super.onRewardedVideoAdLoaded()
                rewardVideo.show()
                Toast.makeText(activity, "reward loaded", Toast.LENGTH_SHORT).show()
            }

            override fun onRewardedVideoAdOpened() {
                super.onRewardedVideoAdOpened()
            }

            override fun onRewardedVideoCompleted() {
                super.onRewardedVideoCompleted()
                Toast.makeText(activity, "reward complete", Toast.LENGTH_SHORT).show()
            }

            override fun onRewarded(type: String?, amount: Int?) {
                super.onRewarded(type, amount)
            }

            override fun onRewardedVideoStarted() {
                super.onRewardedVideoStarted()
            }

            override fun onRewardedVideoAdFailedToLoad(p0: Int) {
                super.onRewardedVideoAdFailedToLoad(p0)
                Toast.makeText(activity, "reward failed loaded:" + p0, Toast.LENGTH_SHORT).show()
            }
        })
    }

    override fun onResume() {
        super.onResume()
        rewardVideo.resume(activity)
    }

    override fun onPause() {
        super.onPause()
        rewardVideo.pause(activity)
    }


}
