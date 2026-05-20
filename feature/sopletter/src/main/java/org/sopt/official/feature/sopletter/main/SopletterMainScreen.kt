package org.sopt.official.feature.sopletter.main

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.itemsIndexed
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.collections.immutable.persistentListOf
import org.sopt.official.designsystem.SoptTheme
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
    )
}

@Composable
private fun SopletterMainScreen(
    uiState: SopletterMainUiState,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(color = SoptTheme.colors.background),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    horizontal = 20.dp,
                    vertical = 12.dp,
                ),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                Icon(
                    imageVector = ImageVector.vectorResource(R.drawable.ic_close_32),
                    contentDescription = null,
                    tint = Color.Unspecified,
                )

                Text(
                    text = "${uiState.generation}기 솝레터",
                    style = SoptTheme.typography.heading18B,
                    color = SoptTheme.colors.onSurface10,
                )
            }

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                Icon(
                    imageVector = ImageVector.vectorResource(R.drawable.ic_download_32),
                    contentDescription = null,
                    tint = Color.Unspecified,
                )

                Icon(
                    imageVector = ImageVector.vectorResource(R.drawable.ic_alert_32),
                    contentDescription = null,
                    tint = Color.Unspecified,
                )
            }
        }

        if (uiState.memoList.isEmpty()) {
            Spacer(modifier = Modifier.weight(160f))

            EmptySopletterContent(modifier = Modifier.weight(187f))

            Spacer(modifier = Modifier.weight(343f))
        } else {
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
        }
    }
}

@Composable
private fun EmptySopletterContent(
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(17.dp),
    ) {
        Image(
            painter = painterResource(id = R.drawable.img_sopletter_empty),
            contentDescription = null,
        )

        Text(
            text = "작성된 솝레터가 없어요.\n우리 기수 첫 번쨰 솝레터의 주인공은?",
            style = SoptTheme.typography.body18M,
            color = SoptTheme.colors.onSurface200,
            textAlign = TextAlign.Center,
        )
    }
}

@Composable
private fun SopletterMemoCard(
    memo: SopletterMemoUiModel,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .rotate(memo.rotation.degree),
        contentAlignment = Alignment.Center,
    ) {
        Image(
            imageVector = ImageVector.vectorResource(memo.shapeImageRes),
            contentDescription = null,
            colorFilter = ColorFilter.tint(memo.memoColor.color),
        )

        Text(
            text = memo.message,
            modifier = Modifier
                .width(111.dp)
                .height(110.dp),
            style = SoptTheme.typography.body14M,
            color = SoptTheme.colors.onSurface800,
            textAlign = TextAlign.Center,
            overflow = TextOverflow.Ellipsis,
        )
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
            )
        )
    }
}
