package io.ttcnet.sdk.activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;
import com.acn.behavior.ui.ACNAdSize;
import com.acn.behavior.ui.ACNAdsBanner;
import com.acn.behavior.ui.ACNAdsCallback;
import io.ttcnet.sdk.R;
import io.ttcnet.sdk.utils.Utils;

public class BannerActivity extends AppCompatActivity {

    private Activity activity = this;
    private String appId = "";
    private FrameLayout container;
    private RadioGroup radioGroup;
    private RadioButton radioButton1;
    private int type = ACNAdSize.BANNER;
    ACNAdsBanner banner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_banner);

        container = findViewById(R.id.ads_banner_container_fl);
        radioGroup = findViewById(R.id.banner_radio_group);
        radioButton1 = findViewById(R.id.banner_1);

        initRadioGroup();
        changeType(type);
    }


    private void changeType(int type) {

//        if (banner != null) {
//            banner.
//        }
        banner = new ACNAdsBanner();
        ViewGroup bannerView = banner.init(activity, Utils.INSTANCE.getBannerUnitId(), type);
        container.removeAllViews();
        container.addView(bannerView);

        banner.setBannerCallback(new ACNAdsCallback() {

            @Override
            public void onAdImpression() {
                super.onAdImpression();
            }

            @Override
            public void onAdLeftApplication() {
                super.onAdLeftApplication();
                Toast.makeText(activity, "banner left", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onAdClicked() {
                super.onAdClicked();
            }

            @Override
            public void onAdFailedToLoad(int p0) {
                super.onAdFailedToLoad(p0);

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

                Toast.makeText(activity, "banner failed:" + p0, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onAdClosed() {
                super.onAdClosed();
            }

            @Override
            public void onAdOpened() {
                super.onAdOpened();
            }

            @Override
            public void onAdLoaded() {
                super.onAdLoaded();
                Toast.makeText(activity, "banner adloaded", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void initRadioGroup() {
        radioGroup.check(R.id.banner_1);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.banner_1:
                        type = ACNAdSize.BANNER;
                        break;
                    case R.id.banner_2:
                        type = ACNAdSize.LARGE_BANNER;
                        break;
                    case R.id.banner_3:
                        type = ACNAdSize.MEDIUM_RECTANGLE;
                        break;
                    case R.id.banner_4:
                        type = ACNAdSize.LEADER_BOARD;
                        break;
                    case R.id.banner_5:
                        type = ACNAdSize.FULL_BANNER;
                        break;
                    case R.id.banner_6:
                        type = ACNAdSize.SMART_BANNER;
                        break;
                }
                changeType(type);
            }
        });
    }

//    private void changeType(){
//        switch (type) {
//            case ACNAdSize.BANNER:
//                break;
//            case ACNAdSize.LARGE_BANNER:
//                break;
//            case ACNAdSize.MEDIUM_RECTANGLE:
//                break;
//            case ACNAdSize.LEADER_BOARD:
//                break;
//            case ACNAdSize.FULL_BANNER:
//                break;
//            case ACNAdSize.SMART_BANNER:
//                break;
//        }
//    }
}
