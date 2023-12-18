package org.sopt.official.domain.usecase.poke

import org.sopt.official.data.model.poke.response.PokeNotificationResponse
import org.sopt.official.domain.repository.poke.PokeRepository
import javax.inject.Inject

class GetPokeNotificationUseCase @Inject constructor(
    private val pokeRepository: PokeRepository
) {
    suspend operator fun invoke(): Result<PokeNotificationResponse> {
        return pokeRepository.getPokeNotification()
    }
}