/*
 * MIT License
 * Copyright 2023-2024 SOPT - Shout Our Passion Together
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
package org.sopt.official.data.poke.service

import org.sopt.official.data.poke.dto.request.PokeMessageRequest
import org.sopt.official.data.poke.dto.response.CheckNewInPokeResult
import org.sopt.official.data.poke.dto.response.GetFriendListDetailResult
import org.sopt.official.data.poke.dto.response.GetFriendListSummaryResult
import org.sopt.official.data.poke.dto.response.GetPokeMessageListResult
import org.sopt.official.data.poke.dto.response.PokeFriendOfFriendListResult
import org.sopt.official.data.poke.dto.response.PokeNotificationResult
import org.sopt.official.data.poke.dto.response.PokeRandomUserListResult
import org.sopt.official.data.poke.dto.response.PokeUserResult
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

interface PokeService {
    @GET("poke/new")
    suspend fun checkNewInPoke(): Response<CheckNewInPokeResult>

    @GET("poke/random")
    suspend fun getOnboardingPokeUserList(
        @Query("randomType") randomType: String?,
        @Query("size") size: Int,
    ): Response<PokeRandomUserListResult>

    @GET("poke/to/me")
    suspend fun getPokeMe(): Response<PokeUserResult>

    @GET("poke/friend")
    suspend fun getPokeFriend(): Response<List<PokeUserResult>>

    @GET("poke/friend/random-user")
    suspend fun getPokeFriendOfFriendList(): Response<List<PokeFriendOfFriendListResult>>

    @GET("poke/to/me/list")
    suspend fun getPokeNotificationList(@Query("page") page: Int): Response<PokeNotificationResult>

    @GET("poke/friend/list")
    suspend fun getFriendListSummary(): Response<GetFriendListSummaryResult>

    @GET("poke/friend/list")
    suspend fun getFriendListDetail(@Query("type") type: String, @Query("page") page: Int): Response<GetFriendListDetailResult>

    @GET("poke/message")
    suspend fun getPokeMessageList(@Query("messageType") messageType: String): Response<GetPokeMessageListResult>

    @PUT("poke/{userId}")
    suspend fun pokeUser(@Path("userId") userId: Int, @Body pokeMessageRequest: PokeMessageRequest): Response<PokeUserResult>
}
