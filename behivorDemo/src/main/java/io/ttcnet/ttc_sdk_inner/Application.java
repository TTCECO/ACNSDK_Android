package io.ttcnet.ttc_sdk_inner;

import android.content.Context;
import android.support.multidex.MultiDex;
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
        if (errCode > 0) {
            String msg = TTCError.getMessage(errCode);
        }
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(base);
    }
}
