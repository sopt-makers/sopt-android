package org.sopt.official.data.model.poke.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PokeFriendResponse(
    @SerialName("userId")
    val userId: Long,
    @SerialName("profileImage")
    val profileImage: String,
    @SerialName("name")
    val name: String,
    @SerialName("generation")
    val generation: String,
    @SerialName("part")
    val part: String,
    @SerialName("pockNum")
    val pockNum: Int,
    @SerialName("isAlreadyPoke")
    val isAlreadyPoke: Boolean
)