/*
 * MIT License
 * Copyright 2023-2024 SOPT - Shout Our Passion Together
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package org.sopt.official.data.repository.home

import javax.inject.Inject
import org.sopt.official.data.service.home.HomeService
import org.sopt.official.domain.entity.home.AppService
import org.sopt.official.domain.entity.home.HomeSection
import org.sopt.official.domain.entity.home.SoptUser
import org.sopt.official.domain.repository.home.HomeRepository

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

    override suspend fun getAppService(): Result<List<AppService>> {
        return runCatching {
            homeService.getAppService().map { it.toEntity() }
        }
    }
}
