package org.sopt.official.data.sopletter.repository

import javax.inject.Inject
import org.sopt.official.common.coroutines.suspendRunCatching
import org.sopt.official.data.sopletter.datasource.SopletterDataSource
import org.sopt.official.data.sopletter.mapper.toDomain
import org.sopt.official.domain.sopletter.model.SopletterDefaultMessages
import org.sopt.official.domain.sopletter.repository.SopletterRepository

internal class SopletterRepositoryImpl @Inject constructor(
    private val sopletterDataSource: SopletterDataSource,
) : SopletterRepository {
    override suspend fun getDefaultMessages(
        cursor: Long?,
        size: Int,
    ): Result<SopletterDefaultMessages> = suspendRunCatching {
        sopletterDataSource.getDefaultMessages(
            cursor = cursor,
            size = size,
        ).toDomain()
    }
}
