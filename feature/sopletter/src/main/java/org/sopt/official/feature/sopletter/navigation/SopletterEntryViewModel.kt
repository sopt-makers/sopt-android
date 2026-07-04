package org.sopt.official.feature.sopletter.navigation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import org.sopt.official.localstorage.source.UserStorage
import javax.inject.Inject

@HiltViewModel
class SopletterEntryViewModel @Inject constructor(
    userStorage: UserStorage,
) : ViewModel() {
    val isOnboardingCompleted: StateFlow<Boolean?> = userStorage.isSopletterOnboardingCompleted
        .map<Boolean, Boolean?> { it }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = null,
        )
}
