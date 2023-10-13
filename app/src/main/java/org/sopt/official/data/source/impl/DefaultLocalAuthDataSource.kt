package org.sopt.official.data.source.impl

import org.sopt.official.data.persistence.SoptDataStore
import org.sopt.official.data.source.api.auth.LocalAuthDataSource
import org.sopt.official.domain.entity.auth.Token
import org.sopt.official.domain.entity.auth.UserStatus
import javax.inject.Inject

class DefaultLocalAuthDataSource @Inject constructor(
    private val dataStore: SoptDataStore
) : LocalAuthDataSource {
    override fun save(token: Token) {
        dataStore.apply {
            accessToken = token.accessToken
            refreshToken = token.refreshToken
            playgroundToken = token.playgroundToken
        }
    }

    override fun save(status: UserStatus) {
        dataStore.apply {
            userStatus = status.value
        }
    }

    override fun clear() {
        dataStore.clear()
    }
}
