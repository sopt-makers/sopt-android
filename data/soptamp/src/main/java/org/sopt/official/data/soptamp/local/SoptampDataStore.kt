/*
 * Copyright 2023 SOPT - Shout Our Passion Together
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
package org.sopt.official.data.soptamp.local

import android.content.SharedPreferences
import androidx.core.content.edit
import org.sopt.official.domain.soptamp.constant.Soptamp
import javax.inject.Inject

class SoptampDataStore @Inject constructor(
    @Soptamp private val dataStore: SharedPreferences
) {
    var userId: Int
        get() = dataStore.getInt("user_id", -1)
        set(value) = dataStore.edit { putInt("user_id", value) }

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
        profileMessage = ""
        nickname = ""
    }
}
