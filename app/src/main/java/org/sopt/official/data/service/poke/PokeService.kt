package org.sopt.official.data.service.poke

import org.sopt.official.data.model.poke.response.PokeFriendOfFriendResponse
import org.sopt.official.data.model.poke.response.PokeFriendResponse
import org.sopt.official.data.model.poke.response.PokeMeResponse
import org.sopt.official.data.model.poke.response.PokeNotificationResponse
import retrofit2.http.GET

interface PokeService {
    @GET("poke/to/me")
    suspend fun getPokeMe() : PokeMeResponse

    @GET("poke/friend")
    suspend fun getPokeFriend() : List<PokeFriendResponse>

    @GET("/poke/friend/random-user")
    suspend fun getPokeFriendOfFriend(): PokeFriendOfFriendResponse

    @GET("poke/to/me/list")
    suspend fun getPokeNotification(): PokeNotificationResponse
}