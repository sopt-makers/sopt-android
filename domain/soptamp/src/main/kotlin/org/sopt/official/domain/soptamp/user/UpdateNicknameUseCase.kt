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
package org.sopt.official.domain.soptamp.user

import org.sopt.official.domain.soptamp.repository.UserRepository
import javax.inject.Inject

class UpdateNicknameUseCase @Inject constructor(
    private val repository: UserRepository
) {
    suspend operator fun invoke(nickname: String) = runCatching {
        repository.updateNickname(nickname)
    }
}