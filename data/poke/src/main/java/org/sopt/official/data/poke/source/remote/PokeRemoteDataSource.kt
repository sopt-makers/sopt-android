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
import org.sopt.official.domain.poke.entity.CheckNewInPoke
import org.sopt.official.domain.poke.entity.FriendListDetail
import org.sopt.official.domain.poke.entity.FriendListSummary
import org.sopt.official.domain.poke.entity.PokeFriendOfFriendList
import org.sopt.official.domain.poke.entity.PokeMessageList
import org.sopt.official.domain.poke.entity.PokeNotificationList
import org.sopt.official.domain.poke.entity.PokeRandomUserList
import org.sopt.official.domain.poke.entity.PokeUser
import javax.inject.Inject

class PokeRemoteDataSource @Inject constructor(
    private val service: PokeService,
) {
    suspend fun checkNewInPoke(): CheckNewInPoke =
        service.checkNewInPoke().data.toEntity()

    suspend fun getOnboardingPokeUserList(randomType: String?, size: Int): PokeRandomUserList =
        service.getOnboardingPokeUserList(randomType, size).data.toEntity()

    suspend fun getPokeMe(): PokeUser =
        service.getPokeMe().data.toEntity()

    suspend fun getPokeFriend(): List<PokeUser> =
        service.getPokeFriend().data.map { it.toEntity() }

    suspend fun getPokeFriendOfFriendList(): List<PokeFriendOfFriendList> =
        service.getPokeFriendOfFriendList().data.map { it.toEntity() }

    suspend fun getPokeNotificationList(getPokeNotificationListRequest: GetPokeNotificationListRequest): PokeNotificationList =
        service.getPokeNotificationList(page = getPokeNotificationListRequest.page).data.toEntity()

    suspend fun getFriendListSummary(): FriendListSummary =
        service.getFriendListSummary().data.toEntity()

    suspend fun getFriendListDetail(getFriendListDetailRequest: GetFriendListDetailRequest): FriendListDetail =
        service.getFriendListDetail(
            type = getFriendListDetailRequest.type.typeName,
            page = getFriendListDetailRequest.page,
        ).data.toEntity()

    suspend fun getPokeMessageList(getPokeMessageListRequest: GetPokeMessageListRequest): PokeMessageList =
        service.getPokeMessageList(messageType = getPokeMessageListRequest.messageType.typeName).data.toEntity()

    suspend fun pokeUser(pokeUserRequest: PokeUserRequest): PokeUser =
        service.pokeUser(
            userId = pokeUserRequest.userId,
            pokeMessageRequest = PokeMessageRequest(
                isAnonymous = pokeUserRequest.isAnonymous,
                message = pokeUserRequest.message,
            ),
        ).data.toEntity()
}
