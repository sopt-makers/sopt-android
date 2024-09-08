package org.sopt.official.feature.notification.all

import androidx.paging.PagingSource
import androidx.paging.PagingState
import org.sopt.official.domain.notification.entity.NotificationItem
import org.sopt.official.domain.notification.repository.NotificationRepository

class NotificationPagingSource(
  private val repository: NotificationRepository
) : PagingSource<Int, NotificationItem>() {
  override fun getRefreshKey(state: PagingState<Int, NotificationItem>): Int? {
    return state.anchorPosition?.let { anchorPosition ->
      val anchorPage = state.closestPageToPosition(anchorPosition)
      anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
    }
  }

  override suspend fun load(params: LoadParams<Int>): LoadResult<Int, NotificationItem> {
    val page = params.key ?: 0
    val notifications = repository.getNotificationHistory(page).getOrElse { return LoadResult.Error(it) }
    return LoadResult.Page(
      data = notifications,
      prevKey = if (page == 0) null else page - 1,
      nextKey = if (notifications.isEmpty()) null else page + 1
    )
  }
}
