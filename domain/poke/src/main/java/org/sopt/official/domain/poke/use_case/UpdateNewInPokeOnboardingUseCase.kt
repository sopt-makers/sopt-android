package org.sopt.official.domain.poke.use_case

import org.sopt.official.domain.poke.repository.PokeRepository
import javax.inject.Inject

class UpdateNewInPokeOnboardingUseCase @Inject constructor(
    private val repository: PokeRepository,
) {
    suspend operator fun invoke() {
        repository.updateNewInPokeOnboarding()
    }
}