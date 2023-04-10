package org.sopt.official.data.repository

import org.sopt.official.data.persistence.SoptDataStore
import org.sopt.official.domain.repository.AuthRepository
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val dataStore: SoptDataStore
) : AuthRepository
