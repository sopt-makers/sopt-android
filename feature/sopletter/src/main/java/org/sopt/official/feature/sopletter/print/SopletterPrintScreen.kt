package org.sopt.official.feature.sopletter.print

import android.graphics.Bitmap
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asAndroidBitmap
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.layer.drawLayer
import androidx.compose.ui.graphics.rememberGraphicsLayer
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.flowWithLifecycle
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.delay
import org.sopt.official.common.util.noRippleClickable
import org.sopt.official.designsystem.SoptTheme
import org.sopt.official.domain.sopletter.model.SopletterMessage
import org.sopt.official.feature.sopletter.common.component.SopletterScaffold
import org.sopt.official.feature.sopletter.common.component.SopletterTopbar
import org.sopt.official.feature.sopletter.common.model.SopletterSnackbarVisuals
import org.sopt.official.feature.sopletter.main.component.SopletterMemoCard
import org.sopt.official.feature.sopletter.print.model.SopletterPrintSideEffect
import org.sopt.official.feature.sopletter.print.model.SopletterPrintUiState

@Composable
fun SopletterPrintRoute(
    onBackClick: () -> Unit,
    viewModel: SopletterPrintViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val snackBarHostState = remember { SnackbarHostState() }
    val lifecycleOwner = LocalLifecycleOwner.current
    val applicationContext = LocalContext.current.applicationContext

    LaunchedEffect(Unit) {
        viewModel.sideEffect.flowWithLifecycle(lifecycleOwner.lifecycle)
            .collect { sideEffect ->
                when (sideEffect) {
                    is SopletterPrintSideEffect.ShowSnackbar -> {
                        snackBarHostState.showSnackbar(
                            SopletterSnackbarVisuals(
                                message = sideEffect.message,
                                type = sideEffect.type
                            )
                        )
                    }
                    is SopletterPrintSideEffect.NavigateBack -> onBackClick()
                }
            }
    }

    SopletterScaffold(snackbarHostState = snackBarHostState) { paddingValues ->
        SopletterPrintScreen(
            uiState = uiState,
            onBackClick = onBackClick,
            onPdfSaveClick = viewModel::fetchAllAndTriggerCapture,
            onBitmapsCaptured = { bitmaps -> viewModel.processSavePdf(applicationContext, bitmaps) },
            onCaptureFailed = viewModel::onCaptureFailed,
            modifier = Modifier.padding(paddingValues)
        )
    }
}

@Composable
private fun SopletterPrintScreen(
    uiState: SopletterPrintUiState,
    onBackClick: () -> Unit,
    onPdfSaveClick: () -> Unit,
    onBitmapsCaptured: (List<Bitmap>) -> Unit,
    onCaptureFailed: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val graphicsLayer = rememberGraphicsLayer()

    val chunks = remember(uiState.fullMemoList) { uiState.fullMemoList?.chunked(16) ?: emptyList() }
    var currentChunkIndex by remember { mutableIntStateOf(0) }
    val capturedBitmaps = remember { mutableStateListOf<Bitmap>() }
    var isViewReady by remember { mutableStateOf(false) }
    var isCapturing by remember { mutableStateOf(false) }

    val isPreviewLoading = uiState.previewMemoList.isEmpty()

    Box(modifier = modifier.fillMaxSize()) {

        if (uiState.isCaptureRequested && chunks.isNotEmpty()) {
            val currentMemos = chunks.getOrNull(currentChunkIndex)
            if (currentMemos != null) {
                Box(
                    modifier = Modifier
                        .wrapContentSize(unbounded = true)
                        .zIndex(-1f)
                        .graphicsLayer { alpha = 0.01f }
                        .drawWithCache {
                            onDrawWithContent {
                                graphicsLayer.record { this@onDrawWithContent.drawContent() }
                                drawLayer(graphicsLayer)
                            }
                        }
                        .onGloballyPositioned { isViewReady = true }
                ) {
                    SopletterBoardLayout(
                        title = uiState.topicTitle,
                        memos = currentMemos,
                    )
                }

                LaunchedEffect(currentChunkIndex, isViewReady) {
                    if (isViewReady && !isCapturing) {
                        isCapturing = true
                        try {
                            delay(300L)
                            val bitmap = graphicsLayer.toImageBitmap().asAndroidBitmap()
                            capturedBitmaps.add(bitmap)

                            if (currentChunkIndex < chunks.size - 1) {
                                isViewReady = false
                                isCapturing = false
                                currentChunkIndex++
                            } else {
                                onBitmapsCaptured(capturedBitmaps.toList())
                                capturedBitmaps.clear()
                                currentChunkIndex = 0
                                isViewReady = false
                                isCapturing = false
                            }
                        } catch (e: CancellationException) {
                            isCapturing = false
                            throw e
                        } catch (e: Exception) {
                            e.printStackTrace()
                            onCaptureFailed()
                            capturedBitmaps.clear()
                            currentChunkIndex = 0
                            isViewReady = false
                            isCapturing = false
                        }
                    }
                }
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

            Spacer(modifier = Modifier.height(16.dp))

            Box(
                modifier = Modifier
                    .weight(1.5f)
                    .wrapContentWidth()
                    .align(Alignment.CenterHorizontally)
                    .border(1.dp, SoptTheme.colors.onSurface100, RoundedCornerShape(10.dp))
                    .padding(8.dp),
                contentAlignment = Alignment.TopCenter
            ) {
                if (isPreviewLoading) {
                    Box(
                        modifier = Modifier
                            .fillMaxHeight()
                            .fillMaxWidth(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator(color = SoptTheme.colors.primary)
                    }
                } else {
                    Column(
                        modifier = Modifier
                            .fillMaxHeight()
                            .wrapContentWidth()
                            .verticalScroll(rememberScrollState()),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        ScaledSopletterBoard(scale = 0.45f) {
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
private fun SopletterBoardLayout(
    title: String,
    memos: List<SopletterMessage>,
    modifier: Modifier = Modifier,
) {
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
            modifier = Modifier.align(Alignment.Start).padding(bottom = 16.dp, start = 12.dp)
        )

        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.Top,
            modifier = Modifier.wrapContentWidth()
        ) {
            Column(
                modifier = Modifier.wrapContentWidth(),
                verticalArrangement = Arrangement.spacedBy((-8).dp)
            ) {
                leftColMemos.forEach { item ->
                    SopletterMemoCard(
                        memo = item,
                        onClick = {},
                    )
                }
            }
            Column(
                modifier = Modifier.wrapContentWidth().padding(top = 20.dp),
                verticalArrangement = Arrangement.spacedBy((-8).dp)
            ) {
                rightColMemos.forEach { item ->
                    SopletterMemoCard(
                        memo = item,
                        onClick = {},
                    )
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
            onBackClick = {}
        )
    }
}
