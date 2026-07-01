package org.sopt.official.data.sopletter.datasource

import org.sopt.official.data.sopletter.dto.response.SopletterDefaultMessagesResponseDto
import org.sopt.official.data.sopletter.dto.response.SopletterMessageDetailResponseDto
import org.sopt.official.data.sopletter.dto.response.SopletterReportFormResponseDto

interface SopletterDataSource {
    suspend fun getDefaultMessages(
        cursor: Long?,
        size: Int,
    ): SopletterDefaultMessagesResponseDto

    suspend fun getReportForm(): SopletterReportFormResponseDto

    suspend fun getMessageDetail(
        topicId: Long,
        messageId: Long,
    ): SopletterMessageDetailResponseDto

    suspend fun addMessageLike(
        topicId: Long,
        messageId: Long,
    )

    suspend fun deleteMessageLike(
        topicId: Long,
        messageId: Long,
    )
}
