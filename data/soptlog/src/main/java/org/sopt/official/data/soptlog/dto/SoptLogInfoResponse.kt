package org.sopt.official.data.soptlog.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SoptLogInfoResponse(
    @SerialName("userName")
    val userName: String?,
    @SerialName("profileImage")
    val profileImageUrl: String?,
    @SerialName("part")
    val part: String?,
    @SerialName("soptampRank")
    val soptampRank: String?,
    @SerialName("pokeCount")
    val pokeCount: String?,
    @SerialName("soptLevel")
    val soptLevel: String?,
    @SerialName("during")
    val during: String?,
    @SerialName("profileMessage")
    val profileMessage: String?,
    @SerialName("icons")
    val icons: List<String?>,
    @SerialName("isActive")
    val isActive: Boolean,
    @SerialName("isFortuneChecked")
    val isFortuneChecked: Boolean,
    @SerialName("todayFortuneText")
    val todayFortuneTitle: String?,
)
