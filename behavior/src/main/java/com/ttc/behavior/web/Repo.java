package com.ttc.behavior.web;

import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import com.ttc.behavior.IManager;
import com.ttc.behavior.TTCAgent;
import com.ttc.behavior.command.BehaviorReq;
import com.ttc.behavior.command.WalletBalanceCommand;
import com.ttc.behavior.command.base.Command;
import com.ttc.behavior.db.TTCSp;
import com.ttc.behavior.util.CommonType;
import com.ttc.behavior.util.Constants;
import com.ttc.biz.BizApi;
import com.ttc.biz.BizCallback;
import com.ttc.biz.model.BaseInfo;
import com.ttc.biz.model.BindSucData;

import java.math.BigDecimal;
import java.util.Map;

/**
 * 具体的网络请求
 */
public class Repo {

    private static Handler mainHandler = new Handler(Looper.getMainLooper());

    public void getBaseInfo() {
        BizApi.getBaseInfo(TTCAgent.getClient().getContext(), new BizCallback<BaseInfo>() {
            @Override
            public void success(BaseInfo baseInfo) {
                if (baseInfo != null) {

                    if (!TextUtils.isEmpty(baseInfo.getSideChainRPCUrl())) {
                        Constants.SIDE_CHAIN_RPC_URL = baseInfo.getSideChainRPCUrl();
                    }
                    if (!TextUtils.isEmpty(baseInfo.getMainChainRPCUrl())) {
                        Constants.MAIN_CHAIN_RPC_URL = baseInfo.getMainChainRPCUrl();
                    }
                }
            }

            @Override
            public void error(String msg) {

            }
        });
    }

    public void registerUser(final IManager.UserInfoCallback callback) {
        BizApi.userRegister(TTCAgent.getClient().getContext(), new BizCallback<Map<String, String>>() {
            @Override
            public void success(final Map<String, String> stringStringMap) {
                if (callback != null) {
                    mainHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            callback.success(stringStringMap);
                        }
                    });
                }
            }

            @Override
            public void error(final String msg) {

                if (callback != null) {
                    mainHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            callback.error(msg);
                        }
                    });
                }
            }
        });
    }

    public void updateUser(final Map<String, String> param, final IManager.UserInfoCallback callback) {
        BizApi.updateUser(TTCAgent.getClient().getContext(), param, new BizCallback<Map<String, String>>() {
            @Override
            public void success(final Map<String, String> stringStringMap) {
                if (callback != null) {
                    mainHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            callback.success(stringStringMap);
                        }
                    });
                }
            }

            @Override
            public void error(final String msg) {
                if (callback != null) {
                    mainHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            callback.error(msg);
                        }
                    });
                }
            }
        });
    }

    public void bindApp(String walletAddress, boolean autoTransaction, final IManager.BindCallback callback) {
        BizApi.bindApp(TTCAgent.getClient().getContext(), walletAddress, autoTransaction, new BizCallback<BindSucData>() {
            @Override
            public void success(final BindSucData bindSucData) {
                if (callback != null) {
                    mainHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            callback.success(bindSucData);
                        }
                    });
                }
            }

            @Override
            public void error(final String msg) {
                if (callback != null) {
                    mainHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            callback.error(msg);
                        }
                    });
                }
            }
        });
    }


    public void unbindApp(final IManager.UnbindCallback callback) {
        BizApi.unbindApp(TTCAgent.getClient().getContext(), new BizCallback<String>() {
            @Override
            public void success(String s) {
                if (callback != null) {
                    mainHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            callback.success();
                        }
                    });
                }
            }

            @Override
            public void error(final String msg) {
                if (callback != null) {
                    mainHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            callback.error(msg);

                        }
                    });
                }
            }
        });
    }

    public void getWalletBalance(final IManager.BalanceCallback callback) {
        Command<BigDecimal> command = new WalletBalanceCommand();
        command.execute(new Callback<BigDecimal>() {
            @Override
            public void success(final BigDecimal balance) {
                if (callback != null) {
                    mainHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            callback.success(balance);

                        }
                    });
                }
            }

            @Override
            public void error(final Throwable e) {
                if (callback != null) {
                    mainHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            callback.error(e.getMessage());

                        }
                    });
                }
            }
        });
    }

    public void getAppBalance(final IManager.BalanceCallback callback) {
        TTCAgent.getClient().getEventExecutorService().execute(new Runnable() {
            @Override
            public void run() {
                String address = BizApi.getIndividualAddress(TTCAgent.getClient().getContext());
                final BigDecimal balance = EthClient.getBalance(address);

                mainHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        if (callback == null) {
                            return;
                        }
                        if (balance != null) {
                            callback.success(balance);
                        } else {
                            callback.error("");
                        }
                    }
                });
            }
        });
    }

    public void onEvent(int behaviorType, String extra) {
        if (behaviorType == CommonType.OPEN_DAPP) {
            if (!isNeedUploadOpenBehavior()) {
                return;
            }
        }
        BehaviorReq.doIt(behaviorType, extra);
    }

    private boolean isNeedUploadOpenBehavior() {
        long lastOpenDay = TTCSp.getLastOpenTimestamp() / Constants.ONE_DAY_MILLISECOND;
        long currentDay = System.currentTimeMillis() / Constants.ONE_DAY_MILLISECOND;
        return currentDay > lastOpenDay;
    }

}
