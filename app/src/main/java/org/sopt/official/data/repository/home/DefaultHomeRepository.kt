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
package org.sopt.official.data.repository.home

import org.sopt.official.data.service.home.HomeService
import org.sopt.official.domain.entity.home.HomeSection
import org.sopt.official.domain.entity.home.SoptUser
import org.sopt.official.domain.repository.home.HomeRepository
import javax.inject.Inject

class DefaultHomeRepository @Inject constructor(
    private val homeService: HomeService,
) : HomeRepository {
    override suspend fun getMainView(): Result<SoptUser> {
        return runCatching {
            homeService.getMainView().toEntity()
        }
    }

    override suspend fun getMainDescription(): Result<HomeSection> {
        return runCatching {
            homeService.getMainDescription().toEntity()
        }
    }
}
