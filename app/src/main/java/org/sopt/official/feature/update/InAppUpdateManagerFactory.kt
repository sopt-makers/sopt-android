package org.sopt.official.feature.update

import android.app.Activity
import com.google.android.play.core.appupdate.AppUpdateManager
import javax.inject.Inject

class InAppUpdateManagerFactory @Inject constructor(
    private val inAppUpdateManager: AppUpdateManager
) {
    fun create(activity: Activity): InAppUpdateManager {
        return InAppUpdateManagerImpl(inAppUpdateManager, activity)
    }
}
