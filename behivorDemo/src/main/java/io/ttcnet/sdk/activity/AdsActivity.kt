package io.ttcnet.sdk.activity

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.widget.Toast
import com.acn.behavior.ui.ACNAdsCallback
import com.acn.behavior.ui.ACNAdsInterstitial
import com.acn.behavior.ui.ACNAdsRewardVideo
import com.acn.behavior.ui.ACNRewardCallback
import io.ttcnet.sdk.R
import io.ttcnet.sdk.utils.Utils
import kotlinx.android.synthetic.main.activity_ads.*

class AdsActivity : AppCompatActivity() {

    private var activity = this
    //    private var appId = ""
    private lateinit var interstitial: ACNAdsInterstitial
    private lateinit var rewardVideo: ACNAdsRewardVideo

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ads)


//        Utils.useProdId = true

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
        interstitial = ACNAdsInterstitial()
        interstitial.init(activity, Utils.getInterstitialUnitId())

        interstitial.setAdsCallback(object : ACNAdsCallback() {

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
        rewardVideo = ACNAdsRewardVideo()
        rewardVideo.init(activity, Utils.getRewardUnitId())
        rewardVideo.setRewardedCallback(object : ACNRewardCallback() {
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
