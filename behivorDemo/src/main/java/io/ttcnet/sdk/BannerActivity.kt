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
        var bannerView = banner.init(activity,  BuildConfig.bannerUnitId, TTCAdSize.BANNER)
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
