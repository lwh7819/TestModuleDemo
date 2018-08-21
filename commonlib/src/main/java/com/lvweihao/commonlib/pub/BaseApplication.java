package com.lvweihao.commonlib.pub;

import android.app.Application;
import android.content.Context;

import com.facebook.stetho.Stetho;
import com.lvweihao.commonlib.baseactivity.ActivityLifecycleListener;

/**
 * Created by lv.weihao on 2018/1/15.
 */
public class BaseApplication extends Application {

    public ActivityLifecycleListener lifecycleListener;

    public static Context mContext;

    @Override
    public void onCreate() {
        super.onCreate();

        lifecycleListener = new ActivityLifecycleListener();
        MWClient.init(getBaseContext());
        Stetho.initializeWithDefaults(this);
        registerActivityLifecycleCallbacks(lifecycleListener);


//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
//            StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
//            StrictMode.setVmPolicy(builder.build());
//        }
        mContext = this.getApplicationContext();
    }

    public static Context getAppContext() {
        return mContext;
    }
}
