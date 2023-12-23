package org.sopt.official.domain.repository.poke

import org.sopt.official.data.model.poke.response.PokeFriendOfFriendResponse
import org.sopt.official.data.model.poke.response.PokeFriendResponse
import org.sopt.official.data.model.poke.response.PokeMeResponse
import org.sopt.official.data.model.poke.response.PokeNotificationResponse

interface PokeRepository {
    suspend fun getPokeMe(): Result<PokeMeResponse?>
    suspend fun getPokeFriend(): Result<PokeFriendResponse>
    suspend fun getPokeFriendOfFriend(): Result<PokeFriendOfFriendResponse>
    suspend fun getPokeNotification(): Result<PokeNotificationResponse>
}