package io.ttcnet.sdk;

import android.content.Context;
import android.support.multidex.MultiDex;
import android.util.Log;
import com.ttc.behavior.TTCAgent;
import com.ttc.behavior.util.TTCError;

/**
 * Created by lwq on 2018/12/20.
 */
public class Application extends android.app.Application {


    @Override
    public void onCreate() {
        super.onCreate();

        int errCode = TTCAgent.init(this);

        //开发和测试期间请设为false；上线时，改为true;
        TTCAgent.setEnvProd(false);

        if (errCode > 0) {
            String msg = TTCError.getMessage(errCode);
            Log.e("TTC Behavior Demo", msg);
        }
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(base);
    }
}
