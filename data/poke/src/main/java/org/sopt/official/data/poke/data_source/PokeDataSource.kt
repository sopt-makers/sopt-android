package org.sopt.official.data.poke.data_source

import org.sopt.official.data.poke.service.PokeService
import org.sopt.official.data.poke.dto.request.GetFriendListDetailRequest
import org.sopt.official.domain.poke.entity.request.GetPokeMessageListRequest
import org.sopt.official.domain.poke.entity.request.PokeUserRequest
import org.sopt.official.domain.poke.entity.CheckNewInPokeResponse
import org.sopt.official.domain.poke.entity.GetPokeMessageListResponse
import org.sopt.official.domain.poke.entity.GetFriendListDetailResponse
import org.sopt.official.domain.poke.entity.GetFriendListSummaryResponse
import org.sopt.official.domain.poke.entity.GetOnboardingPokeUserListResponse
import org.sopt.official.domain.poke.entity.PokeUserResponse
import javax.inject.Inject

class PokeDataSource @Inject constructor(
    private val service: PokeService,
) {
    suspend fun checkNewInPoke(): CheckNewInPokeResponse {
        val call = service.checkNewInPoke()
        return CheckNewInPokeResponse().apply {
            statusCode = call.code().toString()
            responseMessage = call.message()
            data = call.body()?.toEntity()
        }
    }

    suspend fun getOnboardingPokeUserList(): GetOnboardingPokeUserListResponse {
        val call = service.getOnboardingPokeUserList()
        return GetOnboardingPokeUserListResponse().apply {
            statusCode = call.code().toString()
            responseMessage = call.message()
            data = call.body()?.map { it.toEntity() }
        }
    }

    suspend fun getFriendListSummary(): GetFriendListSummaryResponse {
        val call = service.getFriendListSummary()
        return GetFriendListSummaryResponse().apply {
            statusCode = call.code().toString()
            responseMessage = call.message()
            data = call.body()?.toEntity()
        }
    }

    suspend fun getFriendListDetail(
        getFriendListDetailRequest: GetFriendListDetailRequest,
    ): GetFriendListDetailResponse {
        val call = service.getFriendListDetail(
            type = getFriendListDetailRequest.type.typeName,
            page = getFriendListDetailRequest.page,
        )
        return GetFriendListDetailResponse().apply {
            statusCode = call.code().toString()
            responseMessage = call.message()
            data = call.body()?.toEntity()
        }
    }

    suspend fun getPokeMessageList(
        getPokeMessageListRequest: GetPokeMessageListRequest,
    ): GetPokeMessageListResponse {
        val call = service.getPokeMessageList(
            messageType = getPokeMessageListRequest.messageType.typeName,
        )
        return GetPokeMessageListResponse().apply {
            statusCode = call.code().toString()
            responseMessage = call.message()
            data = call.body()?.toEntity()
        }
    }

    suspend fun pokeUser(
        pokeUserRequest: PokeUserRequest,
    ): PokeUserResponse {
        val call = service.pokeUser(
            userId = pokeUserRequest.userId,
            message = pokeUserRequest.message,
        )
        return PokeUserResponse().apply {
            statusCode = call.code().toString()
            responseMessage = call.message()
            data = call.body()?.toEntity()
        }
    }
}