package org.sopt.official.data.source.api.auth

import org.sopt.official.domain.entity.auth.Token
import org.sopt.official.domain.entity.auth.UserStatus

interface LocalAuthDataSource {
    fun save(token: Token)
    fun save(status: UserStatus)
}
