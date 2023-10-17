/*
 * Copyright 2023 SOPT - Shout Our Passion Together
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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
