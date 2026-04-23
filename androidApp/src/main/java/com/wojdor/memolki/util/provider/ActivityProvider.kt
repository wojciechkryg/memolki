package com.wojdor.memolki.util.provider

import android.app.Activity
import android.app.Application
import android.content.Context
import android.os.Bundle
import java.lang.ref.WeakReference

open class ActivityProvider(context: Context) {

    private var activityRef: WeakReference<Activity>? = null

    val current: Activity? get() = activityRef?.get()

    init {
        (context.applicationContext as? Application)?.registerActivityLifecycleCallbacks(
            object : Application.ActivityLifecycleCallbacks {
                override fun onActivityResumed(activity: Activity) {
                    activityRef = WeakReference(activity)
                }

                override fun onActivityPaused(activity: Activity) {
                    if (activityRef?.get() === activity) activityRef = null
                }

                override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) = Unit
                override fun onActivityStarted(activity: Activity) = Unit
                override fun onActivityStopped(activity: Activity) = Unit
                override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) = Unit
                override fun onActivityDestroyed(activity: Activity) = Unit
            }
        )
    }
}
