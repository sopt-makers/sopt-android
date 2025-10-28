package org.sopt.official.data.soptamp.remote.model.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.sopt.official.domain.soptamp.model.StampClapResult

@Serializable
data class StampPostClapResponse(
    @SerialName("stampId")
    val stampId: Int,
    @SerialName("appliedCount")
    val appliedCount: Int,
    @SerialName("totalClapCount")
    val totalClapCount: Int,
) {
    fun toDomain() = StampClapResult(
        stampId = stampId,
        appliedCount = appliedCount,
        totalClapCount = totalClapCount
    )
}
