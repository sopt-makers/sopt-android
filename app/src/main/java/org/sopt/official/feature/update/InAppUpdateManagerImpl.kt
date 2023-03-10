package org.sopt.official.feature.update

import android.app.Activity
import android.content.Intent
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.LifecycleOwner
import com.google.android.play.core.appupdate.AppUpdateInfo
import com.google.android.play.core.appupdate.AppUpdateManager
import com.google.android.play.core.install.InstallState
import com.google.android.play.core.install.InstallStateUpdatedListener
import com.google.android.play.core.install.model.AppUpdateType
import com.google.android.play.core.install.model.InstallStatus
import com.google.android.play.core.install.model.UpdateAvailability
import timber.log.Timber
import javax.inject.Inject

class InAppUpdateManagerImpl @Inject constructor(
    private val inAppUpdateManager: AppUpdateManager,
    private val parentActivity: FragmentActivity
) : InAppUpdateManager, InstallStateUpdatedListener {
    private var currentType = AppUpdateType.FLEXIBLE
    private var onUpdateCompleteListener: OnUpdateCompleteListener? = null

    init {
        inAppUpdateManager.appUpdateInfo.addOnSuccessListener { info ->
            val updatePriority = info.updatePriority()
            val stalenessDay = info.clientVersionStalenessDays()
            if (info.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE) { // UPDATE IS AVAILABLE
                if (
                    UpdateCriteria.isUpdatableOf(
                        updatePriority,
                        stalenessDay,
                        AppUpdateType.IMMEDIATE
                    )
                ) {
                    launchUpdateFlow(info, AppUpdateType.IMMEDIATE)
                } else if (
                    UpdateCriteria.isUpdatableOf(
                        updatePriority,
                        stalenessDay,
                        AppUpdateType.FLEXIBLE
                    )
                ) {
                    launchUpdateFlow(info, AppUpdateType.FLEXIBLE)
                } else {
                    return@addOnSuccessListener
                }
            }
        }
        inAppUpdateManager.registerListener(this)
    }

    override fun onResume(owner: LifecycleOwner) {
        super.onResume(owner)
        inAppUpdateManager.appUpdateInfo.addOnSuccessListener { info ->
            if (currentType == AppUpdateType.FLEXIBLE) {
                // If the update is downloaded but not installed, notify the user to complete the update.
                if (info.installStatus() == InstallStatus.DOWNLOADED) {
                    onUpdateCompleteListener?.onComplete()
                }
            } else if (currentType == AppUpdateType.IMMEDIATE) {
                // for AppUpdateType.IMMEDIATE only, already executing updater
                if (info.updateAvailability() == UpdateAvailability.DEVELOPER_TRIGGERED_UPDATE_IN_PROGRESS) {
                    launchUpdateFlow(info, AppUpdateType.IMMEDIATE)
                }
            }
        }
    }

    override fun onDestroy(owner: LifecycleOwner) {
        super.onDestroy(owner)
        inAppUpdateManager.unregisterListener(this)
    }

    private fun launchUpdateFlow(info: AppUpdateInfo, type: Int) {
        inAppUpdateManager.startUpdateFlowForResult(info, type, parentActivity, UPDATE_REQUEST_CODE)
        currentType = type
    }

    override fun setOnFlexibleUpdateCompleted(onUpdateCompleteListener: OnUpdateCompleteListener) {
        this.onUpdateCompleteListener = onUpdateCompleteListener
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == UPDATE_REQUEST_CODE) {
            if (resultCode != Activity.RESULT_OK) {
                // If the update is cancelled or fails, you can request to start the update again.
                Timber.tag("ERROR").e("Update flow failed! Result code: $resultCode")
            }
        }
    }

    override fun onStateUpdate(state: InstallState) {
        if (state.installStatus() == InstallStatus.DOWNLOADED) {
            onUpdateCompleteListener?.onComplete()
        }
    }

    companion object {
        private const val UPDATE_REQUEST_CODE = 3131
    }
}
