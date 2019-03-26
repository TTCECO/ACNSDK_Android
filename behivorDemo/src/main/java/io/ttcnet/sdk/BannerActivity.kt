package io.ttcnet.sdk

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.Toast
import com.ttc.behavior.ui.TTCAdSize
import com.ttc.behavior.ui.TTCAdsBanner
import com.ttc.behavior.ui.TTCAdsCallback
import kotlinx.android.synthetic.main.activity_banner.*

class BannerActivity : AppCompatActivity() {

    private var activity = this
    private var appId = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_banner)

        initAdsBanner()

    }


    private fun initAdsBanner() {

        var banner = TTCAdsBanner()
        var bannerView = banner.init(activity, Utils.getBannerUnitId(), TTCAdSize.MEDIUM_RECTANGLE)
        ads_banner_container_fl.addView(bannerView)

        banner.setBannerCallback(object : TTCAdsCallback() {

            override fun onAdImpression() {
                super.onAdImpression()
            }

            override fun onAdLeftApplication() {
                super.onAdLeftApplication()
                Toast.makeText(activity, "banner left", Toast.LENGTH_SHORT).show()
            }

            override fun onAdClicked() {
                super.onAdClicked()
            }

            override fun onAdFailedToLoad(p0: Int) {
                super.onAdFailedToLoad(p0)

//                Constant Value: 0
//                public static final int ERROR_CODE_INTERNAL_ERROR
//                Something happened internally; for instance, an invalid response was received from the ad server.
//
//                Constant Value: 1
//                public static final int ERROR_CODE_INVALID_REQUEST
//                The ad request was invalid; for instance, the ad unit ID was incorrect.
//
//                    Constant Value: 2
//                public static final int ERROR_CODE_NETWORK_ERROR
//                The ad request was unsuccessful due to network connectivity.
//
//                Constant Value: 3
//                public static final int ERROR_CODE_NO_FILL
//                The ad request was successful, but no ad was returned due to lack of ad inventory.

                Toast.makeText(activity, "banner failed:" + p0, Toast.LENGTH_SHORT).show()
            }

            override fun onAdClosed() {
                super.onAdClosed()
            }

            override fun onAdOpened() {
                super.onAdOpened()
            }

            override fun onAdLoaded() {
                super.onAdLoaded()
                Toast.makeText(activity, "banner adloaded", Toast.LENGTH_SHORT).show()
            }
        });
    }
}
