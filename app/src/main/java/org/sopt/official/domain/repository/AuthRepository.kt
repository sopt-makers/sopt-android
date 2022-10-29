package org.sopt.official.domain.repository

interface AuthRepository {
    suspend fun authenticateEmail(email: String, clientToken: String): Long
}
