package org.sopt.official.data.poke.service

import org.sopt.official.data.poke.dto.response.CheckNewInPokeResult
import org.sopt.official.data.poke.dto.response.GetPokeMessageListResult
import org.sopt.official.data.poke.dto.response.GetFriendListDetailResult
import org.sopt.official.data.poke.dto.response.GetFriendListSummaryResult
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

    @GET("poke/random-user")
    suspend fun getOnboardingPokeUserList(): Response<List<PokeUserResult>>

    @GET("poke/friend/list")
    suspend fun getFriendListSummary(): Response<GetFriendListSummaryResult>

    @GET("poke/friend/list")
    suspend fun getFriendListDetail(
        @Query("type") type: String,
        @Query("page") page: Int,
    ): Response<GetFriendListDetailResult>

    @GET("poke/message")
    suspend fun getPokeMessageList(
        @Query("messageType") messageType: String,
    ): Response<GetPokeMessageListResult>

    @PUT("poke/{userId}")
    suspend fun pokeUser(
        @Path("userId") userId: Int,
        @Body message: String,
    ): Response<PokeUserResult>
}