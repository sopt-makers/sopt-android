package org.sopt.official.data.sopletter.datasource

import org.sopt.official.data.sopletter.dto.response.SopletterDefaultMessagesResponseDto

interface SopletterDataSource {
    suspend fun getDefaultMessages(
        cursor: Long?,
        size: Int,
    ): SopletterDefaultMessagesResponseDto
}
