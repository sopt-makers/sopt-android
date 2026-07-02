package org.sopt.official.data.sopletter.datasourceimpl

import org.sopt.official.data.sopletter.datasource.SopletterDataSource
import org.sopt.official.data.sopletter.dto.SopletterWriteRequest
import org.sopt.official.data.sopletter.dto.SopletterWriteResponse
import org.sopt.official.data.sopletter.api.SopletterService
import javax.inject.Inject

internal class SopletterDataSourceImpl @Inject constructor(
    private val service: SopletterService
) : SopletterDataSource {
    override suspend fun postTopicSopletter(topicId: Long, request: SopletterWriteRequest): SopletterWriteResponse {
        return service.postTopicSopletter(topicId, request)
    }
}