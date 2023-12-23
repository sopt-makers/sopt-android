package org.sopt.official.domain.usecase.poke

import org.sopt.official.data.model.poke.response.PokeFriendOfFriendResponse
import org.sopt.official.domain.repository.poke.PokeRepository
import javax.inject.Inject

class GetPokeFriendOfFriendUseCase @Inject constructor(
    private val pokeRepository: PokeRepository
) {
    suspend operator fun invoke(): Result<PokeFriendOfFriendResponse> {
        return pokeRepository.getPokeFriendOfFriend()
    }
}