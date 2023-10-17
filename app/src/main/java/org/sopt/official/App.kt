/*
 * Copyright 2022-2023 SOPT - Shout Our Passion Together
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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
