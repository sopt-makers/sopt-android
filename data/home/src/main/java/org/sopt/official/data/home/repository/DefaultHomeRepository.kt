/*
 * MIT License
 * Copyright 2023-2025 SOPT - Shout Our Passion Together
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

import javax.inject.Inject
import javax.inject.Named
import org.sopt.official.cache.InMemoryCache
import org.sopt.official.common.coroutines.suspendRunCatching
import org.sopt.official.data.home.mapper.toDomain
import org.sopt.official.data.home.remote.api.CalendarApi
import org.sopt.official.data.home.remote.api.HomeApi
import org.sopt.official.domain.home.model.AppService
import org.sopt.official.domain.home.model.FloatingToast
import org.sopt.official.domain.home.model.HomeAppServiceInfo
import org.sopt.official.domain.home.model.LatestPost
import org.sopt.official.domain.home.model.PopularPost
import org.sopt.official.domain.home.model.RecentCalendar
import org.sopt.official.domain.home.model.ReviewForm
import org.sopt.official.domain.home.model.UserDescription
import org.sopt.official.domain.home.repository.HomeRepository

internal class DefaultHomeRepository @Inject constructor(
    private val homeApi: HomeApi,
    private val calendarApi: CalendarApi,
    @Named("homeAppService") private val homeAppServiceCache: InMemoryCache<HomeAppServiceInfo>,
    @Named("tabAppService") private val tabAppServiceCache: InMemoryCache<List<AppService>>,
) : HomeRepository {

    override suspend fun getRecentCalendar(): Result<RecentCalendar> =
        suspendRunCatching { calendarApi.getRecentCalendar().toDomain() }

    override suspend fun getHomeDescription(): Result<UserDescription> =
        suspendRunCatching { homeApi.getHomeDescription().toDomain() }

    override suspend fun getHomeAppService(forceRefresh: Boolean): Result<HomeAppServiceInfo> =
        suspendRunCatching {
            if (forceRefresh) homeAppServiceCache.invalidate()
            homeAppServiceCache.getOrFetch { homeApi.getHomeAppService().toDomain() }
        }

    override suspend fun getTabAppService(): Result<List<AppService>> =
        suspendRunCatching {
            tabAppServiceCache.getOrFetch { homeApi.getTabAppService().map { it.toDomain() } }
        }

    override suspend fun getHomeReviewForm(): Result<ReviewForm> =
        suspendRunCatching { homeApi.getReviewForm().toDomain() }

    override suspend fun getHomeFloatingToast(): Result<FloatingToast> =
        suspendRunCatching { homeApi.getHomeFloatingToast().toDomain() }

    override suspend fun getHomePopularPosts(): Result<List<PopularPost>> =
        suspendRunCatching { homeApi.getHomePopularPosts().popularPosts.map { it.toDomain() } }

    override suspend fun getHomeLatestPosts(): Result<List<LatestPost>> {
        return suspendRunCatching { homeApi.getHomeLatestPosts().recentPosts.map { it.toDomain() } }
    }
}
