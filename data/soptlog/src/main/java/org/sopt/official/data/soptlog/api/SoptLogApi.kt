package org.sopt.official.data.soptlog.api

import org.sopt.official.data.soptlog.dto.SoptLogInfoResponse
import retrofit2.http.GET

interface SoptLogApi {

    @GET("user/sopt-log")
    suspend fun getSoptLogInfo(): SoptLogInfoResponse
}
