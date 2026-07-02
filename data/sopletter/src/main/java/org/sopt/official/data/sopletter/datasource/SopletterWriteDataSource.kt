package org.sopt.official.data.sopletter.datasource

import org.sopt.official.data.sopletter.dto.SopletterWriteRequest
import org.sopt.official.data.sopletter.dto.SopletterWriteResponse

interface SopletterDataSource {
    suspend fun postTopicSopletter(topicId: Long, request: SopletterWriteRequest): SopletterWriteResponse
}