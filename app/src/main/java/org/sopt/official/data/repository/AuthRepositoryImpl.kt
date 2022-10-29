package org.sopt.official.data.repository

import org.sopt.official.data.model.RequestAuthEmail
import org.sopt.official.data.persistence.SoptDataStore
import org.sopt.official.data.service.AuthService
import org.sopt.official.domain.repository.AuthRepository
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val service: AuthService,
    private val dataStore: SoptDataStore
) : AuthRepository {
    override suspend fun authenticateEmail(email: String, clientToken: String): Long {
        return service.authenticateEmail(RequestAuthEmail(email, clientToken)).userId
    }

    override fun saveUserToken(userId: Long) {
        dataStore.userId = userId
    }
}
