package org.sopt.official.data.appjamtamp.dto.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.sopt.official.domain.appjamtamp.entity.AppjamtampMyAppjamInfoEntity

@Serializable
data class AppjamtampMyAppjamInfoResponseDto(
    @SerialName("teamNumber")
    val teamNumber: String,

    @SerialName("teamName")
    val teamName: String,

    @SerialName("isAppjamJoined")
    val isAppjamJoined: Boolean
) {
    fun toEntity(): AppjamtampMyAppjamInfoEntity {
        return AppjamtampMyAppjamInfoEntity(
            teamNumber = teamNumber,
            teamName = teamName,
            isAppjamJoined = isAppjamJoined
        )
    }
}
