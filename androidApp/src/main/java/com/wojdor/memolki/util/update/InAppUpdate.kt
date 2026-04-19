package com.wojdor.memolki.util.update

import android.app.Activity
import com.google.android.play.core.appupdate.AppUpdateManager
import com.google.android.play.core.appupdate.AppUpdateManagerFactory
import com.google.android.play.core.appupdate.AppUpdateOptions
import com.google.android.play.core.install.InstallStateUpdatedListener
import com.google.android.play.core.install.model.AppUpdateType
import com.google.android.play.core.install.model.InstallStatus
import com.google.android.play.core.install.model.UpdateAvailability

class InAppUpdate {

    private lateinit var appUpdateManager: AppUpdateManager
    private lateinit var installStateUpdatedListener: InstallStateUpdatedListener
    fun checkUpdate(activity: Activity) {
        appUpdateManager = AppUpdateManagerFactory.create(activity)
        installStateUpdatedListener = InstallStateUpdatedListener {
            if (it.installStatus() == InstallStatus.DOWNLOADED) {
                completeUpdate()
            }
        }
        appUpdateManager.registerListener(installStateUpdatedListener)
        listenForUpdate(activity)
    }

    fun resumeUpdate(activity: Activity) {
        if (!this::appUpdateManager.isInitialized) return
        appUpdateManager.appUpdateInfo.addOnSuccessListener { appUpdateInfo ->
            if (appUpdateInfo.installStatus() == InstallStatus.DOWNLOADED) {
                completeUpdate()
            } else if (appUpdateInfo.updateAvailability() == UpdateAvailability.DEVELOPER_TRIGGERED_UPDATE_IN_PROGRESS) {
                appUpdateManager.startUpdateFlow(
                    appUpdateInfo,
                    activity,
                    AppUpdateOptions.newBuilder(AppUpdateType.IMMEDIATE).build()
                )
            }
        }
    }

    private fun listenForUpdate(activity: Activity) {
        appUpdateManager.appUpdateInfo.addOnSuccessListener { appUpdateInfo ->
            if (appUpdateInfo.installStatus() == InstallStatus.DOWNLOADED) {
                completeUpdate()
            } else if (appUpdateInfo.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE) {
                val stalenessDays = appUpdateInfo.clientVersionStalenessDays() ?: 0
                if (stalenessDays >= FORCE_UPDATE_STALENESS_DAYS &&
                    appUpdateInfo.isUpdateTypeAllowed(AppUpdateType.IMMEDIATE)
                ) {
                    appUpdateManager.startUpdateFlow(
                        appUpdateInfo,
                        activity,
                        AppUpdateOptions.newBuilder(AppUpdateType.IMMEDIATE).build()
                    )
                } else if (appUpdateInfo.isUpdateTypeAllowed(AppUpdateType.FLEXIBLE)) {
                    appUpdateManager.startUpdateFlow(
                        appUpdateInfo,
                        activity,
                        AppUpdateOptions.newBuilder(AppUpdateType.FLEXIBLE).build()
                    )
                }
            }
        }
    }

    private fun completeUpdate() {
        appUpdateManager.completeUpdate()
    }

    fun cleanup() {
        if (this::appUpdateManager.isInitialized) {
            appUpdateManager.unregisterListener(installStateUpdatedListener)
        }
    }

    companion object {
        private const val FORCE_UPDATE_STALENESS_DAYS = 14
    }
}
