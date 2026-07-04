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
package org.sopt.official.feature.sopletter.main

import android.content.Intent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridItemSpan
import androidx.compose.foundation.lazy.staggeredgrid.itemsIndexed
import androidx.compose.foundation.lazy.staggeredgrid.rememberLazyStaggeredGridState
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.PullRefreshState
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.flowWithLifecycle
import org.sopt.official.common.util.onBottomReached
import org.sopt.official.designsystem.SoptTheme
import org.sopt.official.designsystem.component.dialog.NetworkErrorDialog
import org.sopt.official.domain.sopletter.model.SopletterMessage
import org.sopt.official.feature.sopletter.common.component.SopletterScaffold
import org.sopt.official.feature.sopletter.common.component.SopletterSnackbarHost
import org.sopt.official.feature.sopletter.main.component.EmptySopletterContent
import org.sopt.official.feature.sopletter.main.component.SopletterDeleteDialog
import org.sopt.official.feature.sopletter.main.component.SopletterMainTopBar
import org.sopt.official.feature.sopletter.main.component.SopletterMemoCard
import org.sopt.official.feature.sopletter.main.component.SopletterMemoDetailDialog
import org.sopt.official.feature.sopletter.main.component.SopletterTopicCta
import org.sopt.official.feature.sopletter.main.component.WriteSopletterFloatingActionButton
import org.sopt.official.feature.sopletter.main.contract.SopletterMainSideEffect
import org.sopt.official.feature.sopletter.main.contract.SopletterMemoDetailDialogContract
import org.sopt.official.feature.sopletter.main.model.SopletterMainUiState
import org.sopt.official.feature.sopletter.main.model.memoColor
import org.sopt.official.feature.sopletter.main.preview.SopletterMainPreviewParameterProvider
import org.sopt.official.webview.view.WebViewActivity

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun SopletterMainRoute(
    viewModel: SopletterMainViewModel = hiltViewModel(),
    navigateUp: () -> Unit = {},
    navigateToTopic: () -> Unit = {},
    navigateToWrite: (Long?) -> Unit = {},
    navigateToPrint: (Long?) -> Unit = {},
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }
    val dialogActions: SopletterMemoDetailDialogContract.Actions = viewModel
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current

    LaunchedEffect(Unit) {
        viewModel.sideEffect.flowWithLifecycle(lifecycleOwner.lifecycle)
            .collect { sideEffect ->
                when (sideEffect) {
                    is SopletterMainSideEffect.NavigateToReportForm -> {
                        Intent(context, WebViewActivity::class.java).apply {
                            putExtra(WebViewActivity.INTENT_URL, sideEffect.url)
                            context.startActivity(this)
                        }
                    }

                    is SopletterMainSideEffect.ShowSnackbar -> {
                        snackbarHostState.showSnackbar(sideEffect.visuals)
                    }
                }
            }
    }

    SopletterScaffold(
        snackbarHostState = snackbarHostState,
        isSnackbarHostVisible = false,
    ) { paddingValues ->
        SopletterMainScreen(
            uiState = uiState,
            snackbarHostState = snackbarHostState,
            onMemoClick = {
                viewModel.fetchMemoDetail(
                    messageId = it.messageId,
                    memoColor = it.memoColor(),
                )
            },
            onRefresh = viewModel::fetchMessages,
            onLoadMore = { viewModel.fetchMessages(isLoadMore = true) },
            dialogActions = dialogActions,
            onBackClick = navigateUp,
            onDownloadClick = {navigateToPrint(uiState.selectedTopicId) },
            onTopicClick = navigateToTopic,
            onReportClick = viewModel::openReportForm,
            onErrorConfirm = viewModel::dismissErrorDialog,
            onWriteFABClick = { navigateToWrite(uiState.selectedTopicId) },
            modifier = Modifier.padding(paddingValues),
        )
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
private fun SopletterMainScreen(
    uiState: SopletterMainUiState,
    snackbarHostState: SnackbarHostState,
    onMemoClick: (SopletterMessage) -> Unit,
    onRefresh: () -> Unit,
    onLoadMore: () -> Unit,
    dialogActions: SopletterMemoDetailDialogContract.Actions,
    onBackClick: () -> Unit,
    onDownloadClick: () -> Unit,
    onReportClick: () -> Unit,
    onTopicClick: () -> Unit,
    onErrorConfirm: () -> Unit,
    onWriteFABClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val gridState = rememberLazyStaggeredGridState()
    val refreshState = rememberPullRefreshState(
        refreshing = uiState.isMessageRefreshing,
        onRefresh = onRefresh,
    )
    val isTopicCtaVisible = uiState.selectedTopicId == null

    gridState.onBottomReached(
        threshold = 4,
        hasNext = uiState.hasNext,
        isPaging = uiState.isPaging,
        onLoadMore = onLoadMore,
    )

    Box(
        modifier = modifier.fillMaxSize(),
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(color = SoptTheme.colors.background),
        ) {
            SopletterMainTopBar(
                title = uiState.topicTitle,
                isTopicDetail = uiState.selectedTopicId != null,
                isDownloadBtnVisible = uiState.memoList.isNotEmpty(),
                onBackClick = onBackClick,
                onDownloadClick = onDownloadClick,
                onReportClick = onReportClick,
                onTopicClick = if (uiState.selectedTopicId == null) onTopicClick else onBackClick,
            )

            when {
                !uiState.isInitialized -> {
                    SopletterPullRefreshContainer(
                        refreshState = refreshState,
                        isRefreshing = uiState.isMessageRefreshing,
                        isShowIndicator = false,
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(SoptTheme.colors.background),
                        )
                    }
                }

                uiState.memoList.isEmpty() -> {
                    SopletterPullRefreshContainer(
                        refreshState = refreshState,
                        isRefreshing = uiState.isMessageRefreshing,
                        isShowIndicator = uiState.isInitialized,
                    ) {
                        Column(modifier = Modifier.fillMaxSize()) {
                            if (isTopicCtaVisible) {
                                SopletterTopicCta(
                                    text = "이번 기수 회고하러 가볼까요?",
                                    onClick = { /* TODO API 연동후 추가 예정 */ },
                                    modifier = Modifier.padding(horizontal = 20.dp, vertical = 10.dp),
                                )
                            }

                            Spacer(modifier = Modifier.weight(86f))

                            EmptySopletterContent(
                                modifier = Modifier.weight(187f),
                            )

                            Spacer(modifier = Modifier.weight(342f))
                        }

                        WriteSopletterFloatingActionButton(
                            onWriteFABClick = onWriteFABClick,
                            modifier = Modifier
                                .align(Alignment.BottomEnd)
                                .padding(end = 20.dp, bottom = 24.dp),
                        )
                    }
                }

                else -> {
                    SopletterPullRefreshContainer(
                        refreshState = refreshState,
                        isRefreshing = uiState.isMessageRefreshing,
                    ) {
                        LazyVerticalStaggeredGrid(
                            modifier = Modifier.fillMaxSize(),
                            state = gridState,
                            columns = StaggeredGridCells.Fixed(2),
                            verticalItemSpacing = (-10).dp,
                        ) {
                            if (isTopicCtaVisible) {
                                item(span = StaggeredGridItemSpan.FullLine) {
                                    SopletterTopicCta(
                                        text = "이번 기수 회고하러 가볼까요?",
                                        onClick = { /* TODO API 연동후 추가 예정 */ },
                                        modifier = Modifier.padding(horizontal = 20.dp, vertical = 10.dp),
                                    )
                                }
                            }

                            itemsIndexed(
                                items = uiState.memoList,
                                key = { _, item -> item.messageId },
                            ) { index, item ->
                                SopletterMemoCard(
                                    memo = item,
                                    onClick = { onMemoClick(item) },
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .offset(x = if (index % 2 == 0) 5.dp else (-5).dp),
                                )
                            }
                        }

                        WriteSopletterFloatingActionButton(
                            onWriteFABClick = onWriteFABClick,
                            modifier = Modifier
                                .align(Alignment.BottomEnd)
                                .padding(end = 20.dp, bottom = 24.dp),
                        )
                    }
                }
            }
        }

        uiState.selectedMemoDetail?.let { memo ->
            SopletterMemoDetailDialog(
                state = memo,
                actions = dialogActions,
            )
        }

        if (uiState.isDeleteDialogVisible) {
            SopletterDeleteDialog(
                onDismiss = dialogActions::onDeleteDialogDismissClick,
                onDeleteClick = dialogActions::onDeleteConfirmClick,
            )
        }

        if (uiState.isShowErrorDialog) {
            NetworkErrorDialog(onConfirm = onErrorConfirm)
        }

        SopletterSnackbarHost(
            snackbarHostState = snackbarHostState,
            modifier = Modifier
                .align(Alignment.TopCenter)
                .padding(top = 16.dp, start = 16.dp, end = 16.dp),
        )
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
private fun SopletterPullRefreshContainer(
    refreshState: PullRefreshState,
    isRefreshing: Boolean,
    isShowIndicator: Boolean = true,
    content: @Composable BoxScope.() -> Unit = {},
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .pullRefresh(refreshState),
    ) {
        content()

        if (isShowIndicator) {
            PullRefreshIndicator(
                refreshing = isRefreshing,
                state = refreshState,
                modifier = Modifier.align(Alignment.TopCenter),
            )
        }
    }
}

