package org.sopt.official.domain.poke.repository

import org.sopt.official.domain.poke.entity.CheckNewInPokeResponse
import org.sopt.official.domain.poke.entity.GetFriendListDetailResponse
import org.sopt.official.domain.poke.entity.GetFriendListSummaryResponse
import org.sopt.official.domain.poke.entity.GetOnboardingPokeUserListResponse
import org.sopt.official.domain.poke.entity.GetPokeMessageListResponse
import org.sopt.official.domain.poke.entity.PokeUserResponse
import org.sopt.official.domain.poke.type.PokeFriendType
import org.sopt.official.domain.poke.type.PokeMessageType

interface PokeRepository {
    suspend fun checkNewInPoke(): CheckNewInPokeResponse
    suspend fun getOnboardingPokeUserList(): GetOnboardingPokeUserListResponse
    suspend fun getFriendListSummary(): GetFriendListSummaryResponse

    suspend fun getFriendListDetail(
        type: PokeFriendType,
        page: Int,
    ): GetFriendListDetailResponse

    suspend fun getPokeMessageList(
        messageType: PokeMessageType,
    ): GetPokeMessageListResponse

    suspend fun pokeUser(
        userId: Int,
        message: String,
    ): PokeUserResponse
}