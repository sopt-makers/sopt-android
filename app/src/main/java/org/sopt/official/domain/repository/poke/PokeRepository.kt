package org.sopt.official.domain.repository.poke

import org.sopt.official.data.model.poke.response.PokeFriendOfFriendResponse
import org.sopt.official.data.model.poke.response.PokeFriendResponse
import org.sopt.official.data.model.poke.response.PokeMeResponse
import org.sopt.official.data.model.poke.response.PokeMessageResponse
import org.sopt.official.data.model.poke.response.PokeNotificationResponse
import org.sopt.official.domain.entity.poke.PokeNotificationItem

interface PokeRepository {
    suspend fun getPokeMe(): Result<PokeMeResponse?>
    suspend fun getPokeFriend(): Result<PokeFriendResponse>
    suspend fun getPokeFriendOfFriend(): Result<List<PokeFriendOfFriendResponse>>
    suspend fun getPokeNotification(page: Int): Result<List<PokeNotificationItem>>
    suspend fun getPokeMessages(messageType: String): Result<PokeMessageResponse>
}