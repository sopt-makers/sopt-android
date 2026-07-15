package org.sopt.official.feature.sopletter.print

import android.graphics.Bitmap
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asAndroidBitmap
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.layer.drawLayer
import androidx.compose.ui.graphics.rememberGraphicsLayer
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.repeatOnLifecycle
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.delay
import org.sopt.official.common.util.noRippleClickable
import org.sopt.official.designsystem.SoptTheme
import org.sopt.official.domain.sopletter.model.SopletterMessage
import org.sopt.official.feature.sopletter.common.component.SopletterTopbar
import org.sopt.official.feature.sopletter.common.model.SopletterSnackbarVisuals
import org.sopt.official.feature.sopletter.main.component.SopletterMemoCard
import org.sopt.official.feature.sopletter.print.model.SopletterPrintSideEffect
import org.sopt.official.feature.sopletter.print.model.SopletterPrintUiState
import timber.log.Timber

@Composable
fun SopletterPrintRoute(
    onShowSnackbar: (SopletterSnackbarVisuals) -> Unit,
    onBackClick: () -> Unit,
    viewModel: SopletterPrintViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val lifecycleOwner = LocalLifecycleOwner.current
    val applicationContext = LocalContext.current.applicationContext

    val chunks = remember(uiState.fullMemoList) { uiState.fullMemoList?.chunked(16) ?: emptyList() }
    var currentChunkIndex by remember { mutableIntStateOf(0) }
    var isCaptureFinished by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        lifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
            viewModel.sideEffect.collect { sideEffect ->
                when (sideEffect) {
                    is SopletterPrintSideEffect.ShowSnackbar -> {
                        onShowSnackbar(SopletterSnackbarVisuals(sideEffect.message, sideEffect.type))
                    }

                    is SopletterPrintSideEffect.NavigateBack -> onBackClick()
                }
            }
        }
    }

    LaunchedEffect(uiState.isCaptureRequested) {
        if (uiState.isCaptureRequested) {
            currentChunkIndex = 0
            isCaptureFinished = false
        }
    }

    LaunchedEffect(uiState.isCaptureRequested, chunks) {
        if (uiState.isCaptureRequested && chunks.isEmpty()) {
            viewModel.onCaptureFailed("저장할 데이터가 없습니다.")
        }
    }

    SopletterPrintScreen(
        uiState = uiState,
        currentChunkMemos = chunks.getOrNull(currentChunkIndex),
        currentChunkIndex = currentChunkIndex,
        isCaptureFinished = isCaptureFinished,
        onBackClick = onBackClick,
        onPdfSaveClick = viewModel::fetchAllAndTriggerCapture,
        onChunkCaptured = { bitmap ->
            viewModel.addPageToPdf(bitmap)
            if (currentChunkIndex < chunks.size - 1) {
                currentChunkIndex++
            } else {
                isCaptureFinished = true
                viewModel.processSavePdf(applicationContext)
            }
        },
        onCaptureFailed = { e ->
            if (e != null) {
                Timber.e(e, "타겟 보드 이미지 캡처 실패")
            }
            isCaptureFinished = true
            viewModel.onCaptureFailed()
        },
    )
}

@Composable
private fun SopletterPrintScreen(
    uiState: SopletterPrintUiState,
    currentChunkMemos: List<SopletterMessage>?,
    currentChunkIndex: Int,
    isCaptureFinished: Boolean,
    onBackClick: () -> Unit,
    onPdfSaveClick: () -> Unit,
    onChunkCaptured: (Bitmap) -> Unit,
    onCaptureFailed: (Throwable?) -> Unit,
    modifier: Modifier = Modifier,
) {
    val isPreviewLoading = uiState.previewMemoList.isEmpty()

    Box(modifier = modifier.fillMaxSize()) {

        if (uiState.isCaptureRequested && currentChunkMemos != null && !isCaptureFinished) {
            key(currentChunkMemos) {
                SopletterCaptureTarget(
                    requestKey = currentChunkIndex,
                    title = uiState.topicTitle,
                    memos = currentChunkMemos,
                    onCaptured = onChunkCaptured,
                    onCaptureFailed = onCaptureFailed
                )
            }
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(color = SoptTheme.colors.background)
                .zIndex(1f)
        ) {
            SopletterTopbar(
                onBackClick = { if (!uiState.isSaving) onBackClick() },
                topbarTitle = "솝레터 출력"
            )

            Spacer(modifier = Modifier.height(24.dp))

            Box(
                modifier = Modifier
                    .weight(1.5f)
                    .wrapContentWidth()
                    .align(Alignment.CenterHorizontally),
                contentAlignment = Alignment.TopCenter
            ) {
                if (isPreviewLoading) {
                    Box(
                        modifier = Modifier
                            .fillMaxHeight()
                            .fillMaxWidth()
                            .border(1.dp, SoptTheme.colors.onSurface100, RoundedCornerShape(10.dp))
                            .padding(8.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator(color = SoptTheme.colors.primary)
                    }
                } else {
                    Column(
                        modifier = Modifier
                            .wrapContentHeight()
                            .wrapContentWidth()
                            .border(1.dp, SoptTheme.colors.onSurface100, RoundedCornerShape(10.dp))
                            .padding(8.dp)
                            .verticalScroll(rememberScrollState()),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        ScaledSopletterBoard(scale = 0.55f) {
                            SopletterBoardLayout(
                                title = uiState.topicTitle,
                                memos = uiState.previewMemoList.take(16),
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "미리보기에서는 최대 16개까지 확인할 수 있어요.",
                style = SoptTheme.typography.body14M,
                color = SoptTheme.colors.onSurface300,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(16.dp))

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 24.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .background(SoptTheme.colors.onSurface600)
                    .noRippleClickable {
                        if (!uiState.isSaving) onPdfSaveClick()
                    }
                    .padding(vertical = 16.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "PDF 저장하기",
                    style = SoptTheme.typography.title16SB,
                    color = SoptTheme.colors.onSurface30
                )
            }
        }

        if (uiState.isSaving) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.3f))
                    .zIndex(10f),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(color = SoptTheme.colors.primary)
            }
        }

    }
}

