package io.ttcnet.sdk.activity;

import android.content.Context;
import android.content.Intent;
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
import com.acn.behavior.ACNAgent;
import com.acn.behavior.ACNConfigure;
import com.acn.behavior.IManager;
import com.acn.behavior.util.ACNKey;
import com.acn.behavior.util.SDKError;
import io.ttcnet.sdk.R;
import io.ttcnet.sdk.utils.IntentKey;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

public class BehaviorActivity extends AppCompatActivity {

    private final int WHAT_GET_EXCHANGE_RATE_SUC = 1;


    private Context context = this;
    private Map<String, String> updateInfo = new HashMap<>();
    private boolean isLogOn = true;
    private boolean isSdkFunOn = true;
    private MyHandler handler = new MyHandler();

    private TextView tvMsg;
    private Button btnLog;
    private Button btnSdkFun;
    //    private TextView tvDisplayUpdateInfo;
    private EditText etUpdateKey;
    private EditText etUpdateValue;
    private Button btnUpdateAdd;
    //    private EditText etBehaviorType;
//    private EditText etBehaviorExtra;
    private Button btnLike;
    private Button btnComment;
    private Button btn1;
    private Button btn2;
    private Button btn3;
    private Button btn4;
    private Button btn5;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_behavior);

        ACNAgent.configure(new ACNConfigure.Builder().logEnabled(true).build());

//        tvEnv = findViewById(R.id.main_env);
        tvMsg = findViewById(R.id.main_msg_tv);
//        etUserId = findViewById(R.id.main_user_id_et);
        btnLog = findViewById(R.id.main_log_btn);
        btnSdkFun = findViewById(R.id.main_sdk_fun);
//        tvDisplayUpdateInfo = findViewById(R.id.main_display_update_info_tv);
        etUpdateKey = findViewById(R.id.main_update_info_key);
        etUpdateValue = findViewById(R.id.main_update_info_value);
        btnUpdateAdd = findViewById(R.id.main_update_info_add);
//        etBehaviorType = findViewById(R.id.main_behavior_type);
//        etBehaviorExtra = findViewById(R.id.main_behavior_extra);
        btnLike = findViewById(R.id.like_btn);
        btnComment = findViewById(R.id.comment_btn);
        btn1 = findViewById(R.id.behavior1);
        btn2 = findViewById(R.id.behavior2);
        btn3 = findViewById(R.id.behavior3);
        btn4 = findViewById(R.id.behavior4);
        btn5 = findViewById(R.id.behavior5);


        btnLike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                behavior(113, "like");
            }
        });

        btnComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                behavior(114, "comment");
            }
        });

        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                behavior(1, "1");
            }
        });

        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                behavior(2, "2");
            }
        });

        btn3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                behavior(3, "3");
            }
        });

        btn4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                behavior(4, "4");
            }
        });

        btn5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                behavior(5, "5");
            }
        });


        btnLog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!isLogOn) {
                    ACNAgent.configure(new ACNConfigure.Builder().logEnabled(true).build());
                    isLogOn = true;
                    btnLog.setText(R.string.close_log_switch);
                    tvMsg.setVisibility(View.VISIBLE);
                } else {
                    ACNAgent.configure(new ACNConfigure.Builder().logEnabled(false).build());
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
                    ACNAgent.configure(new ACNConfigure.Builder().serverEnabled(true).build());
                    isSdkFunOn = true;
                    btnSdkFun.setText(R.string.close_sdk_fun_switch);
                } else {
                    ACNAgent.configure(new ACNConfigure.Builder().serverEnabled(false).build());
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

        tvMsg.setText(getIntent().getStringExtra(IntentKey.CONTENT));

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case 1:
                if (resultCode == RESULT_OK) {
                    if (data != null) {
                        boolean isBindSuc = data.getBooleanExtra(ACNKey.BIND_STATE, false);
                        int reward = data.getIntExtra(ACNKey.BIND_REWARD, 0);
                        String rewardSymbol = data.getStringExtra(ACNKey.BIND_REWARD_SYMBOL);
                        String walletAddress = data.getStringExtra(ACNKey.WALLET_ADDRESS);
                        String errMsg = data.getStringExtra(ACNKey.ERROR_MSG);
                        if (isBindSuc) {
                            tvMsg.setText("bind success.");
                            if (reward > 0) {
                                tvMsg.append("reward:" + reward + rewardSymbol);
                            }
                        } else {
                            tvMsg.setText("bind failure");
                        }
                    }
                }
                break;
        }
        if (requestCode == 10) {
            Toast.makeText(context, "refresh order ", Toast.LENGTH_SHORT).show();
        }
    }

    private void addUpdateInfo() {
//        tvDisplayUpdateInfo.setText("");

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
//        tvDisplayUpdateInfo.setText(updateInfo.toString());
        etUpdateKey.setText("");
        etUpdateValue.setText("");
        etUpdateKey.requestFocus();

        updateUser();
    }


    public void updateUser() {
        if (updateInfo.isEmpty()) {
            tvMsg.setText("update info is empty");
            return;
        }

//        tvDisplayUpdateInfo.setText("");

        ACNAgent.updateUserInfo(updateInfo, new IManager.UserInfoCallback() {
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
//        tvDisplayUpdateInfo.setText("");


        ACNAgent.unregister();
        setTvMsg("unregister successfully", "");
    }

    public void login(View view) {
//        tvDisplayUpdateInfo.setText("");


//        String behaviorExtra = etBehaviorExtra.getText().toString();
        ACNAgent.onEvent(112, "");
        setTvMsg("login", "behaviorType:112, behaviorExtra");
    }


    public void behavior(int behaviorType, String extra) {
//        tvDisplayUpdateInfo.setText("");

//        int behaviorType = 0;
//        String behaviorExtra = etBehaviorExtra.getText().toString();
//        try {
//            behaviorType = Integer.parseInt(etBehaviorType.getText().toString());
//        } catch (NumberFormatException e) {
//            e.printStackTrace();
//        }
        int errCode = ACNAgent.onEvent(behaviorType, extra);
        if (errCode > 0) {
            setTvMsg(SDKError.getMessage(errCode), "");
        } else {
            setTvMsg("behavior is sent", "behaviorType:" + behaviorType + ", behaviorExtra:" + extra);
        }
    }

    public void getBoundAddress(View view) {
        String address = ACNAgent.getBoundWalletAddress();
        tvMsg.setText("boundAddress:" + address);
    }


    //在调用的app中如是写
    public void bind(View v) {
        ACNAgent.bindApp(this, "http://img.freepik.com/free-icon/facebook-logo-button_318-84980.jpg?size=158c&ext=jpg", 1);
    }

    public void unbind(View v) {

        ACNAgent.unbindApp(new IManager.UnbindCallback() {
            @Override
            public void success() {
                setTvMsg("unbind successfully", "");
            }

            @Override
            public void error(String msg) {
                setTvMsg("unbind error", msg);
            }
        });
    }

    public void walletBalance(View v) {

        ACNAgent.getWalletBalance(new IManager.BalanceCallback() {
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

        ACNAgent.getAppBalance(new IManager.BalanceCallback() {
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
