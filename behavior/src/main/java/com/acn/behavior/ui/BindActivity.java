package com.acn.behavior.ui;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.acn.behavior.ACNAgent;
import com.acn.behavior.IManager;
import com.acn.behavior.R;
import com.acn.behavior.db.ACNSp;
import com.acn.behavior.util.*;
import com.acn.biz.model.BaseInfo;
import com.acn.biz.model.BindSucData;

public class BindActivity extends Activity {

    private TextView tvTitle;
    private TextView tvWallet;
    private TextView tvApp;
    private TextView tvBind;
    private ImageView ivIconApp;

    private String walletAddress;
    private int bindReward;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bind);

        initData();
        ACNAgent.init(this.getApplicationContext());

        if (isSDKRegisted()) {
            initView();
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    private void initView() {

        tvTitle = findViewById(R.id.tv_title);
        tvWallet = findViewById(R.id.tv_wallet);
        tvApp = findViewById(R.id.tv_app);
        tvBind = findViewById(R.id.btn_bind);
        ivIconApp = findViewById(R.id.iv_icon_app);

        setBold(tvTitle);
        setBold(tvWallet);
        setBold(tvApp);
        setBold(tvBind);

        if (bindReward > 0) {
            tvTitle.setText(getString(R.string.get_ttc_after_bind, bindReward));
        }
        Drawable drawable = Utils.getApplicationIcon(this);
        ivIconApp.setImageDrawable(drawable);

        tvApp.setText(Utils.getApplicationName(this));
    }

    private void initData() {
        walletAddress = getIntent().getStringExtra(ACNKey.WALLET_ADDRESS);
        bindReward = getIntent().getIntExtra(ACNKey.BIND_REWARD, 0);
//        SDKLogger.e("walletAddress=" + walletAddress);
    }

    private void setBold(TextView textView) {
        textView.getPaint().setFakeBoldText(true);
    }


    public void bind(View view) {
        if (isSDKRegisted()) {
            final boolean auto = true;
            ACNAgent.getClient().getRepo().bindApp(walletAddress, auto, new IManager.BindCallback() {
                @Override
                public void success(BindSucData data) {
                    bindReward = data.reward;
                    finishActivity(1, auto, 0, "");
                }

                @Override
                public void error(String msg) {
                    finishActivity(0, auto, 0, msg);
                }
            });
        }
    }

    public void close(View v) {
        finishActivity(0, false, 0, "");
    }

    private void finishActivity(int bindState, boolean autoTransaction, int errorCode, String errorMsg) {
        Intent data = new Intent();
        if (ACNAgent.getClient() != null) {
            data.putExtra(ACNKey.APP_ID, BaseInfo.getInstance().getAppId());
        }
        data.putExtra(ACNKey.BIND_STATE, bindState);
        data.putExtra(ACNKey.AUTO_TRANSACTION, autoTransaction);
        data.putExtra(ACNKey.ERROR_CODE, errorCode);   //0:错误原因不在列举的范围中
        data.putExtra(ACNKey.ERROR_MSG, errorMsg);
        data.putExtra(ACNKey.BIND_REWARD, bindReward);

        setResult(RESULT_OK, data);
        finish();
    }

    private boolean isSDKRegisted() {
        int errorCode = -1;
        String errorInfo = "";
        if (TextUtils.isEmpty(ACNSp.getUserId())) {
            errorCode = SDKError.USER_ID_IS_EMPTY;
//            errorInfo = SDKError.getMessage(SDKError.USER_ID_IS_EMPTY);
        } else if (TextUtils.isEmpty(ACNSp.getDappId())) {
            errorCode = SDKError.APP_ID_IS_EMPTY;
//            errorInfo = SDKError.getMessage(SDKError.APP_ID_IS_EMPTY);
        } else if (TextUtils.isEmpty(ACNSp.getDappSecretKey())) {
            errorCode = SDKError.SECRET_KEY_IS_EMPTY;
//            errorInfo = SDKError.getMessage(SDKError.SECRET_KEY_IS_EMPTY);
        } else if (TextUtils.isEmpty(walletAddress)) {
            errorCode = SDKError.WALLET_ADDRESS_IS_EMPTY;
//            errorInfo = SDKError.getMessage(SDKError.WALLET_ADDRESS_IS_EMPTY);
        }
//        else if (ACNAgent.getClient() == null || ACNAgent.getClient().getRepo() == null) {
//            errorInfo = SDKError.getMessage(SDKError.NOT_INITIAL);
//        }

        if (errorCode < 0) {
            ACNAgent.register(ACNSp.getUserId(), null);
            return true;
        } else {
            errorInfo = SDKError.getMessage(errorCode);
            SDKLogger.e(errorInfo);
            finishActivity(Constants.BIND_STATE_UNBOUND, false, errorCode, errorInfo);
            return false;
        }
    }
}
