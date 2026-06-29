package org.sopt.official.data.sopletter.datasourceimpl

import org.sopt.official.data.sopletter.datasource.SopletterDataSource
import org.sopt.official.data.sopletter.dto.response.SopletterDefaultMessagesResponseDto
import org.sopt.official.data.sopletter.dto.response.SopletterReportFormResponseDto
import org.sopt.official.data.sopletter.service.SopletterService
import javax.inject.Inject

internal class SopletterDataSourceImpl @Inject constructor(
    private val sopletterService: SopletterService,
) : SopletterDataSource {
    override suspend fun getDefaultMessages(
        cursor: Long?,
        size: Int,
    ): SopletterDefaultMessagesResponseDto = sopletterService.getDefaultMessages(
        cursor = cursor,
        size = size,
    )

    override suspend fun getReportForm(): SopletterReportFormResponseDto = sopletterService.getReportForm()
}
