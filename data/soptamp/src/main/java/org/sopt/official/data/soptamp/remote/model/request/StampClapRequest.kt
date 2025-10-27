package org.sopt.official.data.soptamp.remote.model.request

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.sopt.official.domain.soptamp.model.StampClap

@Serializable
data class StampClapRequest(
    @SerialName("clapCount")
    val clapCount: Int
)

fun StampClap.toData(): StampClapRequest {
    return StampClapRequest(
        clapCount = this.clapCount
    )
}
