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

import android.app.Application
import android.content.SharedPreferences
import androidx.core.content.edit
import dev.zacsweers.metro.Inject
import org.sopt.official.common.file.createSharedPreference
import org.sopt.official.data.poke.di.PokeStoreKey

@Inject
class PokeLocalDataSource(
    private val application: Application,
    private val pokeStoreKey: PokeStoreKey,
) {
    private val dataStore = createSharedPreference(pokeStoreKey.value, application.applicationContext)

    var isAnonymousInPokeOnboarding: Boolean
        get() = dataStore.getBoolean(IS_ANONYMOUS_IN_POKE_ONBOARDING, true)
        set(value) = dataStore.edit { putBoolean(IS_ANONYMOUS_IN_POKE_ONBOARDING, value) }

    fun clear() {
        isAnonymousInPokeOnboarding = true
    }

    companion object {
        private const val IS_ANONYMOUS_IN_POKE_ONBOARDING = "is_anonymous_in_poke_onboarding"
    }
}