@Preview
@Composable
private fun SopletterMainScreenPreview(
    @PreviewParameter(SopletterMainPreviewParameterProvider::class) initialState: SopletterMainUiState,
) {
    SoptTheme {
        var previewState by remember { mutableStateOf(initialState) }
        val previewDialogActions = remember {
            object : SopletterMemoDetailDialogContract.Actions {
                override fun onLikeClick() = Unit

                override fun onEditClick() = Unit

                override fun onEditCancelClick() = Unit

                override fun showMemoLengthWarning() = Unit

                override fun onEditCompleteClick(content: String) = Unit

                override fun onDeleteClick() = Unit

                override fun onDeleteDialogDismissClick() = Unit

                override fun onDeleteConfirmClick() = Unit

                override fun onDismissClick() {
                    previewState = previewState.copy(selectedMemoDetail = null)
                }
            }
        }

        SopletterMainScreen(
            uiState = previewState,
            snackbarHostState = remember { SnackbarHostState() },
            onMemoClick = { _ -> },
            onRefresh = {},
            onLoadMore = {},
            dialogActions = previewDialogActions,
            onBackClick = {},
            onDownloadClick = {},
            onReportClick = {},
            onErrorConfirm = {},
            onWriteFABClick = {},
            onTopicClick = {},
        )
    }
}
