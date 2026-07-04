package org.sopt.official.domain.sopletter.onboarding.model

data class SopletterOnboardingModel(
    val nickname: String,
    val isOnboarded: Boolean,
    val currentGeneration: Int
)