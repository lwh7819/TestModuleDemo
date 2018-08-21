package com.lvweihao.commonlib.baseactivity;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;

public class ActivityLifecycleListener implements Application.ActivityLifecycleCallbacks {
    private int refCount = 0;
    private RunOnBackground listener;

    public void setListener(RunOnBackground listener) {
        this.listener = listener;
    }

    public void unListener(RunOnBackground listener) {
        listener = null;
    }

    @Override
    public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
    }

    @Override
    public void onActivityStarted(Activity activity) {
        refCount++;
    }

    @Override
    public void onActivityResumed(Activity activity) {
    }

    @Override
    public void onActivityPaused(Activity activity) {
    }

    @Override
    public void onActivityStopped(Activity activity) {
        refCount--;
        if (refCount == 0) {
            //后台运行
            if (listener != null) {
                listener.isBackground(true);
            }
        } else {
            if (listener != null) {
                listener.isBackground(false);
            }
        }
    }

    @Override
    public void onActivitySaveInstanceState(Activity activity, Bundle outState) {
    }

    @Override
    public void onActivityDestroyed(Activity activity) {
    }
}
