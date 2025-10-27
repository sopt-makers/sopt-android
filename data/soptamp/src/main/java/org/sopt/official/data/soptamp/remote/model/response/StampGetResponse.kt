package org.sopt.official.data.soptamp.remote.model.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.sopt.official.domain.soptamp.model.Archive

@Serializable
data class StampGetResponse(
    @SerialName("activityDate")
    val activityDate: String,
    @SerialName("createdAt")
    val createdAt: String? = null,
    @SerialName("updatedAt")
    val updatedAt: String? = null,
    @SerialName("id")
    val id: Int,
    @SerialName("contents")
    val contents: String,
    @SerialName("images")
    val images: List<String>? = null,
    @SerialName("missionId")
    val missionId: Int = -1,
    @SerialName("clapCount")
    val clapCount: Int = 0,
    @SerialName("viewCount")
    val viewCount: Int = 0,
    @SerialName("myClapCount")
    val myClapCount: Int? = null,
    @SerialName("mine")
    val mine: Boolean? = null
) {
    fun toDomain() = Archive(
        activityDate = activityDate,
        createdAt = createdAt,
        updatedAt = updatedAt,
        id = id,
        contents = contents,
        images = images ?: emptyList(),
        missionId = missionId,
        clapCount = clapCount,
        viewCount = viewCount,
        myClapCount = myClapCount,
        mine = mine
    )
}