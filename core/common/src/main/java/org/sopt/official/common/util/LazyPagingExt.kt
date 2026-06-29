package org.sopt.official.common.util

import androidx.compose.foundation.lazy.staggeredgrid.LazyStaggeredGridState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.snapshotFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filter

@Composable
fun LazyStaggeredGridState.onBottomReached(
    threshold: Int,
    hasNext: Boolean,
    isPaging: Boolean,
    onLoadMore: () -> Unit,
) {
    require(threshold >= 0) { "threshold cannot be negative, but was $threshold" }

    LaunchedEffect(this, hasNext, isPaging) {
        snapshotFlow {
            val totalItemCount = layoutInfo.totalItemsCount
            val lastVisibleItemIndex = layoutInfo.visibleItemsInfo.lastOrNull()?.index ?: -1

            hasNext && !isPaging && totalItemCount > 0 &&
                lastVisibleItemIndex >= totalItemCount - 1 - threshold
        }
            .distinctUntilChanged()
            .filter { it }
            .collect { onLoadMore() }
    }
}
