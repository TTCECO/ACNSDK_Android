package com.acn.behavior.web;

import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import com.acn.behavior.ACNAgent;
import com.acn.behavior.IManager;
import com.acn.behavior.db.ACNSp;
import com.acn.behavior.db.BehaviorDBHelper;
import com.acn.behavior.util.AlgorithmUtil;
import com.acn.behavior.util.CommonType;
import com.acn.behavior.util.Constants;
import com.acn.behavior.util.Utils;
import com.acn.biz.BizApi;
import com.acn.biz.BizCallback;
import com.acn.biz.model.BaseInfo;
import com.acn.biz.model.BindSucData;

import java.math.BigDecimal;
import java.util.Map;

/**
 * 具体的网络请求
 */
public class Repo {

    private static Handler mainHandler = new Handler(Looper.getMainLooper());

    public void registerUser(final IManager.UserInfoCallback callback) {
        BizApi.getInstance().userRegister(new BizCallback<Map<String, String>>() {
            @Override
            public void success(final Map<String, String> stringStringMap) {
                BizApi.getInstance().getBaseInfo(null);

                if (callback != null) {
                    mainHandler.post(() -> callback.success(stringStringMap));
                }
            }

            @Override
            public void error(final String msg) {

                if (callback != null) {
                    mainHandler.post(() -> callback.error(msg));
                }
            }
        });
    }

    public void updateUser(final Map<String, String> param, final IManager.UserInfoCallback callback) {
        BizApi.getInstance().updateUser(param, new BizCallback<Map<String, String>>() {
            @Override
            public void success(final Map<String, String> stringStringMap) {
                if (callback != null) {
                    mainHandler.post(() -> callback.success(stringStringMap));
                }
            }

            @Override
            public void error(final String msg) {
                if (callback != null) {
                    mainHandler.post(() -> callback.error(msg));
                }
            }
        });
    }

    public void bindApp(String walletAddress, boolean autoTransaction, final IManager.BindCallback callback) {
        BizApi.getInstance().bindApp(walletAddress, autoTransaction, new BizCallback<BindSucData>() {
            @Override
            public void success(final BindSucData bindSucData) {
                if (callback != null) {
                    mainHandler.post(() -> callback.success(bindSucData));
                }
            }

            @Override
            public void error(final String msg) {
                if (callback != null) {
                    mainHandler.post(() -> callback.error(msg));
                }
            }
        });
    }


    public void unbindApp(final IManager.UnbindCallback callback) {
        BizApi.getInstance().unbindApp(new BizCallback<BindSucData>() {
            @Override
            public void success(BindSucData data) {
                if (callback != null) {
                    mainHandler.post(callback::success);
                }
            }

            @Override
            public void error(final String msg) {
                if (callback != null) {
                    mainHandler.post(() -> callback.error(msg));
                }
            }
        });
    }

    public void getWalletBalance(final IManager.BalanceCallback callback) {
        ACNAgent.getClient().getEventExecutorService().execute(() -> {
            String wallet = BaseInfo.getInstance().getWalletAddress();

            try {
                BigDecimal balance = EthClient.getFrozenAvailableAmount(wallet, BaseInfo.getInstance().getAcnContractAddress(), BaseInfo.getInstance().getExchangeContractAddress());

                if (callback != null) {
                    mainHandler.post(() -> {
                        if (balance != null) {
                            callback.success(balance);
                        } else {
                            callback.error("");
                        }
                    });
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

    }

    public void getAppBalance(final IManager.BalanceCallback callback) {
        ACNAgent.getClient().getEventExecutorService().execute(new Runnable() {
            @Override
            public void run() {
                String address = BaseInfo.getInstance().getIndividualAddress();
                try {
                    BigDecimal balance = EthClient.getFrozenAvailableAmount(address, BaseInfo.getInstance().getAcnContractAddress(), BaseInfo.getInstance().getExchangeContractAddress());

                    mainHandler.post(() -> {
                        if (callback == null) {
                            return;
                        }
                        if (balance != null) {
                            callback.success(balance);
                        } else {
                            callback.error("");
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });
    }

    //behaviorTime:<=0 则获取当前时间
    public void onEvent(int behaviorType, String extra, long behaviorTime) {
        if (behaviorType == CommonType.OPEN_DAPP) {
            if (!isNeedUploadOpenBehavior()) {
                return;
            }
        }


        ACNAgent.getClient().getEventExecutorService().execute(() -> {
            long timestamp = behaviorTime;
            if (timestamp <= 0) {
                timestamp = System.currentTimeMillis();
            }

            String hash = AlgorithmUtil.hash(behaviorType, BaseInfo.getInstance().getUserId(), timestamp, extra);
            String data = Utils.addHash2Ufo1OplogMd5(hash);
            String txHash = null;
            try {


                //在发送之前，先存数据库，以防发送过程中崩溃
                ACNAgent.getClient().getDbManager().add(String.valueOf(timestamp), BaseInfo.getInstance().getUserId(), behaviorType, extra, txHash, 0, 0);

                txHash = EthClient.sendTransaction(BaseInfo.getInstance().getSideChainRPCUrl(), BaseInfo.getInstance().getDappActionAddress(), BaseInfo.getInstance().getIndividualAddress(),
                        BaseInfo.getInstance().getPrivateKey(), BaseInfo.getInstance().getGasPrice(), BaseInfo.getInstance().getGasLimit(), data);

                //生成hash后，再存一次，更新
//                ACNAgent.getClient().getDbManager().add(String.valueOf(timestamp), BaseInfo.getInstance().getUserId(), behaviorType, extra, txHash, 0, 0);
                ACNAgent.getClient().getDbManager().updateString(String.valueOf(timestamp), BehaviorDBHelper.HASH, txHash);


                if (!TextUtils.isEmpty(txHash)) {
                    BizApi.getInstance().behaviour(behaviorType, txHash, extra, timestamp, new BizCallback<String>() {
                        @Override
                        public void success(String s) {
                            if (behaviorType == CommonType.OPEN_DAPP) {
                                ACNSp.setLastOpenTimestamp(System.currentTimeMillis());
                            }
                        }

                        @Override
                        public void error(String msg) {

                        }
                    });
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        });

    }

    private boolean isNeedUploadOpenBehavior() {
        long lastOpenDay = ACNSp.getLastOpenTimestamp() / Constants.ONE_DAY_MILLISECOND;
        long currentDay = System.currentTimeMillis() / Constants.ONE_DAY_MILLISECOND;
        return currentDay > lastOpenDay;
    }

}
