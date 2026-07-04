package org.sopt.official.data.sopletter.onboarding.service

import org.sopt.official.data.sopletter.onboarding.dto.response.SopletterOnboardingResponseDto
import retrofit2.http.GET
import retrofit2.http.POST

interface SopletterOnboardingService {
    @GET("sopt-letter/onboarding")
    suspend fun getOnboarding(): SopletterOnboardingResponseDto

    @POST("sopt-letter/onboarding/complete")
    suspend fun completeOnboarding(): SopletterOnboardingResponseDto
}