@Composable
private fun SopletterCaptureTarget(
    requestKey: Any,
    title: String,
    memos: List<SopletterMessage>,
    onCaptured: (Bitmap) -> Unit,
    onCaptureFailed: (Throwable) -> Unit,
) {
    val graphicsLayer = rememberGraphicsLayer()
    val drawCompleted = remember(requestKey) { CompletableDeferred<Unit>() }

    Box(
        modifier = Modifier
            .wrapContentSize(unbounded = true)
            .zIndex(5f)
            .graphicsLayer { alpha = 0.01f }
            .drawWithContent {
                graphicsLayer.record {
                    this@drawWithContent.drawContent()
                }
                drawLayer(graphicsLayer)
                drawCompleted.complete(Unit)
            }
    ) {
        SopletterBoardLayout(
            title = title,
            memos = memos,
        )
    }

    LaunchedEffect(requestKey) {
        try {
            drawCompleted.await()
            delay(50L)
            val bitmap = graphicsLayer.toImageBitmap().asAndroidBitmap()
            onCaptured(bitmap)
        } catch (e: CancellationException) {
            throw e
        } catch (e: Throwable) {
            onCaptureFailed(e)
        }
    }
}

@Composable
private fun SopletterBoardLayout(
    title: String,
    memos: List<SopletterMessage>,
    modifier: Modifier = Modifier,
    memoCardWidth: Dp = 140.dp,
    memoColumnSpacing: Dp = 8.dp,
) {
    val density = LocalDensity.current
    val leftColMemos = memos.filterIndexed { index, _ -> index % 2 == 0 }
    val rightColMemos = memos.filterIndexed { index, _ -> index % 2 == 1 }

    Column(
        modifier = modifier
            .wrapContentSize(unbounded = true)
            .background(SoptTheme.colors.background)
    ) {
        Text(
            text = title,
            style = SoptTheme.typography.title16SB,
            color = SoptTheme.colors.onSurface100,
            modifier = Modifier
                .align(Alignment.Start)
                .padding(bottom = 16.dp, start = 12.dp)
        )

        CompositionLocalProvider(
            LocalDensity provides Density(
                density = density.density,
                fontScale = density.fontScale * 0.85f
            )
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(memoColumnSpacing),
                verticalAlignment = Alignment.Top,
                modifier = Modifier.wrapContentWidth()
            ) {
                Column(
                    modifier = Modifier.width(memoCardWidth),
                    verticalArrangement = Arrangement.spacedBy((-8).dp)
                ) {
                    leftColMemos.forEach { item ->
                        SopletterMemoCard(memo = item, onClick = {}, textPadding = PaddingValues(horizontal = 12.dp, vertical = 16.dp))
                    }
                }
                Column(
                    modifier = Modifier
                        .width(memoCardWidth)
                        .padding(top = 20.dp),
                    verticalArrangement = Arrangement.spacedBy((-8).dp)
                ) {
                    rightColMemos.forEach { item ->
                        SopletterMemoCard(memo = item, onClick = {}, textPadding = PaddingValues(horizontal = 12.dp, vertical = 16.dp))
                    }
                }
            }
        }
    }
}

@Composable
private fun ScaledSopletterBoard(
    scale: Float,
    content: @Composable () -> Unit,
) {
    if (scale == 1f) {
        content()
        return
    }

    Layout(content = content) { measurables, constraints ->
        val placeable = measurables.first().measure(constraints)
        val scaledWidth = (placeable.width * scale).toInt()
        val scaledHeight = (placeable.height * scale).toInt()

        layout(scaledWidth, scaledHeight) {
            val xOffset = (scaledWidth - placeable.width) / 2
            val yOffset = (scaledHeight - placeable.height) / 2

            placeable.placeWithLayer(x = xOffset, y = yOffset) {
                scaleX = scale
                scaleY = scale
            }
        }
    }
}

@Composable
@Preview(showBackground = true)
private fun PreviewSopletterPrintScreen() {
    SoptTheme {
        SopletterPrintRoute(
            onShowSnackbar = {},
            onBackClick = {}
        )
    }
}