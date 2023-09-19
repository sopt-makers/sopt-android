package org.sopt.official.data.model.main

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.sopt.official.domain.entity.UserState
import org.sopt.official.domain.entity.main.MainDescriptionViewResult
import org.sopt.official.domain.entity.main.MainViewOperationInfo
import org.sopt.official.domain.entity.main.MainViewResult
import org.sopt.official.domain.entity.main.MainViewUserInfo
import org.sopt.official.util.wrapper.asNullableWrapper

@Serializable
data class DescriptionViewResponse(
    @SerialName("topDescription")
    val topDescription: String,
    @SerialName("bottomDescription")
    val bottomDescription: String,
) {
    fun toEntity(): MainDescriptionViewResult = MainDescriptionViewResult(
        topDescription = this.topDescription,
        bottomDescription = this.bottomDescription,
    )
}