package org.sopt.official.domain.repository

import org.sopt.official.domain.entity.auth.Auth
import org.sopt.official.domain.entity.auth.Token
import org.sopt.official.domain.entity.auth.UserStatus

interface AuthRepository {
    suspend fun refresh(token: String): Result<Auth>
    fun save(token: Token)
    fun save(status: UserStatus)
    suspend fun withdraw(): Result<Unit>
    suspend fun logout(): Result<Unit>
}
