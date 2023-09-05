package org.sopt.official.widget

import android.content.Context
import android.util.Log
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import org.sopt.official.data.service.attendance.AttendanceService
import retrofit2.HttpException
import timber.log.Timber

@HiltWorker
class AttendanceWorker @AssistedInject constructor(
    @Assisted private val context: Context,
    @Assisted params: WorkerParameters,
    private val service: AttendanceService
) : CoroutineWorker(context, params) {
    override suspend fun doWork(): Result {
        runCatching {
            Log.d("Nunu", "request")
            service.getAttendanceHistory().data?.toEntity()
        }.onSuccess {
            Log.d("Nunu", "success $it")
            AttendanceWidgetReceiver.updateWidget(
                score = it?.userInfo?.attendancePoint?.toDouble() ?: AttendanceWidget.DEFAULT_VALUE,
                context = context
            )
            return Result.success()
        }.onFailure {
            Log.d("Nunu", "failure $it")
            if (it is HttpException) {
                if (it.code() == 401) {
                    AttendanceWidgetReceiver.updateWidget(
                        score = AttendanceWidget.INVALID_TOKEN_ERROR,
                        context = context
                    )
                } else {
                    AttendanceWidgetReceiver.updateWidget(
                        score = AttendanceWidget.NETWORK_ERROR,
                        context = context
                    )
                }
                return Result.retry()
            }
            return Result.retry()
        }
        return Result.retry()
    }

    companion object {
        const val TAG = "attendance_worker"
    }
}
