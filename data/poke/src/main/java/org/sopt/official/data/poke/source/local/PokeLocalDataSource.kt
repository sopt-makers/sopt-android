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
package org.sopt.official.data.poke.source.local

import android.content.SharedPreferences
import androidx.core.content.edit
import javax.inject.Inject
import org.sopt.official.data.poke.di.Poke

class PokeLocalDataSource @Inject constructor(
    @Poke private val dataStore: SharedPreferences,
) {
    var isNewInPokeOnboarding: Boolean
        get() = dataStore.getBoolean(IS_NEW_IN_POKE_ONBOARDING, true)
        set(value) = dataStore.edit { putBoolean(IS_NEW_IN_POKE_ONBOARDING, value) }

    var isNewInPokeAnonymousOnboarding: Boolean
        get() = dataStore.getBoolean(IS_NEW_IN_POKE_ANONYMOUS_ONBOARDING, true)
        set(value) = dataStore.edit { putBoolean(IS_NEW_IN_POKE_ANONYMOUS_ONBOARDING, value) }


    fun clear() {
        isNewInPokeOnboarding = true
        isNewInPokeAnonymousOnboarding = true
    }

    companion object {
        private const val IS_NEW_IN_POKE_ONBOARDING = "is_new_in_poke_onboarding"
        private const val IS_NEW_IN_POKE_ANONYMOUS_ONBOARDING = "is_new_in_poke_anonymous_onboarding"
    }
}
