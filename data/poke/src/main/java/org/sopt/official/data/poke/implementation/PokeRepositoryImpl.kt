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
package org.sopt.official.data.poke.implementation

import org.sopt.official.data.poke.dto.request.GetFriendListDetailRequest
import org.sopt.official.data.poke.dto.request.GetPokeMessageListRequest
import org.sopt.official.data.poke.dto.request.GetPokeNotificationListRequest
import org.sopt.official.data.poke.dto.request.PokeUserRequest
import org.sopt.official.data.poke.source.local.PokeLocalDataSource
import org.sopt.official.data.poke.source.remote.PokeRemoteDataSource
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
import org.sopt.official.domain.poke.repository.PokeRepository
import org.sopt.official.domain.poke.type.PokeFriendType
import org.sopt.official.domain.poke.type.PokeMessageType
import dev.zacsweers.metro.Inject

@Inject
class PokeRepositoryImpl(
    private val localDataSource: PokeLocalDataSource,
    private val remoteDataSource: PokeRemoteDataSource,
) : PokeRepository {

    override suspend fun checkNewInPokeOnboarding(): Boolean {
        return localDataSource.isAnonymousInPokeOnboarding
    }

    override suspend fun updateNewInPokeOnboarding() {
        localDataSource.isAnonymousInPokeOnboarding = false
    }

    override suspend fun checkNewInPoke(): CheckNewInPokeResponse {
        return remoteDataSource.checkNewInPoke()
    }

    override suspend fun getOnboardingPokeUserList(randomType: String?, size: Int): GetOnboardingPokeUserListResponse {
        return remoteDataSource.getOnboardingPokeUserList(randomType, size)
    }

    override suspend fun getPokeMe(): GetPokeMeResponse {
        return remoteDataSource.getPokeMe()
    }

    override suspend fun getPokeFriend(): GetPokeFriendResponse {
        return remoteDataSource.getPokeFriend()
    }

    override suspend fun getPokeFriendOfFriendList(): GetPokeFriendOfFriendListResponse {
        return remoteDataSource.getPokeFriendOfFriendList()
    }

    override suspend fun getPokeNotificationList(page: Int): GetPokeNotificationListResponse {
        return remoteDataSource.getPokeNotificationList(
            getPokeNotificationListRequest =
            GetPokeNotificationListRequest(
                page = page,
            ),
        )
    }

    override suspend fun getFriendListSummary(): GetFriendListSummaryResponse {
        return remoteDataSource.getFriendListSummary()
    }

    override suspend fun getFriendListDetail(type: PokeFriendType, page: Int): GetFriendListDetailResponse {
        return remoteDataSource.getFriendListDetail(
            getFriendListDetailRequest =
            GetFriendListDetailRequest(
                type = type,
                page = page,
            ),
        )
    }

    override suspend fun getPokeMessageList(messageType: PokeMessageType): GetPokeMessageListResponse {
        return remoteDataSource.getPokeMessageList(
            getPokeMessageListRequest =
            GetPokeMessageListRequest(
                messageType = messageType,
            ),
        )
    }

    override suspend fun pokeUser(userId: Int, isAnonymous: Boolean, message: String): PokeUserResponse {
        return remoteDataSource.pokeUser(
            pokeUserRequest =
            PokeUserRequest(
                userId = userId,
                isAnonymous = isAnonymous,
                message = message,
            ),
        )
    }
}
