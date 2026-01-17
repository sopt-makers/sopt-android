/*
 * MIT License
 * Copyright 2024 SOPT - Shout Our Passion Together
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
package org.sopt.official.data.poke.source.remote

import org.sopt.official.data.poke.dto.request.GetFriendListDetailRequest
import org.sopt.official.data.poke.dto.request.GetPokeMessageListRequest
import org.sopt.official.data.poke.dto.request.GetPokeNotificationListRequest
import org.sopt.official.data.poke.dto.request.PokeMessageRequest
import org.sopt.official.data.poke.dto.request.PokeUserRequest
import org.sopt.official.data.poke.service.PokeService
import org.sopt.official.domain.poke.entity.CheckNewInPokeResponse
import org.sopt.official.domain.poke.entity.GetFriendListDetailResponse
import org.sopt.official.domain.poke.entity.GetFriendListSummaryResponse
import org.sopt.official.domain.poke.entity.GetOnboardingPokeUserListResponse
import org.sopt.official.domain.poke.entity.GetPokeFriendOfFriendListResponse
import org.sopt.official.domain.poke.entity.GetPokeFriendResponse
import org.sopt.official.domain.poke.entity.GetPokeMeResponse
import org.sopt.official.domain.poke.entity.GetPokeMessageListResponse
import org.sopt.official.domain.poke.entity.GetPokeNotificationListResponse
import org.sopt.official.domain.poke.entity.PokeUserResponse
import dev.zacsweers.metro.Inject

@Inject
class PokeRemoteDataSource(
    private val service: PokeService,
) {
    suspend fun checkNewInPoke(): CheckNewInPokeResponse {
        val response = service.checkNewInPoke()
        return CheckNewInPokeResponse().apply {
            statusCode = response.code().toString()
            responseMessage = response.message()
            data = response.body()?.toEntity()
        }
    }

    suspend fun getOnboardingPokeUserList(randomType: String?, size: Int): GetOnboardingPokeUserListResponse {
        val response = service.getOnboardingPokeUserList(randomType, size)
        return GetOnboardingPokeUserListResponse().apply {
            statusCode = response.code().toString()
            responseMessage = response.message()
            data = response.body()?.toEntity()
        }
    }

    suspend fun getPokeMe(): GetPokeMeResponse {
        val response = service.getPokeMe()
        return GetPokeMeResponse().apply {
            statusCode = response.code().toString()
            responseMessage = response.message()
            data = response.body()?.toEntity()
        }
    }

    suspend fun getPokeFriend(): GetPokeFriendResponse {
        val response = service.getPokeFriend()
        return GetPokeFriendResponse().apply {
            statusCode = response.code().toString()
            responseMessage = response.message()
            data = response.body()?.map { it.toEntity() }
        }
    }

    suspend fun getPokeFriendOfFriendList(): GetPokeFriendOfFriendListResponse {
        val response = service.getPokeFriendOfFriendList()
        return GetPokeFriendOfFriendListResponse().apply {
            statusCode = response.code().toString()
            responseMessage = response.message()
            data = response.body()?.map { it.toEntity() }
        }
    }

    suspend fun getPokeNotificationList(getPokeNotificationListRequest: GetPokeNotificationListRequest): GetPokeNotificationListResponse {
        val response =
            service.getPokeNotificationList(
                page = getPokeNotificationListRequest.page,
            )
        return GetPokeNotificationListResponse().apply {
            statusCode = response.code().toString()
            responseMessage = response.message()
            data = response.body()?.toEntity()
        }
    }

    suspend fun getFriendListSummary(): GetFriendListSummaryResponse {
        val response = service.getFriendListSummary()
        return GetFriendListSummaryResponse().apply {
            statusCode = response.code().toString()
            responseMessage = response.message()
            data = response.body()?.toEntity()
        }
    }

    suspend fun getFriendListDetail(getFriendListDetailRequest: GetFriendListDetailRequest): GetFriendListDetailResponse {
        val response =
            service.getFriendListDetail(
                type = getFriendListDetailRequest.type.typeName,
                page = getFriendListDetailRequest.page,
            )
        return GetFriendListDetailResponse().apply {
            statusCode = response.code().toString()
            responseMessage = response.message()
            data = response.body()?.toEntity()
        }
    }

    suspend fun getPokeMessageList(getPokeMessageListRequest: GetPokeMessageListRequest): GetPokeMessageListResponse {
        val response =
            service.getPokeMessageList(
                messageType = getPokeMessageListRequest.messageType.typeName,
            )
        return GetPokeMessageListResponse().apply {
            statusCode = response.code().toString()
            responseMessage = response.message()
            data = response.body()?.toEntity()
        }
    }

    suspend fun pokeUser(pokeUserRequest: PokeUserRequest): PokeUserResponse {
        val response =
            service.pokeUser(
                userId = pokeUserRequest.userId,
                pokeMessageRequest = PokeMessageRequest(
                    isAnonymous = pokeUserRequest.isAnonymous,
                    message = pokeUserRequest.message,
                ),
            )
        return PokeUserResponse().apply {
            statusCode = response.code().toString()
            responseMessage = response.message()
            data = response.body()?.toEntity()
        }
    }
}
