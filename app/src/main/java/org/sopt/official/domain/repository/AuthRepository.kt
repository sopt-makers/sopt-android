package org.sopt.official.domain.repository

import org.sopt.official.domain.entity.auth.Token
import org.sopt.official.domain.entity.auth.UserStatus

interface AuthRepository {
    fun save(token: Token)
    fun save(status: UserStatus)
}
