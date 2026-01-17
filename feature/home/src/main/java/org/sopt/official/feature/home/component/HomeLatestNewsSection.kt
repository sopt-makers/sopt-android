/*
 * MIT License
 * Copyright 2025 SOPT - Shout Our Passion Together
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
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.sopt.official.designsystem.SoptTheme
import org.sopt.official.feature.home.R
import org.sopt.official.feature.home.model.HomePlaygroundPostModel

private const val AUTO_SCROLL_DELAY = 3000L
private const val AUTO_SCROLL_ANIMATION_DELAY = 450

@Composable
fun HomeLatestNewsSection(
    feedList: ImmutableList<HomePlaygroundPostModel>,
    navigateToPlayground: () -> Unit,
    navigateToWebLink: (String) -> Unit,
    navigateToMemberProfile: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    val coroutineScope = rememberCoroutineScope()
    val pageCount = feedList.size
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

            with(feedList[page]) {
                if (isOutdated) {
                    HomePlaygroundEmptyPost(
                        category = category,
                        description = content,
                        iconUrl = profileImage,
                        title = title,
                        onClick = { navigateToWebLink(webLink) }
                    )
                } else {
                    HomePlaygroundPost(
                        profileImage = profileImage,
                        userName = name,
                        userPart = generationAndPart,
                        label = label,
                        category = category,
                        title = title,
                        description = content,
                        isAnonymous = userId == null,
                        onClick = { navigateToWebLink(webLink) },
                        onProfileClick = { if (userId != null) navigateToMemberProfile(userId) }
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
