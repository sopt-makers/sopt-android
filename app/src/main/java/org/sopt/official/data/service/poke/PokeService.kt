package org.sopt.official.data.service.poke

import org.sopt.official.data.model.poke.response.PokeFriendResponse
import org.sopt.official.data.model.poke.response.PokeMeResponse
import retrofit2.http.GET

interface PokeService {
    @GET("poke/to/me")
    suspend fun getPokeMe() : PokeMeResponse

    @GET("poke/friend")
    suspend fun getPokeFriend() : PokeFriendResponse
}