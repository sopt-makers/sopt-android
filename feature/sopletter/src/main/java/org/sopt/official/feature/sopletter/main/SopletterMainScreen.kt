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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.sopt.official.designsystem.SoptTheme
import org.sopt.official.feature.sopletter.main.contract.SopletterMemoDetailDialogContract
import org.sopt.official.feature.sopletter.main.component.EditSopletterFloatingActionButton
import org.sopt.official.feature.sopletter.main.component.EmptySopletterContent
import org.sopt.official.feature.sopletter.main.component.SopletterMainTopBar
import org.sopt.official.feature.sopletter.main.component.SopletterMemoCard
import org.sopt.official.feature.sopletter.main.component.SopletterMemoDetailDialog
import org.sopt.official.feature.sopletter.main.mapper.toDetailDialogState
import org.sopt.official.feature.sopletter.main.model.SopletterMainUiState
import org.sopt.official.feature.sopletter.main.model.SopletterMemoUiModel
import org.sopt.official.feature.sopletter.main.preview.SopletterMainPreviewParameterProvider

@Composable
fun SopletterMainRoute(
    viewModel: SopletterMainViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    SopletterMainScreen(
        uiState = uiState,
        onMemoClick = { viewModel.updateSelectMemoDetail(it.toDetailDialogState()) },
        dialogActions = viewModel,
        onCloseClick = { /* TODO close click */ },
        onDownloadClick = { /* TODO download click */ },
        onReportClick = { /* TODO report click */ },
        onEditFABClick = { /* TODO edit FAB click */ },
    )
}

@Composable
private fun SopletterMainScreen(
    uiState: SopletterMainUiState,
    onMemoClick: (SopletterMemoUiModel) -> Unit,
    dialogActions: SopletterMemoDetailDialogContract.Actions,
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
                            onClick = { onMemoClick(item) },
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
                        .padding(end = 20.dp, bottom = 24.dp),
                )
            }
        }
    }

    uiState.selectedMemoDetail?.let { memo ->
        SopletterMemoDetailDialog(
            state = memo,
            actions = dialogActions,
        )
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

                override fun onDeleteClick() = Unit

                override fun onDismissClick() {
                    previewState = previewState.copy(selectedMemoDetail = null)
                }
            }
        }

        SopletterMainScreen(
            uiState = previewState,
            onMemoClick = { memo ->
                previewState = previewState.copy(selectedMemoDetail = memo.toDetailDialogState())
            },
            dialogActions = previewDialogActions,
            onCloseClick = { /* TODO close click */ },
            onDownloadClick = { /* TODO download click */ },
            onReportClick = { /* TODO report click */ },
            onEditFABClick = { /* TODO edit FAB click */ },
        )
    }
}
