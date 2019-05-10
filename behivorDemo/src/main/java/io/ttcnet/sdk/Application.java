package io.ttcnet.sdk;

import android.content.Context;
import android.support.multidex.MultiDex;
import android.util.Log;
import com.acn.behavior.ACNAgent;
import com.acn.behavior.util.SDKError;

/**
 * Created by lwq on 2018/12/20.
 */
public class Application extends android.app.Application {


    @Override
    public void onCreate() {
        super.onCreate();

        int errCode = ACNAgent.init(this);

        //开发和测试期间请设为false；上线时，改为true;
        //true-采用线上的广告id，false-采用google提供的开发id。如果不设置，默认是true。
        //同时要修改manifest中的admobId
        ACNAgent.setEnvProd(false);

        if (errCode > 0) {
            String msg = SDKError.getMessage(errCode);
            Log.e("ACN Behavior Demo", msg);
        }
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(base);
    }
}
