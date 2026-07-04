package org.sopt.official.feature.sopletter.onboarding.model

import androidx.compose.runtime.Immutable
import org.sopt.official.domain.sopletter.onboarding.model.SopletterOnboardingModel

@Immutable
data class SopletterOnboardingUiModel(
    val nickname: String = "",
    val isOnboarded: Boolean = false,
    val currentGeneration: Int = 38
)

internal fun SopletterOnboardingModel.toUiModel() = SopletterOnboardingUiModel(
    nickname = nickname,
    isOnboarded = isOnboarded,
    currentGeneration = currentGeneration
)