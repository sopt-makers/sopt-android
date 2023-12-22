package org.sopt.official.data.poke.dto.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.sopt.official.domain.poke.entity.CheckNewInPoke

@Serializable
data class CheckNewInPokeResult(
    @SerialName("isNew")
    val isNew: Boolean,
) {
    fun toEntity(): CheckNewInPoke = CheckNewInPoke(
        isNew = isNew,
    )
}