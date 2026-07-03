package org.sopt.official.domain.sopletter.onboarding.repository

import org.sopt.official.domain.sopletter.onboarding.model.SopletterOnboardingModel

interface SopletterOnboardingRepository {
    suspend fun getOnboarding(): Result<SopletterOnboardingModel>
    suspend fun completeOnboarding(): Result<SopletterOnboardingModel>
}