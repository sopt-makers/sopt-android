package org.sopt.official.data.poke.repository_impl

import org.sopt.official.data.poke.data_source.PokeDataSource
import org.sopt.official.data.poke.dto.request.GetFriendListDetailRequest
import org.sopt.official.domain.poke.entity.CheckNewInPokeResponse
import org.sopt.official.domain.poke.entity.GetFriendListDetailResponse
import org.sopt.official.domain.poke.entity.GetFriendListSummaryResponse
import org.sopt.official.domain.poke.entity.GetOnboardingPokeUserListResponse
import org.sopt.official.domain.poke.entity.GetPokeFriendOfFriendListResponse
import org.sopt.official.domain.poke.entity.GetPokeFriendListResponse
import org.sopt.official.domain.poke.entity.GetPokeMeResponse
import org.sopt.official.domain.poke.entity.GetPokeMessageListResponse
import org.sopt.official.domain.poke.entity.GetPokeNotificationListResponse
import org.sopt.official.domain.poke.entity.PokeUserResponse
import org.sopt.official.domain.poke.entity.request.GetPokeMessageListRequest
import org.sopt.official.domain.poke.entity.request.PokeUserRequest
import org.sopt.official.domain.poke.repository.PokeRepository
import org.sopt.official.domain.poke.type.PokeFriendType
import org.sopt.official.domain.poke.type.PokeMessageType
import javax.inject.Inject

class PokeRepositoryImpl @Inject constructor(
    private val dataSource: PokeDataSource,
) : PokeRepository {
    override suspend fun checkNewInPoke(): CheckNewInPokeResponse {
        return dataSource.checkNewInPoke()
    }

    override suspend fun getOnboardingPokeUserList(): GetOnboardingPokeUserListResponse {
        return dataSource.getOnboardingPokeUserList()
    }

    override suspend fun getPokeMe(): GetPokeMeResponse {
        return dataSource.getPokeMe()
    }

    override suspend fun getPokeFriendList(): GetPokeFriendListResponse {
        return dataSource.getPokeFriendList()
    }

    override suspend fun getPokeFriendOfFriendList(): GetPokeFriendOfFriendListResponse {
        return dataSource.getPokeFriendOfFriendList()
    }

    override suspend fun getPokeNotificationList(
        page: Int,
    ): GetPokeNotificationListResponse {
        return dataSource.getPokeNotificationList(
            page = page,
        )
    }

    override suspend fun getFriendListSummary(): GetFriendListSummaryResponse {
        return dataSource.getFriendListSummary()
    }

    override suspend fun getFriendListDetail(
        type: PokeFriendType,
        page: Int,
    ): GetFriendListDetailResponse {
        return dataSource.getFriendListDetail(
            getFriendListDetailRequest = GetFriendListDetailRequest(
                type = type,
                page = page,
            )
        )
    }

    override suspend fun getPokeMessageList(
        messageType: PokeMessageType,
    ): GetPokeMessageListResponse {
        return dataSource.getPokeMessageList(
            getPokeMessageListRequest = GetPokeMessageListRequest(
                messageType = messageType,
            )
        )
    }

    override suspend fun pokeUser(
        userId: Int,
        message: String,
    ): PokeUserResponse {
        return dataSource.pokeUser(
            pokeUserRequest = PokeUserRequest(
                userId = userId,
                message = message,
            )
        )
    }
}