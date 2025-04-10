/*
 * MIT License
 * Copyright 2024-2025 SOPT - Shout Our Passion Together
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
package org.sopt.official.feature.notification.all

import androidx.paging.PagingSource
import androidx.paging.PagingState
import org.sopt.official.domain.notification.entity.NotificationItem
import org.sopt.official.domain.notification.repository.NotificationRepository

class NotificationPagingSource(
    private val repository: NotificationRepository,
    private val notificationCategory: NotificationCategory
) : PagingSource<Int, NotificationItem>() {
    override fun getRefreshKey(state: PagingState<Int, NotificationItem>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, NotificationItem> {
        val page = params.key ?: 0

        val notifications = when (notificationCategory) {
            NotificationCategory.ALL -> repository.getNotificationHistory(page)
            else -> repository.getNotificationHistoryByCategory(page = page, category = notificationCategory.serverCode)
        }.getOrElse { return LoadResult.Error(it) }

        return LoadResult.Page(
            data = notifications,
            prevKey = if (page == 0) null else page - 1,
            nextKey = if (notifications.isEmpty()) null else page + 1
        )
    }
}
