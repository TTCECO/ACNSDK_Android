package com.ttc.sdk.web;

import android.os.Handler;
import android.os.Looper;

import com.ttc.biz.http.BizApi;
import com.ttc.biz.http.BizCallback;
import com.ttc.sdk.IManager;
import com.ttc.sdk.TTCAgent;
import com.ttc.sdk.command.AppBalanceCommand;
import com.ttc.sdk.command.EventCommand;
import com.ttc.sdk.command.WalletBalanceCommand;
import com.ttc.sdk.command.base.Command;
import com.ttc.sdk.util.CommonType;
import com.ttc.sdk.util.Constants;
import com.ttc.sdk.util.TTCSp;

import java.math.BigDecimal;
import java.util.Map;

/**
 * 具体的网络请求
 */
public class Repo {

    private static Handler mainHandler = new Handler(Looper.getMainLooper());

    public void getPrivateKey() {
        BizApi.getBaseInfo(TTCAgent.getClient().getContext(), null);
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
        BizApi.bindApp(TTCAgent.getClient().getContext(), walletAddress, autoTransaction, new BizCallback<String>() {
            @Override
            public void success(final String s) {
                if (callback != null) {
                    mainHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            callback.onMessage(true, s);

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
                            callback.onMessage(false, msg);
                        }
                    });
                }
            }
        });
    }

    public void unbindApp(final IManager.BindCallback callback) {
        BizApi.unbindApp(TTCAgent.getClient().getContext(), new BizCallback<String>() {
            @Override
            public void success(final String s) {
                if (callback != null) {
                    mainHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            callback.onMessage(true, s);

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
                            callback.onMessage(false, msg);

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
        new AppBalanceCommand().execute(new Callback<BigDecimal>() {
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

    public void onEvent(int behaviorType, String extra) {
        if (behaviorType == CommonType.OPEN_DAPP) {
            if (!isNeedUploadOpenBehavior()) {
                return;
            }
        }
        Command command = new EventCommand(behaviorType, extra);
        command.execute(TTCAgent.getClient().getEventExecutorService());
    }

    private boolean isNeedUploadOpenBehavior() {
        int lastOpenDay = (int) (TTCSp.getLastOpenTimestamp() / Constants.ONE_DAY_MILLISECOND);
        long currentDay = (int) (System.currentTimeMillis() / Constants.ONE_DAY_MILLISECOND);
        return currentDay > lastOpenDay;
    }

}
