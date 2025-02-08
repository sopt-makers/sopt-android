package org.sopt.official.data.soptlog.repository

import org.sopt.official.data.soptlog.api.SoptLogApi
import org.sopt.official.data.soptlog.mapper.toDomain
import org.sopt.official.domain.soptlog.model.SoptLogInfo
import org.sopt.official.domain.soptlog.repository.SoptLogRepository
import javax.inject.Inject


internal class DefaultSoptLogRepository @Inject constructor(
    private val soptLogApi: SoptLogApi,
) : SoptLogRepository {
    override suspend fun getSoptLogInfo(): Result<SoptLogInfo> = runCatching {
        soptLogApi.getSoptLogInfo().toDomain()
    }
}
