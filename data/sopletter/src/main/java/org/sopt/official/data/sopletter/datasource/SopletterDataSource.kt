package org.sopt.official.data.sopletter.datasource

import org.sopt.official.data.sopletter.dto.response.SopletterDefaultMessagesResponseDto
import org.sopt.official.data.sopletter.dto.response.SopletterReportFormResponseDto

interface SopletterDataSource {
    suspend fun getDefaultMessages(
        cursor: Long?,
        size: Int,
    ): SopletterDefaultMessagesResponseDto

    suspend fun getReportForm(): SopletterReportFormResponseDto
}
