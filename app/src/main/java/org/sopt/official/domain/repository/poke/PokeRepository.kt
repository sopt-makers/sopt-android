package org.sopt.official.domain.repository.poke

import org.sopt.official.data.model.poke.response.PokeFriendResponse
import org.sopt.official.data.model.poke.response.PokeMeResponse

interface PokeRepository {
    suspend fun getPokeMe(): Result<PokeMeResponse>
    suspend fun getPokeFriend(): Result<PokeFriendResponse>
}