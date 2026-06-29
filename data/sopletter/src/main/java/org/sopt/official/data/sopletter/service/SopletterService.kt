package org.sopt.official.data.sopletter.service

import org.sopt.official.data.sopletter.dto.response.SopletterDefaultMessagesResponseDto
import org.sopt.official.data.sopletter.dto.response.SopletterReportFormResponseDto
import retrofit2.http.GET
import retrofit2.http.Query

interface SopletterService {
    @GET("sopt-letter/topics/default/messages")
    suspend fun getDefaultMessages(
        @Query("cursor") cursor: Long?,
        @Query("size") size: Int,
    ): SopletterDefaultMessagesResponseDto

    @GET("sopt-letter/report-form")
    suspend fun getReportForm(): SopletterReportFormResponseDto
}
