package org.sopt.official.data.sopletter.topic.service

import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface SopletterTopicService {
    @GET("sopt-letter/topics")
    suspend fun getTopics(
        @Query("type") type: String
    ): List<Unit>

    @GET("sopt-letter/topics/{topicId}/messages")
    suspend fun getMessages(
        @Path("topicId") topicId: Int,
        @Query("cursor") cursor: Long?,
        @Query("size") size: Int
    ): List<Unit>
}