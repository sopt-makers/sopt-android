package org.sopt.official.data.model.home

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.sopt.official.domain.entity.home.AppService

@Serializable
data class AppServiceResponse(
    @SerialName("serviceName")
    val serviceName: String,
    @SerialName("activeUser")
    val activeUser: Boolean,
    @SerialName("inactiveUser")
    val inactiveUser: Boolean
) {
    fun toEntity(): AppService = AppService(
        serviceName = serviceName,
        activeUser = activeUser,
        inactiveUser = inactiveUser
    )
}
