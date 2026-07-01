package org.sopt.official.data.sopletter.datasourceimpl

import org.sopt.official.data.sopletter.datasource.SopletterDataSource
import org.sopt.official.data.sopletter.dto.response.SopletterDefaultMessagesResponseDto
import org.sopt.official.data.sopletter.dto.response.SopletterMessageDetailResponseDto
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

    override suspend fun getMessageDetail(
        topicId: Long,
        messageId: Long,
    ): SopletterMessageDetailResponseDto = sopletterService.getMessageDetail(
        topicId = topicId,
        messageId = messageId,
    )
}
