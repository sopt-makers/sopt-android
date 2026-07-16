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
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.collections.immutable.persistentListOf
import org.sopt.official.designsystem.SoptTheme
import org.sopt.official.designsystem.component.dialog.NetworkErrorDialog
import org.sopt.official.domain.sopletter.model.SopletterTopic
import org.sopt.official.feature.sopletter.common.component.SopletterTopbar
import org.sopt.official.feature.sopletter.topic.component.SopletterTopicItem

@Composable
internal fun SopletterTopicRoute(
    navigateUp: () -> Unit,
    navigateToMain: (Long) -> Unit,
    viewModel: SopletterTopicViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    SopletterTopicScreen(
        uiState = uiState,
        onBackClick = navigateUp,
        onTopicClick = navigateToMain,
        onErrorConfirm = viewModel::retryFetchTopics,
    )
}

@Composable
private fun SopletterTopicScreen(
    uiState: SopletterTopicState,
    onBackClick: () -> Unit,
    onTopicClick: (Long) -> Unit,
    onErrorConfirm: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(
                color = SoptTheme.colors.background
            ),
    ) {
        SopletterTopbar(
            onBackClick = onBackClick,
            topbarTitle = "솝레터 주제"
        )

        Spacer(modifier = Modifier.height(10.dp))

        uiState.topicList.forEachIndexed { index, item ->
            SopletterTopicItem(
                modifier = Modifier
                    .padding(horizontal = 20.dp),
                topicTitle = item.title,
                onTopicClick = {
                    onTopicClick(item.topicId)
                }
            )
            if (index < uiState.topicList.lastIndex) {
                Spacer(modifier = Modifier.height(12.dp))
            }
        }

        Spacer(modifier = Modifier.weight(1f))
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
                    ),
                    SopletterTopic(
                        topicId = 2,
                        title = "38기 회고",
                    ),

                )
            ),
            onBackClick = {},
            onTopicClick = {},
            onErrorConfirm = {},
        )
    }
}
