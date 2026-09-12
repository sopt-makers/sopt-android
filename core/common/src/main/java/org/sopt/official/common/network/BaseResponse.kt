package org.sopt.official.common.network

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * 통합 서버 공통 응답 포맷.
 *
 * 성공/실패 모두 이 형태로 내려오며 HTTP status는 기존과 동일하게 유지된다.
 * 실패 응답은 Retrofit이 errorBody로 분리하므로 이 타입으로 파싱되는 건 성공 응답뿐이다.
 * 성공인데 [data]가 비어 있을 수 있는 API는 `BaseResponse<Foo?>` 처럼 nullable 타입으로 선언한다.
 */
@Serializable
data class BaseResponse<T>(
    @SerialName("success")
    val success: Boolean,
    @SerialName("message")
    val message: String = "",
    @SerialName("data")
    val data: T
)
