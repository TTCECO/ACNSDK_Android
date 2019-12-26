package com.acn.behavior.web;

import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import com.acn.behavior.ACNAgent;
import com.acn.behavior.IManager;
import com.acn.behavior.db.ACNSp;
import com.acn.behavior.model.BehaviorModel;
import com.acn.behavior.util.AlgorithmUtil;
import com.acn.behavior.util.SDKLogger;
import com.acn.behavior.util.Utils;
import com.acn.biz.BizApi;
import com.acn.biz.BizCallback;
import com.acn.biz.model.BaseInfo;
import com.acn.biz.model.BindSucData;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Map;

/**
 * 具体的网络请求
 */
public class Repo {

    private static ArrayList<BehaviorModel> behaviorModelArrayList = new ArrayList<>();
    private static Handler mainHandler = new Handler(Looper.getMainLooper());

    public void registerUser(final IManager.UserInfoCallback callback) {
        BizApi.getInstance().userRegister(new BizCallback<Map<String, String>>() {
            @Override
            public void success(final Map<String, String> stringStringMap) {
                SDKLogger.d("register suc.");
                BizApi.getInstance().getBaseInfo(new BizCallback<BaseInfo>() {
                    @Override
                    public void success(BaseInfo baseInfo) {

                        //have get slaveChain url
                        ACNAgent.getClient().getEventExecutorService().submit(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    ACNSp.setNextNonce(EthClient.getNonce(BaseInfo.getInstance().getSideChainRPCUrl(), BaseInfo.getInstance().getDappActionAddress()));
                                } catch (Exception e) {
                                    SDKLogger.e(e.getMessage());
                                }
                            }
                        });
                    }

                    @Override
                    public void error(String msg) {

                    }
                });

                if (callback != null) {
                    mainHandler.post(() -> callback.success(stringStringMap));
                }
            }

            @Override
            public void error(final String msg) {

                SDKLogger.e("register error." + msg);
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
    //每次调用都用生成一个线程去写链，应该写链用一个线程
    public void onEvent(int type, String content, long behaviorTime) {
        BehaviorModel model = new BehaviorModel();
        model.fromUserId = BaseInfo.getInstance().getUserId();
        model.behaviorType = type;
        model.extra = content;
        model.timestamp = String.valueOf(behaviorTime);
        behaviorModelArrayList.add(model);
    }


    public void startSendTxThread() {
        //生成hash后，再存一次，更新
        Thread myThread = new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    try {
                        if (behaviorModelArrayList.size() > 0) {
                            BehaviorModel des = behaviorModelArrayList.get(0);
                            int behaviorType = des.behaviorType;
                            String extra = des.extra;
                            long timestamp = Long.valueOf(des.timestamp);

                            SDKLogger.d("before send transaction, behaviorType=" + behaviorType + ", extra=" + extra);

                            String hash = AlgorithmUtil.hash(behaviorType, BaseInfo.getInstance().getUserId(), timestamp, extra);
                            String data = Utils.addHash2Ufo1OplogMd5(hash);
                            String txHash = null;

                            txHash = EthClient.sendTransaction(BaseInfo.getInstance().getSideChainRPCUrl(), BaseInfo.getInstance().getDappActionAddress(), BaseInfo.getInstance().getDappActionAddress(),
                                    BaseInfo.getInstance().getPrivateKey(), BaseInfo.getInstance().getGasPrice(), BaseInfo.getInstance().getGasLimit(), data);
                            behaviorModelArrayList.remove(des);

                            if (!TextUtils.isEmpty(txHash)) {
                                int blockNumber = EthClient.getBlockNumber(BaseInfo.getInstance().getSideChainRPCUrl());
                                //生成hash后，再存一次，更新
                                ACNAgent.getClient().getDbManager().updateWriteChainTsHash(String.valueOf(timestamp), String.valueOf(System.currentTimeMillis()), txHash, blockNumber);

                            }
                        }
                        Thread.sleep(100);
                        SDKLogger.d("after send transaction, sleep 100ms");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        myThread.start();
    }

}
