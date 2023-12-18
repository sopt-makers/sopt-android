package org.sopt.official.domain.usecase.poke

import org.sopt.official.data.model.poke.response.PokeFriendResponse
import org.sopt.official.domain.repository.poke.PokeRepository
import javax.inject.Inject

class GetPokeFriendUseCase @Inject constructor(
    private val pokeRepository: PokeRepository
) {
    suspend operator fun invoke(): Result<PokeFriendResponse> {
        return pokeRepository.getPokeFriend()
    }
}