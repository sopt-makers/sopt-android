package org.sopt.official.feature.notice

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Divider
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.PagerState
import com.google.accompanist.pager.rememberPagerState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import org.sopt.official.R
import org.sopt.official.designsystem.components.SoptIconButton
import org.sopt.official.designsystem.components.SoptTopAppBar
import org.sopt.official.designsystem.style.Gray800
import org.sopt.official.designsystem.style.SoptTheme

@OptIn(ExperimentalPagerApi::class)
@Composable
fun ImageDetailScreen(
    title: String,
    images: List<String>
) {
    Scaffold(
        topBar = {
            ImageDetailViewerHeader(title = title)
        },
        backgroundColor = Color.Black
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            Divider(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(1.dp),
                color = Gray800
            )
            ImageDetailViewer(images = images)
        }
    }
}

@Composable
fun ImageDetailViewerHeader(
    title: String,
    onClickBack: () -> Unit = {},
    onClickDownload: () -> Unit = {}
) {
    SoptTopAppBar(
        title = {
            Text(
                text = title,
                style = SoptTheme.typography.sub2,
                color = Color.White,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center
            )
        },
        navigationIcon = {
            SoptIconButton(
                imageVector = ImageVector.vectorResource(id = R.drawable.ic_back),
                contentDescription = "Back Button of Image Detail Viewer Screen",
                tint = Color.White,
                onClick = onClickBack
            )
        },
        actions = {
            SoptIconButton(
                imageVector = ImageVector.vectorResource(id = R.drawable.ic_download),
                contentDescription = "DownloadButton of Image Detail Viewer Screen",
                tint = Color.White,
                onClick = onClickDownload
            )
        }
    )
}

@OptIn(ExperimentalPagerApi::class)
@Composable
fun ImageDetailViewer(images: List<String>) {
    Box {
        val imagePage = rememberPagerState(0)
        HorizontalPager(count = images.size, state = imagePage) { page ->
            AsyncImage(
                model = images[page],
                contentDescription = "Image Detail View",
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.FillWidth,
                placeholder = painterResource(id = R.drawable.image_placeholder)
            )
        }
        ImageDetailIndicator(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(16.dp),
            totalImageCount = images.size,
            state = imagePage
        )
    }
}

@OptIn(ExperimentalPagerApi::class)
@Composable
fun ImageDetailIndicator(
    modifier: Modifier,
    totalImageCount: Int,
    state: PagerState
) {
    val pageUpdateScope = rememberCoroutineScope()
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        ImageViewerLeftButton(
            state = state,
            pageUpdateScope = pageUpdateScope
        )
        Text(
            text = "${state.currentPage + 1}/$totalImageCount",
            style = SoptTheme.typography.sub2,
            color = SoptTheme.colors.onSurface40
        )
        ImageViewerRightButton(
            totalImageCount = totalImageCount,
            state = state,
            pageUpdateScope = pageUpdateScope
        )
    }
}

@OptIn(ExperimentalPagerApi::class)
@Composable
fun ImageViewerLeftButton(
    state: PagerState,
    pageUpdateScope: CoroutineScope
) {
    SoptIconButton(
        imageVector = ImageVector.vectorResource(id = R.drawable.ic_left),
        tint = Color.White,
        onClick = {
            decreaseImagePage(
                state = state,
                scope = pageUpdateScope
            )
        }
    )
}

@OptIn(ExperimentalPagerApi::class)
private fun decreaseImagePage(
    state: PagerState,
    scope: CoroutineScope
) {
    if (state.currentPage == 0) return
    scope.launch {
        state.animateScrollToPage(page = state.currentPage - 1)
    }
}

@OptIn(ExperimentalPagerApi::class)
@Composable
fun ImageViewerRightButton(
    totalImageCount: Int,
    state: PagerState,
    pageUpdateScope: CoroutineScope
) {
    SoptIconButton(
        imageVector = ImageVector.vectorResource(id = R.drawable.ic_right),
        tint = Color.White,
        onClick = {
            increaseImagePage(
                totalImageCount = totalImageCount,
                state = state,
                scope = pageUpdateScope
            )
        }
    )
}

@OptIn(ExperimentalPagerApi::class)
private fun increaseImagePage(
    totalImageCount: Int,
    state: PagerState,
    scope: CoroutineScope
) {
    if (state.currentPage == totalImageCount - 1) return
    scope.launch {
        state.animateScrollToPage(page = state.currentPage + 1)
    }
}

@Preview(
    showBackground = true,
    backgroundColor = 0xFF000000
)
@Composable
fun PreviewImageDetailScreen() {
    SoptTheme {
        ImageDetailScreen(
            title = "31th SOPT OT 공지",
            images = listOf(
                "https://miro.medium.com/max/786/1*xvkQR4i-c5suBzk6g64gHg.png"
            )
        )
    }
}
