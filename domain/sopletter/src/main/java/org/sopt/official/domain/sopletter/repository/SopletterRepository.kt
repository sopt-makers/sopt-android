package org.sopt.official.domain.sopletter.repository

import org.sopt.official.domain.sopletter.model.SopletterDefaultMessages

interface SopletterRepository {
    suspend fun getDefaultMessages(
        cursor: Long? = null,
        size: Int = DEFAULT_PAGE_SIZE,
    ): Result<SopletterDefaultMessages>

    suspend fun getReportFormUrl(): Result<String>

    private companion object {
        const val DEFAULT_PAGE_SIZE = 20
    }
}
