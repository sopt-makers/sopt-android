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
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.graphics.asAndroidBitmap
import androidx.compose.ui.graphics.layer.drawLayer
import androidx.compose.ui.graphics.rememberGraphicsLayer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.flowWithLifecycle
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.sopt.official.common.util.noRippleClickable
import org.sopt.official.designsystem.SoptTheme
import org.sopt.official.domain.sopletter.model.SopletterMessage
import org.sopt.official.feature.sopletter.common.component.SopletterScaffold
import org.sopt.official.feature.sopletter.common.model.SopletterSnackbarVisuals
import org.sopt.official.feature.sopletter.main.component.SopletterMemoCard
import org.sopt.official.feature.sopletter.print.component.SopletterPrintTopBar

@Composable
fun SopletterPrintRoute(
    onBackClick: () -> Unit,
    viewModel: SopletterPrintViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val snackBarHostState = remember { SnackbarHostState() }
    val lifecycleOwner = LocalLifecycleOwner.current
    val context = LocalContext.current

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
            onBitmapCaptured = { bitmap -> viewModel.processSavePdf(context, bitmap) },
            modifier = Modifier.padding(paddingValues)
        )
    }
}

@Composable
private fun SopletterPrintScreen(
    uiState: SopletterPrintUiState,
    onBackClick: () -> Unit,
    onPdfSaveClick: () -> Unit,
    onBitmapCaptured: (Bitmap) -> Unit,
    modifier: Modifier = Modifier,
) {
    val graphicsLayer = rememberGraphicsLayer()
    val coroutineScope = rememberCoroutineScope()
    var isCapturing by remember { mutableStateOf(false) }

    Box(modifier = modifier.fillMaxSize()) {

        if (uiState.isCaptureRequested && uiState.fullMemoList != null) {
            Box(
                modifier = Modifier
                    .offset(x = 10000.dp)
                    .wrapContentSize(unbounded = true)
                    .drawWithCache {
                        onDrawWithContent {
                            graphicsLayer.record { this@onDrawWithContent.drawContent() }
                            drawLayer(graphicsLayer)

                            if (!isCapturing) {
                                isCapturing = true
                                coroutineScope.launch {
                                    try {
                                        delay(500L)
                                        val bitmap = graphicsLayer.toImageBitmap().asAndroidBitmap()
                                        onBitmapCaptured(bitmap)
                                    } catch (e: Exception) {
                                        e.printStackTrace()
                                    } finally {
                                        isCapturing = false
                                    }
                                }
                            }
                        }
                    }
            ) {
                SopletterBoardLayout(
                    generation = uiState.generation,
                    memos = uiState.fullMemoList,
                    scale = 1f
                )
            }
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(color = SoptTheme.colors.background)
        ) {
            SopletterPrintTopBar(onBackClick = onBackClick)
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
                Column(
                    modifier = Modifier
                        .fillMaxHeight()
                        .wrapContentWidth()
                        .verticalScroll(rememberScrollState()),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    SopletterBoardLayout(
                        generation = uiState.generation,
                        memos = uiState.previewMemoList.take(16),
                        scale = 0.45f
                    )
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
                    .noRippleClickable(onClick = onPdfSaveClick)
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
    }
}

@Composable
private fun SopletterBoardLayout(
    generation: Int,
    memos: List<SopletterMessage>,
    scale: Float,
    modifier: Modifier = Modifier
) {
    val leftColMemos = memos.filterIndexed { index, _ -> index % 2 == 0 }
    val rightColMemos = memos.filterIndexed { index, _ -> index % 2 == 1 }

    Column(modifier = modifier.wrapContentWidth().background(SoptTheme.colors.background)) {
        Text(
            text = "${generation}기 솝레터",
            style = SoptTheme.typography.title16SB.copy(fontSize = (16 * scale).sp),
            color = SoptTheme.colors.onSurface100,
            modifier = Modifier.align(Alignment.Start).padding(bottom = (16 * scale).dp, start = 12.dp)
        )

        Row(
            horizontalArrangement = Arrangement.spacedBy((8 * scale).dp),
            verticalAlignment = Alignment.Top,
            modifier = Modifier.wrapContentWidth()
        ) {
            Column(
                modifier = Modifier.wrapContentWidth(),
                verticalArrangement = Arrangement.spacedBy((-8 * scale).dp)
            ) {
                leftColMemos.forEach { item -> SopletterMemoCard(memo = item, onClick = {}, scale = scale) }
            }
            Column(
                modifier = Modifier.wrapContentWidth().padding(top = (20 * scale).dp),
                verticalArrangement = Arrangement.spacedBy((-8 * scale).dp)
            ) {
                rightColMemos.forEach { item -> SopletterMemoCard(memo = item, onClick = {}, scale = scale) }
            }
        }
    }
}

@Composable
@Preview(showBackground = true)
private fun PreviewSopletterPrintScreen(){
    SoptTheme {
        SopletterPrintScreen(
            uiState = SopletterPrintUiState(),
            onBackClick = {},
            onPdfSaveClick = {},
            onBitmapCaptured = {}
        )
    }
}