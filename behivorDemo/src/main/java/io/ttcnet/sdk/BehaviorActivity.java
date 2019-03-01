package io.ttcnet.sdk;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.ttc.behavior.IManager;
import com.ttc.behavior.TTCAgent;
import com.ttc.behavior.TTCConfigure;
import com.ttc.behavior.util.TTCError;
import com.ttc.behavior.util.TTCKey;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

public class BehaviorActivity extends AppCompatActivity {

    private final int WHAT_GET_EXCHANGE_RATE_SUC = 1;


    private Context context = this;
    private Map<String, String> updateInfo = new HashMap<>();
    private boolean isLogOn = false;
    private boolean isSdkFunOn = true;
    private MyHandler handler = new MyHandler();


    private TextView tvMsg;
    private Button btnLog;
    private Button btnSdkFun;
    private TextView tvDisplayUpdateInfo;
    private EditText etUpdateKey;
    private EditText etUpdateValue;
    private Button btnUpdateAdd;
    private EditText etBehaviorType;
    private EditText etBehaviorExtra;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_behavior);

        TTCAgent.configure(new TTCConfigure.Builder().logEnabled(true).build());

//        tvEnv = findViewById(R.id.main_env);
        tvMsg = findViewById(R.id.main_msg_tv);
//        etUserId = findViewById(R.id.main_user_id_et);
        btnLog = findViewById(R.id.main_log_btn);
        btnSdkFun = findViewById(R.id.main_sdk_fun);
        tvDisplayUpdateInfo = findViewById(R.id.main_display_update_info_tv);
        etUpdateKey = findViewById(R.id.main_update_info_key);
        etUpdateValue = findViewById(R.id.main_update_info_value);
        btnUpdateAdd = findViewById(R.id.main_update_info_add);
        etBehaviorType = findViewById(R.id.main_behavior_type);
        etBehaviorExtra = findViewById(R.id.main_behavior_extra);

        btnLog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!isLogOn) {
                    TTCAgent.configure(new TTCConfigure.Builder().logEnabled(true).build());
                    isLogOn = true;
                    btnLog.setText(R.string.close_log_switch);
                    tvMsg.setVisibility(View.VISIBLE);
                } else {
                    TTCAgent.configure(new TTCConfigure.Builder().logEnabled(false).build());
                    isLogOn = false;
                    btnLog.setText(R.string.open_log_switch);
                    tvMsg.setVisibility(View.INVISIBLE);
                }
            }
        });

        btnSdkFun.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!isSdkFunOn) {
                    TTCAgent.configure(new TTCConfigure.Builder().serverEnabled(true).build());
                    isSdkFunOn = true;
                    btnSdkFun.setText(R.string.close_sdk_fun_switch);
                } else {
                    TTCAgent.configure(new TTCConfigure.Builder().serverEnabled(false).build());
                    isSdkFunOn = false;
                    btnSdkFun.setText(R.string.open_sdk_fun_switch);
                }
            }
        });

        btnUpdateAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addUpdateInfo();
            }
        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 10) {
            Toast.makeText(context, "refresh order ", Toast.LENGTH_SHORT).show();
        }
    }

    private void addUpdateInfo() {
        tvDisplayUpdateInfo.setText("");

        String key = etUpdateKey.getText().toString();
        String value = etUpdateValue.getText().toString();
        if (TextUtils.isEmpty(key)) {
            setTvMsg("error", "key is empty");
            return;
        }
        if (TextUtils.isEmpty(value)) {
            setTvMsg("error", "value is empty");
            return;
        }

        updateInfo.put(key, value);
        tvDisplayUpdateInfo.setText(updateInfo.toString());
        etUpdateKey.setText("");
        etUpdateValue.setText("");
        etUpdateKey.requestFocus();
    }


    public void updateUser(View v) {
        if (updateInfo.isEmpty()) {
            tvMsg.setText("update info is empty");
            return;
        }

        tvDisplayUpdateInfo.setText("");

        TTCAgent.updateUserInfo(updateInfo, new IManager.UserInfoCallback() {
            @Override
            public void success(Map<String, String> map) {
                setTvMsg("update successfully", map.toString());
                updateInfo.clear();
            }

            @Override
            public void error(String msg) {
                setTvMsg("update error", msg);
                updateInfo.clear();
            }
        });

    }

    public void unregister(View v) {
        tvDisplayUpdateInfo.setText("");


        TTCAgent.unregister();
        setTvMsg("unregister successfully", "");
    }

    public void login(View view) {
        tvDisplayUpdateInfo.setText("");


        String behaviorExtra = etBehaviorExtra.getText().toString();
        TTCAgent.onEvent(112, behaviorExtra);
        setTvMsg("login", "behaviorType:112, behaviorExtra:" + behaviorExtra);
    }


    public void behavior(View v) {
        tvDisplayUpdateInfo.setText("");

        int behaviorType = 0;
        String behaviorExtra = etBehaviorExtra.getText().toString();
        try {
            behaviorType = Integer.parseInt(etBehaviorType.getText().toString());
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        int errCode = TTCAgent.onEvent(behaviorType, behaviorExtra);
        if (errCode > 0) {
            setTvMsg(TTCError.getMessage(errCode), "");
        } else {
            setTvMsg("behavior is sent", "behaviorType:" + behaviorType + ", behaviorExtra:" + behaviorExtra);
        }
    }


    //在调用的app中如是写
    public void bind(View v) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(BuildConfig.APPLICATION_ID + "://"));
        intent.putExtra(TTCKey.WALLET_ADDRESS, "0xa6aAc51BE7Cd96412896d5EFa2B8C0Cf72d340Ba");
        startActivity(intent);
    }

    public void unbind(View v) {
        tvDisplayUpdateInfo.setText("");

        TTCAgent.unbindApp(new IManager.BindCallback() {
            @Override
            public void onMessage(boolean success, String message) {
                if (success) {
                    setTvMsg("unbind successfully", message);
                } else {
                    setTvMsg("unbind error", message);
                }
            }
        });
    }

    public void walletBalance(View v) {
        tvDisplayUpdateInfo.setText("");

        TTCAgent.getWalletBalance(new IManager.BalanceCallback() {
            @Override
            public void success(BigDecimal balance) {
                setTvMsg("get wallet balance successfully", "the wallet balance:" + balance.toString());
            }

            @Override
            public void error(String msg) {
                setTvMsg("get wallet balance error", msg);
            }
        });
    }

    public void appBalance(View v) {
        tvDisplayUpdateInfo.setText("");


        TTCAgent.getAppBalance(new IManager.BalanceCallback() {
            @Override
            public void success(BigDecimal balance) {
                setTvMsg("get app balance successfully", "the app balance:" + balance.toString());
            }

            @Override
            public void error(String msg) {
                setTvMsg("get app balance error", msg);
            }
        });
    }

    public void setTvMsg(String status, String msg) {
        tvMsg.setText(status + "\n" + msg);
    }


    class MyHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {

            }
        }
    }


}
