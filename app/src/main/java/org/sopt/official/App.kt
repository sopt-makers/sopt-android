package org.sopt.official

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import androidx.hilt.work.HiltWorkerFactory
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ProcessLifecycleOwner
import androidx.lifecycle.coroutineScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.work.Configuration
import com.airbnb.mvrx.mocking.MockableMavericks
import com.google.firebase.messaging.FirebaseMessaging
import dagger.hilt.android.HiltAndroidApp
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import org.sopt.official.data.persistence.SoptDataStore
import org.sopt.official.widget.AttendanceTaskManager
import timber.log.Timber
import javax.inject.Inject

@HiltAndroidApp
class App : Application(), Configuration.Provider {

    @Inject
    lateinit var dataStore: SoptDataStore

    @Inject
    lateinit var workerFactory: HiltWorkerFactory

    @Inject
    lateinit var attendanceTaskManager: AttendanceTaskManager

    override fun onCreate() {
        super.onCreate()
        initFlipper()
        initMavericks()
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        attendanceTaskManager.enqueue()
        ProcessLifecycleOwner.get().lifecycle.coroutineScope.launch {
            ProcessLifecycleOwner.get().lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                runCatching {
                    FirebaseMessaging.getInstance().token.await()
                }.onSuccess {
                    dataStore.pushToken = it
                }.onFailure(Timber::e)
            }
        }
    }

    private fun initMavericks() {
        MockableMavericks.initialize(this)
    }

    private fun initFlipper() {
        FlipperInitializer.init(this)
    }

    override fun getWorkManagerConfiguration() = Configuration.Builder()
        .setWorkerFactory(workerFactory)
        .setMinimumLoggingLevel(android.util.Log.VERBOSE)
        .build()
}
