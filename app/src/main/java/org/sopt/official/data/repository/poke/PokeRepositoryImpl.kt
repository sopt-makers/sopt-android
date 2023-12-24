package org.sopt.official.data.repository.poke

import org.sopt.official.data.mapper.PokeNotificationItemMapper
import org.sopt.official.data.model.poke.response.PokeFriendOfFriendResponse
import org.sopt.official.data.model.poke.response.PokeFriendResponse
import org.sopt.official.data.model.poke.response.PokeMeResponse
import org.sopt.official.data.model.poke.response.PokeMessageResponse
import org.sopt.official.data.model.poke.response.PokeNotificationResponse
import org.sopt.official.data.service.poke.PokeService
import org.sopt.official.domain.entity.poke.PokeNotificationItem
import org.sopt.official.domain.repository.poke.PokeRepository
import javax.inject.Inject

class PokeRepositoryImpl @Inject constructor(
    private val service: PokeService
) : PokeRepository {

    private val pokeNotificationMapper  = PokeNotificationItemMapper()

    override suspend fun getPokeMe(): Result<PokeMeResponse> {
        return runCatching {
            service.getPokeMe()
        }
    }

    override suspend fun getPokeFriend(): Result<PokeFriendResponse> {
        return runCatching {
            service.getPokeFriend().first()
        }
    }

    override suspend fun getPokeFriendOfFriend(): Result<List<PokeFriendOfFriendResponse>> {
        return runCatching {
            service.getPokeFriendOfFriend()
        }
    }

    override suspend fun getPokeNotification(page: Int): Result<List<PokeNotificationItem>> {
        return runCatching {
            service.getPokeNotification(page).history.map(pokeNotificationMapper::toPokeNotificationItem)
        }
    }

    override suspend fun getPokeMessages(messageType: String): Result<PokeMessageResponse> {
        return runCatching {
            service.getPokeMessages(messageType)
        }
    }
}