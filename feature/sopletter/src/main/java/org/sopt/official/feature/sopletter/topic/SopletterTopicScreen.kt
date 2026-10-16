/*
 * MIT License
 * Copyright 2026 SOPT - Shout Our Passion Together
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
package org.sopt.official.feature.sopletter.topic

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.collections.immutable.persistentListOf
import org.sopt.official.designsystem.component.dialog.NetworkErrorDialog
import org.sopt.official.domain.sopletter.model.SopletterTopic
import org.sopt.official.feature.sopletter.common.component.SopletterTopbar
import org.sopt.official.feature.sopletter.common.component.WriteSopletterFloatingActionButton
import org.sopt.official.feature.sopletter.topic.component.SopletterTopicItem
import org.sopt.official.mds.theme.SoptTheme

@Composable
internal fun SopletterTopicRoute(
    navigateUp: () -> Unit,
    navigateToMain: (Long) -> Unit,
    navigateToWrite: () -> Unit,
    viewModel: SopletterTopicViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    SopletterTopicScreen(
        uiState = uiState,
        onBackClick = navigateUp,
        onTopicClick = navigateToMain,
        onWriteClick = navigateToWrite,
        onErrorConfirm = viewModel::retryFetchTopics,
    )
}

@Composable
private fun SopletterTopicScreen(
    uiState: SopletterTopicState,
    onBackClick: () -> Unit,
    onTopicClick: (Long) -> Unit,
    onWriteClick: () -> Unit,
    onErrorConfirm: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(
                color = SoptTheme.colors.bg.layer.basement
            ),
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
        ) {
            SopletterTopbar(
                onBackClick = onBackClick,
                topbarTitle = "솝레터 주제"
            )

            Spacer(modifier = Modifier.height(10.dp))

            LazyColumn(
                modifier = Modifier.weight(1f),
            ) {
                items(
                    items = uiState.topicList,
                    key = { it.topicId }
                ) { item ->
                    SopletterTopicItem(
                        modifier = Modifier.padding(horizontal = 20.dp),
                        topicTitle = item.title,
                        topicSubTitle = item.subTitle,
                        isHot = item.isHot,
                        isNew = item.isNew,
                        unReadCount = item.unreadCount,
                        onTopicClick = {
                            onTopicClick(item.topicId)
                        }
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                }
            }

            Spacer(modifier = Modifier.weight(1f))
        }

        WriteSopletterFloatingActionButton(
            onWriteFABClick = onWriteClick,
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(end = 20.dp, bottom = 28.dp),
        )
    }

    if (uiState.isShowErrorDialog) {
        NetworkErrorDialog(onConfirm = onErrorConfirm)
    }
}

@Preview
@Composable
private fun SopletterTopicScreenPreview() {
    SoptTheme {
        SopletterTopicScreen(
            uiState = SopletterTopicState(
                topicList = persistentListOf(
                    SopletterTopic(
                        topicId = 1,
                        title = "38기 회고",
                        subTitle = "자유롭게 수다 나누는 방방",
                        isNew = true,
                        isHot = true,
                        unreadCount = 0,
                    ),
                    SopletterTopic(
                        topicId = 2,
                        title = "38기 회고",
                        subTitle = "자유롭게 수다 나누는 방방",
                        isNew = false,
                        isHot = true,
                        unreadCount = 3,
                    ),
                    SopletterTopic(
                        topicId = 3,
                        title = "38기 회고",
                        subTitle = "자유롭게 수다 나누는 방방",
                        isNew = false,
                        isHot = false,
                        unreadCount = 3,
                    ),
                    SopletterTopic(
                        topicId = 4,
                        title = "38기 회고",
                        subTitle = "자유롭게 수다 나누는 방방",
                        isNew = false,
                        isHot = false,
                        unreadCount = 0,
                    ),
                    SopletterTopic(
                        topicId = 5,
                        title = "38기 회고",
                        subTitle = "자유롭게 수다 나누는 방방",
                        isNew = true,
                        isHot = false,
                        unreadCount = 3,
                    ),
                )
            ),
            onBackClick = {},
            onTopicClick = {},
            onWriteClick = {},
            onErrorConfirm = {},
        )
    }
}
