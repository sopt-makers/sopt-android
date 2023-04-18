package org.sopt.official.data.model.main

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.sopt.official.domain.entity.UserState
import org.sopt.official.domain.entity.main.MainViewOperationInfo
import org.sopt.official.domain.entity.main.MainViewResult
import org.sopt.official.domain.entity.main.MainViewUserInfo
import org.sopt.official.util.wrapper.asNullableWrapper

@Serializable
data class MainViewResponse(
    @SerialName("user")
    val user: MainViewUserResponse,
    @SerialName("operation")
    val operation: MainViewOperationResponse,
) {

    fun toEntity(): MainViewResult = MainViewResult(
        user = this.user.toEntity(),
        operation = this.operation.toEntity(),
    )

    @Serializable
    data class MainViewUserResponse(
        @SerialName("status")
        val status: String,
        @SerialName("name")
        val name: String?,
        @SerialName("profileImage")
        val profileImage: String?,
        @SerialName("generationList")
        val generationList: List<Long>?,
    ) {
        fun toEntity(): MainViewUserInfo = MainViewUserInfo(
            status = UserState.valueOf(this.status),
            name = this.name.asNullableWrapper(),
            profileImage = this.profileImage.asNullableWrapper(),
            generationList = this.generationList.asNullableWrapper(),
        )
    }

    @Serializable
    data class MainViewOperationResponse(
        @SerialName("attendanceScore")
        val attendanceScore: Double?,
        @SerialName("announcement")
        val announcement: String?,
    ) {
        fun toEntity(): MainViewOperationInfo = MainViewOperationInfo(
            attendanceScore = this.attendanceScore.asNullableWrapper(),
            announcement = this.announcement.asNullableWrapper(),
        )
    }
}
