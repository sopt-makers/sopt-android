package org.sopt.official.feature.home.component

import androidx.compose.animation.core.tween
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.sopt.official.designsystem.SoptTheme
import org.sopt.official.feature.home.R
import org.sopt.official.feature.home.model.HomePlaygroundEmptyPostModel
import org.sopt.official.feature.home.model.HomePlaygroundPostModel

internal val emptyFeedList = persistentListOf(
    HomePlaygroundEmptyPostModel(
        category = "[자유]",
        description = "에 오늘의 TMI 적어봐!",
        iconUrl = "https://avatars.githubusercontent.com/u/98209004?v=4",
        webLink = "https://playground.sopt.org/?category=21&feed="
    )
)

private const val CATEGORY_COUNT = 5
private const val AUTO_SCROLL_DELAY = 3000L
private const val AUTO_SCROLL_ANIMATION_DELAY = 450

@Composable
internal fun HomeLatestNewsSection(
    feedList: ImmutableList<HomePlaygroundPostModel>,
    emptyFeedList: ImmutableList<HomePlaygroundEmptyPostModel>,
    navigateToPlayground: () -> Unit,
    navigateToFeed: (Int) -> Unit,
    navigateToWebLink: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val coroutineScope = rememberCoroutineScope()
    val pageCount = CATEGORY_COUNT
    val pagerState = rememberPagerState(
        initialPage = 0,
        initialPageOffsetFraction = 0f,
        pageCount = { Int.MAX_VALUE }
    )

    LaunchedEffect(Unit) {
        while (true) {
            delay(AUTO_SCROLL_DELAY)
            coroutineScope.launch {
                val nextPage = pagerState.currentPage + 1
                pagerState.animateScrollToPage(
                    page = nextPage,
                    animationSpec = tween(durationMillis = AUTO_SCROLL_ANIMATION_DELAY)
                )
            }
        }
    }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
    ) {
        TitleSection(onClick = navigateToPlayground)

        Spacer(modifier = Modifier.height(16.dp))

        HorizontalPager(
            state = pagerState,
            beyondViewportPageCount = 1,
            contentPadding = PaddingValues(horizontal = 20.dp),
            pageSpacing = 20.dp
        ) { currentPage ->
            val page = currentPage % pageCount

            if (page < feedList.size) {
                with(feedList[page]) {
                    HomePlaygroundPost(
                        profileImage = profileImage,
                        userName = name,
                        userPart = generationAndPart,
                        label = label,
                        category = category,
                        title = title,
                        description = content,
                        onClick = { navigateToFeed(postId) }
                    )
                }
            } else {
                with(emptyFeedList[page - feedList.size]) {
                    HomePlaygroundEmptyPost(
                        category = category,
                        description = description,
                        iconUrl = iconUrl,
                        onClick = { navigateToWebLink(webLink) }
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(18.dp))

        HomePageIndicator(
            numberOfPages = pageCount,
            selectedPage = pagerState.currentPage
        )
    }
}

@Composable
private fun TitleSection(
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp)
    ) {
        Text(
            text = "최신 게시물",
            style = SoptTheme.typography.heading20B,
            color = SoptTheme.colors.primary
        )
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .clickable(onClick = onClick)
        ) {
            Text(
                text = "전체보기",
                style = SoptTheme.typography.label12SB,
                color = SoptTheme.colors.onSurface300
            )
            Icon(
                imageVector = ImageVector.vectorResource(R.drawable.ic_arrow_right),
                contentDescription = null,
                tint = SoptTheme.colors.onSurface300,
                modifier = Modifier
                    .size(16.dp)
            )
        }
    }
}
