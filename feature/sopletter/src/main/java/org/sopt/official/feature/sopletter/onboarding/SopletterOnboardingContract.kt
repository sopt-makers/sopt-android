package org.sopt.official.feature.sopletter.onboarding

import androidx.compose.runtime.Immutable
import org.sopt.official.feature.sopletter.onboarding.model.SopletterOnboardingUiModel

@Immutable
data class SopletterOnboardingState(
    val onboardingUiModel: SopletterOnboardingUiModel = SopletterOnboardingUiModel(),
    val isLoading: Boolean = false
)

sealed interface SopletterOnboardingSideEffect {
    data class ShowSnackbar(val message: String) : SopletterOnboardingSideEffect
    data class NavigateToNickname(val nickname: String, val currentGeneration: Int) : SopletterOnboardingSideEffect
}

