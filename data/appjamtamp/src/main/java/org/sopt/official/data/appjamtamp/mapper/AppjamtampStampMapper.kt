package org.sopt.official.data.appjamtamp.mapper

import org.sopt.official.data.appjamtamp.dto.response.AppjamtampStampResponseDto
import org.sopt.official.domain.appjamtamp.entity.AppjamtampStampEntity

internal fun AppjamtampStampResponseDto.toEntity(): AppjamtampStampEntity =
    AppjamtampStampEntity(
        stampId = this.id,
        contents = this.contents,
        images = this.images,
        activityDate = this.activityDate,
        createdAt = this.createdAt,
        updatedAt = this.updatedAt,
        missionId = this.missionId,
        teamNumber = this.teamNumber,
        teamName = this.teamName,
        ownerNickname = this.ownerNickname,
        ownerProfileImage = this.ownerProfileImage,
        clapCount = this.clapCount,
        viewCount = this.viewCount,
        myClapCount = this.myClapCount,
        isMine = this.isMine
    )
