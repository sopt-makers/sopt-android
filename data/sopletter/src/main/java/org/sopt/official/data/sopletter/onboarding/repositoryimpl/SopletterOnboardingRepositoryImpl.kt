package org.sopt.official.data.sopletter.onboarding.repositoryimpl

import org.sopt.official.common.coroutines.suspendRunCatching
import org.sopt.official.data.sopletter.onboarding.service.SopletterOnboardingService
import org.sopt.official.domain.sopletter.onboarding.model.SopletterOnboardingModel
import org.sopt.official.domain.sopletter.onboarding.repository.SopletterOnboardingRepository
import javax.inject.Inject

internal class SopletterOnboardingRepositoryImpl @Inject constructor(
    private val sopletterOnboardingService: SopletterOnboardingService,
) : SopletterOnboardingRepository {

    override suspend fun getOnboarding(): Result<SopletterOnboardingModel> = suspendRunCatching {
        sopletterOnboardingService.getOnboarding().toDomain()
    }

    override suspend fun completeOnboarding(): Result<SopletterOnboardingModel> = suspendRunCatching {
        sopletterOnboardingService.completeOnboarding().toDomain()
    }
}