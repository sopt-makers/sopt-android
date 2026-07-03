package org.sopt.official.data.sopletter.api

import org.sopt.official.data.sopletter.dto.SopletterWriteRequest
import org.sopt.official.data.sopletter.dto.SopletterWriteResponse
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Path

interface SopletterWriteService {
    @POST("sopt-letter/topics/{topicId}/messages")
    suspend fun postTopicSopletter(
        @Path("topicId") topicId: Long,
        @Body request: SopletterWriteRequest
    ): SopletterWriteResponse
}