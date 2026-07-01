package org.sopt.official.domain.sopletter.repository

import org.sopt.official.domain.sopletter.model.SopletterDefaultMessages
import org.sopt.official.domain.sopletter.model.SopletterMessageDetail

interface SopletterRepository {
    suspend fun getDefaultMessages(
        cursor: Long? = null,
        size: Int = DEFAULT_PAGE_SIZE,
    ): Result<SopletterDefaultMessages>

    suspend fun getReportFormUrl(): Result<String>

    suspend fun getMessageDetail(
        topicId: Long,
        messageId: Long,
    ): Result<SopletterMessageDetail>

    private companion object {
        const val DEFAULT_PAGE_SIZE = 20
    }
}
