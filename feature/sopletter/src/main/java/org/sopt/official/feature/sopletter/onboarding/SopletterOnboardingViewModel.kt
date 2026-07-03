package org.sopt.official.feature.sopletter.onboarding

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.sopt.official.domain.sopletter.onboarding.repository.SopletterOnboardingRepository
import org.sopt.official.feature.sopletter.onboarding.model.toUiModel
import org.sopt.official.localstorage.source.UserStorage
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class SopletterOnboardingViewModel @Inject constructor(
    private val userStorage: UserStorage,
    private val sopletterOnboardingRepository: SopletterOnboardingRepository
) : ViewModel() {
    private val _state = MutableStateFlow(SopletterOnboardingState())
    val state = _state.asStateFlow()

    private val _sideEffect = Channel<SopletterOnboardingSideEffect>()
    val sideEffect = _sideEffect.receiveAsFlow()

    init {
        fetchSopletterOnboarding()
    }

    fun fetchSopletterOnboarding(){
        viewModelScope.launch {
            sopletterOnboardingRepository.getOnboarding()
                .onSuccess {
                    userStorage.saveOnboardingCompleted(it.isOnboarded)
                    _state.update { currentState ->
                        currentState.copy(
                            onboardingUiModel = it.toUiModel()
                        )
                    }
                }
                .onFailure {
                    Timber.e(it)
                    _sideEffect.send(SopletterOnboardingSideEffect.ShowSnackbar("오류가 발생했어요. 다시 시도해주세요."))
                }
        }
    }

    fun updateSopletterOnboardingStatus() {
        viewModelScope.launch {
            sopletterOnboardingRepository.completeOnboarding()
                .onSuccess {
                    userStorage.saveOnboardingCompleted(it.isOnboarded)
                    _sideEffect.send(SopletterOnboardingSideEffect.NavigateToNickname(it.nickname, 38)) // Todo generation 추가
                }
                .onFailure {
                    Timber.e(it)
                    _sideEffect.send(SopletterOnboardingSideEffect.ShowSnackbar("오류가 발생했어요. 다시 시도해주세요."))
                }
        }
    }
}