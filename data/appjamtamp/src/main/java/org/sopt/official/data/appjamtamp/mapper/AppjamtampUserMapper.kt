package org.sopt.official.data.appjamtamp.mapper

import org.sopt.official.data.appjamtamp.dto.response.AppjamtampPostStampResponseDto
import org.sopt.official.domain.appjamtamp.entity.AppjamtampUser

fun AppjamtampPostStampResponseDto.toDomain(): AppjamtampUser =
    AppjamtampUser(
        nickname = this.ownerNickname,
        profileImage = this.ownerProfileImage
    )
