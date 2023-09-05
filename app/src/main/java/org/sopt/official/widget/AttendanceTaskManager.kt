package org.sopt.official.widget

import androidx.work.Constraints
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.NetworkType
import androidx.work.PeriodicWorkRequest
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class AttendanceTaskManager @Inject constructor(
    private val workManager: WorkManager
) {
    fun enqueue() {
        enqueueWorker()
    }

    private fun enqueueWorker() {
        workManager.enqueueUniquePeriodicWork(
            AttendanceWorker.TAG,
            ExistingPeriodicWorkPolicy.UPDATE,
            buildRequest()
        )
    }

    private fun buildRequest(): PeriodicWorkRequest {
        return PeriodicWorkRequestBuilder<AttendanceWorker>(900000, TimeUnit.MILLISECONDS)
            .addTag(AttendanceWorker.TAG)
            .setConstraints(getDRMConstraints())
            .build()
    }

    companion object {
        private fun getDRMConstraints(): Constraints {
            return Constraints.Builder()
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .build()
        }
    }
}
