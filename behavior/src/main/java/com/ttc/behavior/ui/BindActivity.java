package com.ttc.behavior.ui;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.ttc.behavior.IManager;
import com.ttc.behavior.R;
import com.ttc.behavior.TTCAgent;
import com.ttc.behavior.db.TTCSp;
import com.ttc.behavior.util.*;
import com.ttc.biz.model.BindSucData;

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
        walletAddress = getIntent().getStringExtra(TTCKey.WALLET_ADDRESS);
        bindReward = getIntent().getIntExtra(TTCKey.BIND_REWARD, 0);
        TTCLogger.e("walletAddress=" + walletAddress);
    }

    private void setBold(TextView textView) {
        textView.getPaint().setFakeBoldText(true);
    }


    public void bind(View view) {
        if (isSDKRegisted()) {
            final boolean auto = true;
            TTCAgent.getClient().getRepo().bindApp(walletAddress, auto, new IManager.BindCallback() {
                @Override
                public void success(BindSucData data) {
                    bindReward = data.reward;
                    finishActivity(1, auto, "");
                }

                @Override
                public void error(String msg) {
                    finishActivity(0, auto, msg);
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
            data.putExtra(TTCKey.APP_ID, TTCSp.getAppId());
        }
        data.putExtra(TTCKey.BIND_STATE, bindState);
        data.putExtra(TTCKey.AUTO_TRANSACTION, autoTransaction);
        data.putExtra(TTCKey.ERROR_MSG, errorMsg);
        data.putExtra(TTCKey.BIND_REWARD, bindReward);

        setResult(RESULT_OK, data);
        finish();
    }

    private boolean isSDKRegisted() {
        String errorInfo = "";
        if (TextUtils.isEmpty(TTCSp.getAppId())) {
            errorInfo = TTCError.getMessage(TTCError.APP_ID_IS_EMPTY);
        } else if (TextUtils.isEmpty(TTCSp.getSecretKey())) {
            errorInfo = TTCError.getMessage(TTCError.SECRET_KEY_IS_EMPTY);
        } else if (TextUtils.isEmpty(TTCSp.getUserId())) {
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
