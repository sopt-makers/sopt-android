package org.sopt.official.data.sopletter.repository

import org.sopt.official.common.coroutines.suspendRunCatching
import org.sopt.official.data.sopletter.datasource.SopletterWriteDataSource
import org.sopt.official.data.sopletter.dto.SopletterWriteRequest
import org.sopt.official.data.sopletter.mapper.toDomain
import org.sopt.official.sopletter.model.SopletterWrite
import org.sopt.official.sopletter.repository.SopletterWriteRepository
import javax.inject.Inject

internal class SopletterWriteRepositoryImpl @Inject constructor(
    private val dataSource: SopletterWriteDataSource
) : SopletterWriteRepository {

    override suspend fun postSopletter(topicId: Long?, content: String): Result<SopletterWrite> = suspendRunCatching  {
        val request = SopletterWriteRequest(content)
        val response = dataSource.postTopicSopletter(topicId!!, request)

        response.toDomain()
    }
}