package org.sopt.official.domain.repository

import org.sopt.official.domain.entity.auth.Token

interface AuthRepository {
    suspend fun authenticate(code: String): Token
    fun save(token: Token)
}
