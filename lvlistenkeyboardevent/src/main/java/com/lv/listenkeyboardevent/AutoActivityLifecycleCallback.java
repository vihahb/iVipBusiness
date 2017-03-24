package com.lv.listenkeyboardevent;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;

/**
 * Created by Vulcl on 3/24/2017
 */

public abstract class AutoActivityLifecycleCallback implements Application.ActivityLifecycleCallbacks {
    private final Activity mTargetActivity;

    public AutoActivityLifecycleCallback(Activity targetActivity) {
        mTargetActivity = targetActivity;
    }

    @Override
    public void onActivityCreated(Activity activity, Bundle bundle) {

    }

    @Override
    public void onActivityStarted(Activity activity) {

    }

    @Override
    public void onActivityResumed(Activity activity) {

    }

    @Override
    public void onActivityPaused(Activity activity) {

    }

    @Override
    public void onActivityStopped(Activity activity) {

    }

    @Override
    public void onActivitySaveInstanceState(Activity activity, Bundle bundle) {

    }

    @Override
    public void onActivityDestroyed(Activity activity) {
        if (activity == mTargetActivity) {
            mTargetActivity.getApplication().unregisterActivityLifecycleCallbacks(this);
            onTargetActivityDestroyed();
        }
    }

    protected abstract void onTargetActivityDestroyed();
}