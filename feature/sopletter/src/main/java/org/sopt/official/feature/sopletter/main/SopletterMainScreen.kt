package org.sopt.official.feature.sopletter.main

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.itemsIndexed
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.collections.immutable.persistentListOf
import org.sopt.official.designsystem.SoptTheme
import org.sopt.official.feature.sopletter.main.component.EditSopletterFloatingActionButton
import org.sopt.official.feature.sopletter.main.component.EmptySopletterContent
import org.sopt.official.feature.sopletter.main.component.SopletterMainTopBar
import org.sopt.official.feature.sopletter.main.component.SopletterMemoCard
import org.sopt.official.feature.sopletter.main.model.SopletterMainUiState
import org.sopt.official.feature.sopletter.main.model.SopletterMemoColor
import org.sopt.official.feature.sopletter.main.model.SopletterMemoRotationType
import org.sopt.official.feature.sopletter.main.model.SopletterMemoUiModel
import org.sopt.official.sopletter.R

@Composable
fun SopletterMainRoute(
    viewModel: SopletterMainViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    SopletterMainScreen(
        uiState = uiState,
        onCloseClick = { /* TODO close click */ },
        onDownloadClick = { /* TODO download click */ },
        onReportClick = { /* TODO report click */ },
        onEditFABClick = { /* TODO edit FAB click */ },
    )
}

@Composable
private fun SopletterMainScreen(
    uiState: SopletterMainUiState,
    onCloseClick: () -> Unit,
    onDownloadClick: () -> Unit,
    onReportClick: () -> Unit,
    onEditFABClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(color = SoptTheme.colors.background),
    ) {
        SopletterMainTopBar(
            generation = uiState.generation,
            isDownloadBtnVisible = uiState.memoList.isNotEmpty(),
            onCloseClick = onCloseClick,
            onDownloadClick = onDownloadClick,
            onReportClick = onReportClick,
        )

        if (uiState.memoList.isEmpty()) {
            Column(modifier = Modifier.fillMaxSize()) {
                Spacer(modifier = Modifier.weight(160f))

                EmptySopletterContent(modifier = Modifier.weight(187f))

                Spacer(modifier = Modifier.weight(343f))
            }
        } else {
            Box(
                modifier = Modifier
                    .fillMaxSize(),
            ) {
                LazyVerticalStaggeredGrid(
                    modifier = Modifier.fillMaxSize(),
                    columns = StaggeredGridCells.Fixed(2),
                    verticalItemSpacing = (-10).dp,
                ) {
                    itemsIndexed(
                        items = uiState.memoList,
                        key = { _, item -> item.id },
                    ) { index, item ->
                        SopletterMemoCard(
                            memo = item,
                            modifier = Modifier
                                .fillMaxWidth()
                                .offset(x = if (index % 2 == 0) 5.dp else (-5).dp),
                        )
                    }
                }

                EditSopletterFloatingActionButton(
                    onEditFABClick = onEditFABClick,
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .padding(end = 20.dp, bottom = 66.dp),
                )
            }
        }
    }
}

@Preview
@Composable
private fun SopletterMainScreenPreview() {
    SoptTheme {
        val memoList = persistentListOf(
            SopletterMemoUiModel(
                id = 1L,
                message = "안녕하세요안녕하세요안녕하세요안녕하세요안녕하세요녕하세안녕하세요녕하세하세안녕하세요녕하세",
                shapeImageRes = R.drawable.ic_sopletter_memo_cloud,
                rotation = SopletterMemoRotationType.LEFT,
                memoColor = SopletterMemoColor.BLUE,
            ),
            SopletterMemoUiModel(
                id = 2L,
                message = "안녕하세요안녕하세요안녕하세요안녕하세요안녕하세요녕하세안녕하세요녕하세하세안녕하세요녕하세",
                shapeImageRes = R.drawable.ic_sopletter_memo_sharp,
                rotation = SopletterMemoRotationType.RIGHT,
                memoColor = SopletterMemoColor.MINT,
            ),
            SopletterMemoUiModel(
                id = 3L,
                message = "안녕하세요안녕하세요안녕하세요안녕하세요안녕하세요녕하세안녕하세요녕하세하세안녕하세요녕하세",
                shapeImageRes = R.drawable.ic_sopletter_memo_smooth,
                rotation = SopletterMemoRotationType.CENTER,
                memoColor = SopletterMemoColor.PINK,
            ),
            SopletterMemoUiModel(
                id = 4L,
                message = "안녕하세요안녕하세요안녕하세요안녕하세요안녕하세요녕하세안녕하세요녕하세하세안녕하세요녕하세",
                shapeImageRes = R.drawable.ic_sopletter_memo_point,
                rotation = SopletterMemoRotationType.LEFT,
                memoColor = SopletterMemoColor.YELLOW,
            ),
        )

        SopletterMainScreen(
            uiState = SopletterMainUiState(
                generation = 38,
                memoList = memoList,
            ),
            onCloseClick = { /* TODO close click */ },
            onDownloadClick = { /* TODO download click */ },
            onReportClick = { /* TODO report click */ },
            onEditFABClick = { /* TODO edit FAB click */ },
        )
    }
}
