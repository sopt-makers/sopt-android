/*
 * MIT License
 * Copyright 2024 SOPT - Shout Our Passion Together
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
package org.sopt.official.data.home.repository

import org.sopt.official.data.home.mapper.toDomain
import org.sopt.official.data.home.remote.api.CalendarApi
import org.sopt.official.data.home.remote.api.HomeApi
import org.sopt.official.data.home.remote.api.UserApi
import org.sopt.official.domain.home.model.AppService
import org.sopt.official.domain.home.model.RecentCalendar
import org.sopt.official.domain.home.model.UserInfo
import org.sopt.official.domain.home.model.UserInfo.UserDescription
import org.sopt.official.domain.home.repository.HomeRepository
import org.sopt.official.domain.home.result.Result
import org.sopt.official.domain.home.result.runCatching
import javax.inject.Inject

internal class DefaultHomeRepository @Inject constructor(
    private val userApi: UserApi,
    private val homeApi: HomeApi,
    private val calendarApi: CalendarApi,
) : HomeRepository {

    override suspend fun getUserInfo(): Result<UserInfo> =
        runCatching { userApi.getUserMain().toDomain() }

    override suspend fun getRecentCalendar(): Result<RecentCalendar> =
        runCatching { calendarApi.getRecentCalendar().toDomain() }

    override suspend fun getHomeDescription(): Result<UserDescription> =
        runCatching { homeApi.getHomeDescription().toDomain() }

    override suspend fun getHomeAppService(): Result<List<AppService>> =
        runCatching { homeApi.getHomeAppService().map { it.toDomain() } }
}
