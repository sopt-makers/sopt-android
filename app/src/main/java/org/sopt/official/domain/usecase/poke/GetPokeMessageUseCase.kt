package org.sopt.official.domain.usecase.poke

import org.sopt.official.data.model.poke.response.PokeMessageResponse
import org.sopt.official.domain.repository.poke.PokeRepository
import org.sopt.official.feature.poke.enums.MessageType
import javax.inject.Inject

class GetPokeMessageUseCase @Inject constructor(
    private val pokeRepository: PokeRepository
) {
    suspend operator fun invoke(messageType: MessageType): Result<PokeMessageResponse> {
        return pokeRepository.getPokeMessages(messageType.messageType)
    }
}