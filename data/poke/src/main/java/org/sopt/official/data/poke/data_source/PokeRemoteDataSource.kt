/*
 * MIT License
 * Copyright 2023 SOPT - Shout Our Passion Together
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package org.sopt.official.data.poke.data_source

import org.sopt.official.data.poke.service.PokeService
import org.sopt.official.data.poke.dto.request.GetFriendListDetailRequest
import org.sopt.official.data.poke.dto.request.GetPokeMessageListRequest
import org.sopt.official.domain.poke.entity.request.PokeUserRequest
import org.sopt.official.domain.poke.entity.CheckNewInPokeResponse
import org.sopt.official.domain.poke.entity.GetPokeMessageListResponse
import org.sopt.official.domain.poke.entity.GetFriendListDetailResponse
import org.sopt.official.domain.poke.entity.GetFriendListSummaryResponse
import org.sopt.official.domain.poke.entity.GetOnboardingPokeUserListResponse
import org.sopt.official.domain.poke.entity.GetPokeFriendOfFriendListResponse
import org.sopt.official.domain.poke.entity.GetPokeFriendResponse
import org.sopt.official.domain.poke.entity.GetPokeMeResponse
import org.sopt.official.domain.poke.entity.GetPokeNotificationListResponse
import org.sopt.official.domain.poke.entity.PokeUserResponse
import javax.inject.Inject

class PokeRemoteDataSource @Inject constructor(
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

    suspend fun getPokeMe(): GetPokeMeResponse {
        val call = service.getPokeMe()
        return GetPokeMeResponse().apply {
            statusCode = call.code().toString()
            responseMessage = call.message()
            data = call.body()?.toEntity()
        }
    }

    suspend fun getPokeFriend(): GetPokeFriendResponse {
        val call = service.getPokeFriend()
        return GetPokeFriendResponse().apply {
            statusCode = call.code().toString()
            responseMessage = call.message()
            data = call.body()?.map { it.toEntity() }
        }
    }

    suspend fun getPokeFriendOfFriendList(): GetPokeFriendOfFriendListResponse {
        val call = service.getPokeFriendOfFriendList()
        return GetPokeFriendOfFriendListResponse().apply {
            statusCode = call.code().toString()
            responseMessage = call.message()
            data = call.body()?.map { it.toEntity() }
        }
    }

    suspend fun getPokeNotificationList(
        page: Int,
    ): GetPokeNotificationListResponse {
        val call = service.getPokeNotificationList(
            page = page,
            )
        return GetPokeNotificationListResponse().apply {
            statusCode = call.code().toString()
            responseMessage = call.message()
            data = call.body()?.toEntity()
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
