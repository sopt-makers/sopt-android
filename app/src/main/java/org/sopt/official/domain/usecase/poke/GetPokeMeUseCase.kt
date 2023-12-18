package org.sopt.official.domain.usecase.poke

import org.sopt.official.data.model.poke.response.PokeMeResponse
import org.sopt.official.domain.repository.poke.PokeRepository
import javax.inject.Inject

class GetPokeMeUseCase @Inject constructor(
    private val pokeRepository: PokeRepository
) {
    suspend operator fun invoke(): Result<PokeMeResponse?> {
        return pokeRepository.getPokeMe()
    }
}