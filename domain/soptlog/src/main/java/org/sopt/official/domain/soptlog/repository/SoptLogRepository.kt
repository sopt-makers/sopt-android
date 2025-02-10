package org.sopt.official.domain.soptlog.repository

import org.sopt.official.domain.soptlog.model.SoptLogInfo

interface SoptLogRepository {
    suspend fun getSoptLogInfo(): Result<SoptLogInfo>
}
