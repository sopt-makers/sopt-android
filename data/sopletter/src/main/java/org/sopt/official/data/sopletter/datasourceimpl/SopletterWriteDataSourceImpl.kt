package org.sopt.official.data.sopletter.datasourceimpl

import org.sopt.official.data.sopletter.api.SopletterWriteService
import org.sopt.official.data.sopletter.datasource.SopletterWriteDataSource
import org.sopt.official.data.sopletter.dto.SopletterWriteRequest
import org.sopt.official.data.sopletter.dto.SopletterWriteResponse
import javax.inject.Inject

internal class SopletterWriteDataSourceImpl @Inject constructor(
    private val service: SopletterWriteService
) : SopletterWriteDataSource {
    override suspend fun postTopicSopletter(topicId: Long, request: SopletterWriteRequest): SopletterWriteResponse {
        return service.postTopicSopletter(topicId, request)
    }
}