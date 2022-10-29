package org.sopt.official.feature.notice

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Divider
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import org.sopt.official.R
import org.sopt.official.designsystem.components.SoptTopAppBar
import org.sopt.official.designsystem.components.SoptIconButton
import org.sopt.official.designsystem.style.Gray200
import org.sopt.official.designsystem.style.Gray800
import org.sopt.official.designsystem.style.SoptTheme
import org.sopt.official.feature.notice.components.NoticeInformationText
import org.sopt.official.feature.notice.model.NoticeDetailModel
import org.sopt.official.utils.noRippleClickable

private val noticeDetailHorizontalPadding = 16.dp
private val noticeDetailVerticalPadding = 0.dp

@Composable
fun NoticeDetailScreen(
    noticeDetailModel: NoticeDetailModel
) {
    Scaffold(
        topBar = {
            SoptTopAppBar(
                title = {
                    Text(
                        text = noticeDetailModel.part,
                        style = SoptTheme.typography.h5,
                        color = SoptTheme.colors.onSurface90
                    )
                },
                navigationIcon = {
                    SoptIconButton(
                        imageVector = ImageVector.vectorResource(id = R.drawable.ic_back)
                    )
                }
            )
        }
    ) { paddingValues ->
        val scrollState = rememberScrollState()
        Column(
            modifier = Modifier.fillMaxSize()
                .padding(
                    start = noticeDetailHorizontalPadding,
                    top = noticeDetailVerticalPadding,
                    end = noticeDetailHorizontalPadding,
                    bottom = paddingValues.calculateBottomPadding()
                ).verticalScroll(scrollState)
        ) {
            NoticeDetailTopDivider()
            NoticeDetailHeader(
                title = noticeDetailModel.title,
                creator = noticeDetailModel.creator,
                createdAt = noticeDetailModel.createdAt
            )
            Spacer(modifier = Modifier.size(12.dp))
            Divider(
                modifier = Modifier.fillMaxWidth()
                    .height(1.dp),
                color = Gray200
            )
            Spacer(modifier = Modifier.size(16.dp))
            ImageBox(previewImageUrl = noticeDetailModel.images)
            Spacer(modifier = Modifier.size(24.dp))
            Text(
                modifier = Modifier.fillMaxWidth(),
                text = noticeDetailModel.content,
                style = SoptTheme.typography.b2,
                color = SoptTheme.colors.onSurface90
            )
        }
    }
}

@Composable
fun NoticeDetailTopDivider() {
    Column {
        Spacer(modifier = Modifier.size(8.dp))
        Divider(
            modifier = Modifier.fillMaxWidth()
                .height(1.dp),
            color = Gray800
        )
        Spacer(modifier = Modifier.size(16.dp))
    }
}

@Composable
fun NoticeDetailHeader(
    title: String,
    creator: String,
    createdAt: String
) {
    Column {
        Text(
            text = title,
            style = SoptTheme.typography.h4,
            color = SoptTheme.colors.onSurface90
        )
        Spacer(modifier = Modifier.size(8.dp))
        NoticeInformationText(
            creator = creator,
            createdAt = createdAt
        )
    }
}

@Composable
fun ImageBox(
    previewImageUrl: List<String>,
    onClick: () -> Unit = {}
) {
    Box(
        modifier = Modifier.fillMaxWidth()
            .aspectRatio(1f)
            .noRippleClickable { onClick() }
    ) {
        AsyncImage(
            model = previewImageUrl[0],
            contentDescription = "Preview Image of notice detail Screen",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop,
            placeholder = painterResource(id = R.drawable.image_placeholder)
        )
        Column(
            modifier = Modifier.background(Color(0xFF8B8B8B))
                .align(Alignment.BottomEnd)
        ) {
            Text(
                modifier = Modifier.padding(start = 13.dp, top = 7.dp, end = 13.dp, bottom = 5.dp),
                text = "+ ${previewImageUrl.size}",
                style = SoptTheme.typography.sub2,
                color = Color.White
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewNoticeDetailScreen() {
    val noticeDetailModel = NoticeDetailModel(
        id = 1,
        title = "공지공지공지공지공지공지공지공지공지공지공지공지공지공지공지공지",
        creator = "관리자",
        createdAt = "22.09.27",
        images = listOf("https://miro.medium.com/max/786/1*xvkQR4i-c5suBzk6g64gHg.png", "https://dsfkjslfskfslls"),
        content = "안녕하세요!",
        part = "전체",
        scope = "ALL"
    )
    SoptTheme {
        NoticeDetailScreen(noticeDetailModel)
    }
}
