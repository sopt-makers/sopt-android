package org.sopt.official.feature.sopletter.navigation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import org.sopt.official.domain.sopletter.onboarding.repository.SopletterOnboardingRepository
import org.sopt.official.localstorage.source.UserStorage
import javax.inject.Inject

@HiltViewModel
class SopletterEntryViewModel @Inject constructor(
    private val userStorage: UserStorage,
    private val sopletterOnboardingRepository: SopletterOnboardingRepository,
) : ViewModel() {
    private val _isOnboardingCompleted = MutableStateFlow<Boolean?>(null)
    val isOnboardingCompleted: StateFlow<Boolean?> = _isOnboardingCompleted.asStateFlow()

    init {
        observeOnboardingState()
        initializeOnboardingState()
    }

    private fun observeOnboardingState() = viewModelScope.launch {
        userStorage.isSopletterOnboardingCompleted.collectLatest { isCompleted ->
            if (isCompleted != null) {
                _isOnboardingCompleted.value = isCompleted
            }
        }
    }

    private fun initializeOnboardingState() = viewModelScope.launch {
        val localState = userStorage.isSopletterOnboardingCompleted.first()
        if (localState != null) {
            _isOnboardingCompleted.value = localState
            return@launch
        }

        sopletterOnboardingRepository.getOnboarding()
            .onSuccess { onboarding ->
                userStorage.saveOnboardingCompleted(onboarding.isOnboarded)
            }
            .onFailure {
                _isOnboardingCompleted.value = false
            }
    }
}
