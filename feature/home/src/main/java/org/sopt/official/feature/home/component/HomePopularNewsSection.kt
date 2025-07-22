package org.sopt.official.feature.home.component

import androidx.compose.animation.core.EaseIn
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.coroutines.delay
import org.sopt.official.designsystem.Orange200
import org.sopt.official.designsystem.Orange300
import org.sopt.official.designsystem.Orange500
import org.sopt.official.designsystem.SoptTheme
import org.sopt.official.feature.home.model.HomePlaygroundPostModel

internal val feedList = persistentListOf(
    HomePlaygroundPostModel(
        postId = 1,
        title = "나 메이커스팀인데 메팀 좋다",
        content = "본문 내용은 두줄로 보여줍니다.본문 내용은 두줄로 보여줍니다.본문 내용은 두줄로 보여줍니다.본문 내용은 두줄로 보여줍니다.본문 내용은 두줄로 보여줍니다.",
        category = "전체",
        label = "HOT",
        profileImage = "https://avatars.githubusercontent.com/u/98209004?v=4",
        name = "익명의 앱븐이",
        generationAndPart = ""
    ),
    HomePlaygroundPostModel(
        postId = 2,
        title = "제목 길이가 엄청나게 길어진다면????나 메이커스팀인데 메팀 좋다 ",
        content = "본문 내용은 두줄로 보여줍니다.본문 내용은 두줄로 보여줍니다.본문 내용은 두줄로 보여줍니다.본문 내용은 두줄로 보여줍니다.본문 내용은 두줄로 보여줍니다.",
        category = "전체",
        label = "HOT",
        profileImage = "https://avatars.githubusercontent.com/u/98209004?v=4",
        name = "박효빈",
        generationAndPart = "35기 안드로이드"
    ),
    HomePlaygroundPostModel(
        postId = 3,
        title = "나 메이커스팀인데 메팀 좋다",
        content = "본문 내용이 엄청 짧다면???",
        category = "전체",
        label = "HOT",
        profileImage = "https://avatars.githubusercontent.com/u/98209004?v=4",
        name = "박효빈",
        generationAndPart = "35기 안드로이드"
    ),
    HomePlaygroundPostModel(
        postId = 4,
        title = "사진이 없다면?",
        content = "본문 내용은 두줄로 보여줍니다.본문 내용은 두줄로 보여줍니다.본문 내용은 두줄로 보여줍니다.",
        category = "전체",
        label = "HOT",
        profileImage = "",
        name = "박효빈",
        generationAndPart = "35기 기획"
    )
)
private const val POPULAR_NEWS_LIST_SIZE = 3

@Composable
internal fun HomePopularNewsSection(
    postList: ImmutableList<HomePlaygroundPostModel>,
    navigateToWebLink: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    var highlightedIndex by remember { mutableIntStateOf(0) }

    LaunchedEffect(postList) {
        while (true) {
            delay(2400)
            highlightedIndex = (highlightedIndex + 1) % POPULAR_NEWS_LIST_SIZE
        }
    }

    Column(
        modifier = modifier
            .padding(top = 22.dp)
    ) {
        Text(
            text = "지금 인기 소식 🔥",
            style = SoptTheme.typography.heading20B,
            color = SoptTheme.colors.primary
        )

        Spacer(modifier = Modifier.height(16.dp))

        postList.take(POPULAR_NEWS_LIST_SIZE).forEachIndexed { index, post ->
            val borderAlpha by animateFloatAsState(
                targetValue = if (index == highlightedIndex) 1f else 0f,
                animationSpec = tween(
                    durationMillis = 300,
                    easing = EaseIn
                )
            )
            val gradientStroke = Brush.linearGradient(
                colors = listOf(
                    Orange300.copy(borderAlpha),
                    Orange200.copy(borderAlpha),
                    Orange500.copy(borderAlpha)
                )
            )

            with(post) {
                HomePlaygroundPost(
                    profileImage = profileImage,
                    userName = name,
                    userPart = generationAndPart,
                    label = label,
                    category = category,
                    title = title,
                    description = content,
                    onClick = {
                        navigateToWebLink(webLink)
                    },
                    modifier = Modifier
                        .then(
                            if (index == 0) {
                                Modifier
                            } else {
                                Modifier.padding(top = 10.dp)
                            }
                        )
                        .border(
                            width = 1.dp,
                            brush = gradientStroke,
                            shape = RoundedCornerShape(12.dp)
                        )
                )
            }
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF000000)
@Composable
private fun HomePlaygroundSectionPreview() {
    SoptTheme {
        HomePopularNewsSection(
            postList = persistentListOf(
                HomePlaygroundPostModel(
                    postId = 1,
                    title = "나 메이커스팀인데 메팀 좋다",
                    content = "본문 내용은 두줄로 보여줍니다.본문 내용은 두줄로 보여줍니다.본문 내용은 두줄로 보여줍니다.본문 내용은 두줄로 보여줍니다.본문 내용은 두줄로 보여줍니다.",
                    category = "전체",
                    label = "HOT",
                    profileImage = "https://avatars.githubusercontent.com/u/98209004?v=4",
                    name = "익명의 앱븐이",
                    generationAndPart = ""
                )
            ),
            navigateToWebLink = {}
        )
    }
}
