package org.sopt.official.data.repository.poke

import org.sopt.official.data.model.poke.response.PokeFriendResponse
import org.sopt.official.data.model.poke.response.PokeMeResponse
import org.sopt.official.data.model.poke.response.PokeNotificationResponse
import org.sopt.official.data.service.poke.PokeService
import org.sopt.official.domain.repository.poke.PokeRepository
import javax.inject.Inject

class PokeRepositoryImpl @Inject constructor(
    private val service: PokeService
) : PokeRepository {
    override suspend fun getPokeMe(): Result<PokeMeResponse> {
        return runCatching {
            service.getPokeMe()
        }
    }

    override suspend fun getPokeFriend(): Result<PokeFriendResponse> {
        return runCatching {
            service.getPokeFriend()
        }
    }

    override suspend fun getPokeNotification(): Result<PokeNotificationResponse> {
        return runCatching {
            service.getPokeNotification()
        }
    }
}