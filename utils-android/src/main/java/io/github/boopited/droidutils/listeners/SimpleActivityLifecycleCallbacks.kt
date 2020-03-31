package io.github.boopited.droidutils.listeners

import android.app.Activity
import android.app.Application
import android.os.Bundle

interface SimpleActivityLifecycleCallbacks : Application.ActivityLifecycleCallbacks {

    override fun onActivityCreated(activity: Activity, bundle: Bundle?) {
        //Override if necessary
    }

    override fun onActivityStarted(activity: Activity) {
        //Override if necessary
    }

    override fun onActivityResumed(activity: Activity) {
        //Override if necessary
    }

    override fun onActivityPaused(activity: Activity) {
        //Override if necessary
    }

    override fun onActivityStopped(activity: Activity) {
        //Override if necessary
    }

    override fun onActivitySaveInstanceState(activity: Activity, bundle: Bundle?) {
        //Override if necessary
    }

    override fun onActivityDestroyed(activity: Activity) {
        //Override if necessary
    }

}