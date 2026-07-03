package org.sopt.official.data.sopletter.onboarding.datasource

import org.sopt.official.data.sopletter.onboarding.dto.response.SopletterOnboardingResponseDto

interface SopletterOnboardingDataSource {
    suspend fun getOnboarding(): SopletterOnboardingResponseDto
    suspend fun completeOnboarding(): SopletterOnboardingResponseDto
}