package org.sopt.official.data.model.home

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.sopt.official.domain.entity.home.HomeSection

@Serializable
data class DescriptionViewResponse(
    @SerialName("topDescription")
    val topDescription: String,
    @SerialName("bottomDescription")
    val bottomDescription: String,
) {
    fun toEntity(): HomeSection = HomeSection(
        topDescription = this.topDescription,
        bottomDescription = this.bottomDescription,
    )
}