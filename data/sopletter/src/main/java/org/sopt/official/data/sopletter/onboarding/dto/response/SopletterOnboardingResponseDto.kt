package org.sopt.official.data.sopletter.onboarding.dto.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.sopt.official.domain.sopletter.onboarding.model.SopletterOnboardingModel

@Serializable
data class SopletterOnboardingResponseDto(
    @SerialName("nickname")
    val nickname: String,
    @SerialName("isOnboarded")
    val isOnboarded: Boolean, // 온보딩 진행 여부
    @SerialName("currentGeneration")
    val currentGeneration: Int
) {
    fun toDomain() = SopletterOnboardingModel(
        nickname = nickname,
        isOnboarded = isOnboarded,
        currentGeneration = currentGeneration
    )
}
