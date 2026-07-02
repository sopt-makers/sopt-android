package org.sopt.official.data.sopletter.repository

import org.sopt.official.common.coroutines.suspendRunCatching
import org.sopt.official.data.sopletter.datasource.SopletterDataSource
import org.sopt.official.data.sopletter.dto.SopletterWriteRequest
import org.sopt.official.data.sopletter.mapper.toDomain
import org.sopt.official.sopletter.model.Sopletter
import org.sopt.official.sopletter.repository.SopletterWriteRepository
import javax.inject.Inject

internal class SopletterRepositoryImpl @Inject constructor(
    private val dataSource: SopletterDataSource
) : SopletterWriteRepository {

    override suspend fun postSopletter(topicId: Long?, content: String): Result<Sopletter> = suspendRunCatching  {
        val request = SopletterWriteRequest(content)
        val response = dataSource.postTopicSopletter(topicId!!, request)

        response.toDomain()
    }
}