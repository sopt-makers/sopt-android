package org.sopt.official.data.sopletter.service

import org.sopt.official.data.sopletter.dto.response.SopletterDefaultMessagesResponseDto
import org.sopt.official.data.sopletter.dto.response.SopletterMessageDetailResponseDto
import org.sopt.official.data.sopletter.dto.response.SopletterReportFormResponseDto
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface SopletterService {
    @GET("sopt-letter/topics/default/messages")
    suspend fun getDefaultMessages(
        @Query("cursor") cursor: Long?,
        @Query("size") size: Int,
    ): SopletterDefaultMessagesResponseDto

    @GET("sopt-letter/report-form")
    suspend fun getReportForm(): SopletterReportFormResponseDto

    @GET("sopt-letter/topics/{topicId}/messages/{messageId}")
    suspend fun getMessageDetail(
        @Path("topicId") topicId: Long,
        @Path("messageId") messageId: Long,
    ): SopletterMessageDetailResponseDto
}
