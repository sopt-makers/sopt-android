package org.sopt.official

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ProcessLifecycleOwner
import androidx.lifecycle.coroutineScope
import androidx.lifecycle.repeatOnLifecycle
import com.airbnb.mvrx.mocking.MockableMavericks
import com.google.firebase.messaging.FirebaseMessaging
import dagger.hilt.android.HiltAndroidApp
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import org.sopt.official.data.persistence.SoptDataStore
import timber.log.Timber
import javax.inject.Inject

@HiltAndroidApp
class App : Application() {
    @Inject
    lateinit var dataStore: SoptDataStore
    override fun onCreate() {
        super.onCreate()
        initFlipper()
        initMavericks()
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
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
}
