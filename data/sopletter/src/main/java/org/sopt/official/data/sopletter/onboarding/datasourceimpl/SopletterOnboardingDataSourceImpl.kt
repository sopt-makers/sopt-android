package org.sopt.official.data.sopletter.onboarding.datasourceimpl

import org.sopt.official.data.sopletter.onboarding.datasource.SopletterOnboardingDataSource
import org.sopt.official.data.sopletter.onboarding.dto.response.SopletterOnboardingResponseDto
import org.sopt.official.data.sopletter.onboarding.service.SopletterOnboardingService
import javax.inject.Inject

class SopletterOnboardingDataSourceImpl @Inject constructor(
    private val sopletterOnboardingService: SopletterOnboardingService
): SopletterOnboardingDataSource {
    override suspend fun getOnboarding(): SopletterOnboardingResponseDto =
        sopletterOnboardingService.getOnboarding()

    override suspend fun completeOnboarding(): SopletterOnboardingResponseDto =
        sopletterOnboardingService.completeOnboarding()
}