package com.ttc.sdk.ui;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.ttc.sdk.R;
import com.ttc.sdk.TTCAgent;
import com.ttc.sdk.util.AndroidUtil;
import com.ttc.sdk.util.Constants;
import com.ttc.sdk.web.IManager;
import com.ttc.sdk.util.TTCError;
import com.ttc.sdk.util.TTCKey;
import com.ttc.sdk.util.TTCLogger;
import com.ttc.sdk.util.TTCSp;

public class BindActivity extends Activity {

    private TextView tvTitle;
    private TextView tvWallet;
    private TextView tvApp;
    private TextView tvBind;

    private ImageView ivIconApp;
    private CheckBox cbAuto;

    private String walletAddress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bind);

        initData();
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
        cbAuto = findViewById(R.id.cb_auto);

        setBold(tvTitle);
        setBold(tvWallet);
        setBold(tvApp);
        setBold(tvBind);

        Drawable drawable = AndroidUtil.getApplicationIcon(this);
        ivIconApp.setImageDrawable(drawable);

        tvApp.setText(AndroidUtil.getApplicationName(this));
    }

    private void initData() {
        walletAddress = getIntent().getStringExtra(TTCKey.WALLET_ADDRESS);
        TTCLogger.e("walletAddress=" + walletAddress);
    }

    private void setBold(TextView textView) {
        textView.getPaint().setFakeBoldText(true);
    }


    public void bind(View view) {
        if (isSDKRegisted()) {
            final boolean auto = cbAuto.isChecked();
            TTCAgent.getClient().getRepo().bindApp(walletAddress, auto, new IManager.BindCallback() {
                @Override
                public void onMessage(boolean success, String message) {
                    if (success) {
                        finishActivity(1, auto, "");
                    } else {
                        finishActivity(0, auto, message);
                    }
                }
            });
        }
    }

    public void close(View v) {
        finishActivity(0, false, "");
    }

    private void finishActivity(int bindState, boolean autoTransaction, String errorMsg) {
        Intent data = new Intent();
        if (TTCAgent.getClient() != null) {
            data.putExtra(TTCKey.APP_ID, TTCSp.getAppId(this));
        }
        data.putExtra(TTCKey.BIND_STATE, bindState);
        data.putExtra(TTCKey.AUTO_TRANSACTION, autoTransaction);
        data.putExtra(TTCKey.ERROR_MSG, errorMsg);

        setResult(RESULT_OK, data);
        finish();
    }

    private boolean isSDKRegisted() {
        String errorInfo = "";
        if (TextUtils.isEmpty(TTCSp.getAppId(this))) {
            errorInfo = TTCError.getMessage(TTCError.APP_ID_IS_EMPTY);
        } else if (TextUtils.isEmpty(TTCSp.getSecretKey(this))) {
            errorInfo = TTCError.getMessage(TTCError.SECRET_KEY_IS_EMPTY);
        } else if (TextUtils.isEmpty(TTCSp.getUserId(this))) {
            errorInfo = TTCError.getMessage(TTCError.USER_ID_IS_EMPTY);
        } else if (TextUtils.isEmpty(walletAddress)) {
            errorInfo = TTCError.getMessage(TTCError.WALLET_ADDRESS_IS_EMPTY);
        } else if (TTCAgent.getClient() == null || TTCAgent.getClient().getRepo() == null) {
            errorInfo = TTCError.getMessage(TTCError.NOT_INITIAL);
        }

        if (TextUtils.isEmpty(errorInfo)) {
            return true;
        } else {
            TTCLogger.e(errorInfo);
            finishActivity(Constants.BIND_STATE_UNBOUND, false, errorInfo);
            return false;
        }
    }
}
