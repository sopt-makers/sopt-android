/*
 * MIT License
 * Copyright 2023-2024 SOPT - Shout Our Passion Together
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package org.sopt.official.data.mypage.local

import android.app.Application
import android.content.SharedPreferences
import androidx.core.content.edit
import dev.zacsweers.metro.Inject
import org.sopt.official.common.di.SoptampStoreKey
import org.sopt.official.common.file.createSharedPreference

@Inject
class SoptampDataStore(
    private val application: Application,
    private val soptampStoreKey: SoptampStoreKey,
) {
    private val dataStore = createSharedPreference(soptampStoreKey.value, application.applicationContext)

    var userId: Int
        get() = dataStore.getInt("user_id", -1)
        set(value) = dataStore.edit { putInt("user_id", value) }

    var generation: Int
        get() = dataStore.getInt("generation", -1)
        set(value) = dataStore.edit { putInt("generation", value) }

    var profileMessage: String
        get() = dataStore.getString("profile_message", "") ?: "String"
        set(value) = dataStore.edit { putString("profile_message", value) }

    var nickname: String
        get() = dataStore.getString("nickname", "") ?: "String"
        set(value) = dataStore.edit { putString("nickname", value) }

    var isOnboardingSeen: Boolean
        get() = dataStore.getBoolean("is_onboarding_seen", false)
        set(value) = dataStore.edit { putBoolean("is_onboarding_seen", value) }

    fun clear() {
        userId = -1
        generation = -1
        profileMessage = ""
        nickname = ""
    }
}
