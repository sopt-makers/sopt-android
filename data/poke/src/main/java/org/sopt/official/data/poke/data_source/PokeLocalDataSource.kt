package org.sopt.official.data.poke.data_source

import android.content.SharedPreferences
import androidx.core.content.edit
import org.sopt.official.data.poke.di.Poke
import javax.inject.Inject

class PokeLocalDataSource @Inject constructor(
    @Poke private val dataStore: SharedPreferences,
) {
    var isNewInPokeOnboarding: Boolean
        get() = dataStore.getBoolean(IS_NEW_IN_POKE_ONBOARDING, true)
        set(value) = dataStore.edit { putBoolean(IS_NEW_IN_POKE_ONBOARDING, value) }

    fun clear() {
        isNewInPokeOnboarding = true
    }

    companion object {
        private const val IS_NEW_IN_POKE_ONBOARDING = "is_new_in_poke_onboarding"
    }
}