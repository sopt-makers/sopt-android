package org.sopt.official.feature.home.component

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.coroutines.delay
import org.sopt.official.designsystem.Orange300
import org.sopt.official.designsystem.Orange400
import org.sopt.official.designsystem.SoptTheme
import org.sopt.official.designsystem.component.UrlImage
import org.sopt.official.feature.home.R
import org.sopt.official.feature.home.model.HomePlaygroundFeedModel

internal val feedList = persistentListOf(
    HomePlaygroundFeedModel(
        postId = 1,
        title = "나 메이커스팀인데 메팀 좋다",
        content = "본문 내용은 두줄로 보여줍니다.본문 내용은 두줄로 보여줍니다.본문 내용은 두줄로 보여줍니다.본문 내용은 두줄로 보여줍니다.본문 내용은 두줄로 보여줍니다.",
        category = "전체",
        label = "HOT",
        profileImage = "https://avatars.githubusercontent.com/u/98209004?v=4",
        name = "박효빈",
        part = "35기 안드로이드"
    ),
    HomePlaygroundFeedModel(
        postId = 2,
        title = "제목 길이가 엄청나게 길어진다면????나 메이커스팀인데 메팀 좋다 ",
        content = "본문 내용은 두줄로 보여줍니다.본문 내용은 두줄로 보여줍니다.본문 내용은 두줄로 보여줍니다.본문 내용은 두줄로 보여줍니다.본문 내용은 두줄로 보여줍니다.",
        category = "전체",
        label = "HOT",
        profileImage = "https://avatars.githubusercontent.com/u/98209004?v=4",
        name = "박효빈",
        part = "35기 안드로이드"
    ),
    HomePlaygroundFeedModel(
        postId = 3,
        title = "나 메이커스팀인데 메팀 좋다",
        content = "본문 내용이 엄청 짧다면???",
        category = "전체",
        label = "HOT",
        profileImage = "https://avatars.githubusercontent.com/u/98209004?v=4",
        name = "박효빈",
        part = "35기 안드로이드"
    ),
    HomePlaygroundFeedModel(
        postId = 4,
        title = "사진이 없다면?",
        content = "본문 내용은 두줄로 보여줍니다.본문 내용은 두줄로 보여줍니다.본문 내용은 두줄로 보여줍니다.",
        category = "전체",
        label = "HOT",
        profileImage = "",
        name = "박효빈",
        part = "35기 기획"
    )
)

@Composable
internal fun HomePlaygroundSection(
    feedList: ImmutableList<HomePlaygroundFeedModel>,
    navigateToPlayground: () -> Unit,
    navigateToFeed: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    var highlightedIndex by remember { mutableIntStateOf(0) }

    LaunchedEffect(feedList) {
        while (true) {
            delay(2400)
            highlightedIndex = (highlightedIndex + 1) % 4
        }
    }

    Box(
        modifier = modifier.fillMaxWidth()
    ) {
        Icon(
            imageVector = ImageVector.vectorResource(R.drawable.ic_home_smile),
            contentDescription = null,
            tint = Color.Unspecified,
            modifier = Modifier
                .align(Alignment.TopEnd)
        )

        Column(
            modifier = Modifier
                .padding(top = 22.dp)
        ) {
            Text(
                text = "지금 인기 소식 🔥",
                style = SoptTheme.typography.heading20B,
                color = SoptTheme.colors.primary
            )

            Spacer(modifier = Modifier.height(16.dp))

            feedList.take(4).forEachIndexed { index, post ->
                val borderColor by animateColorAsState(
                    targetValue = if (index == highlightedIndex) Orange400 else Color.Transparent,
                    animationSpec = tween(
                        durationMillis = 300,
                        easing = FastOutSlowInEasing
                    )
                )

                PlaygroundPostItem(
                    post = post,
                    navigateToPost = navigateToFeed,
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
                            color = borderColor,
                            shape = RoundedCornerShape(12.dp)
                        )
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(2.dp),
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .clip(CircleShape)
                    .clickable(onClick = navigateToPlayground)
                    .background(SoptTheme.colors.onSurface900)
                    .padding(vertical = 8.dp)
                    .padding(start = 20.dp, end = 12.dp)
            ) {
                Text(
                    text = "다른 게시물 보러가기",
                    style = SoptTheme.typography.body13M,
                    color = SoptTheme.colors.onSurface400
                )
                Icon(
                    imageVector = ImageVector.vectorResource(R.drawable.ic_arrow_right),
                    contentDescription = null,
                    tint = SoptTheme.colors.onSurface400,
                    modifier = Modifier.size(16.dp)
                )
            }
        }
    }
}

@Composable
private fun PlaygroundPostItem(
    post: HomePlaygroundFeedModel,
    navigateToPost: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(17.dp),
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .fillMaxWidth()
            .background(
                color = SoptTheme.colors.onSurface900,
                shape = RoundedCornerShape(12.dp)
            )
            .clickable {
                navigateToPost(post.postId)
            }
            .padding(horizontal = 17.dp)
    ) {
        with(post) {
            ProfileItem(
                profileImage = profileImage,
                name = name,
                part = part,
                modifier = Modifier
                    .padding(top = 18.dp, bottom = 22.dp)
            )

            PostContentSection(
                label = label,
                category = category,
                title = title,
                content = content
            )
        }
    }
}

@Composable
private fun ProfileItem(
    profileImage: String,
    name: String,
    part: String,
    modifier: Modifier = Modifier
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = modifier
            .width(IntrinsicSize.Min)
    ) {
        UrlImage(
            url = profileImage,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .size(54.dp)
                .clip(shape = CircleShape)
                .background(SoptTheme.colors.onSurface800.copy(0.6f))
        )
        Text(
            text = name,
            style = SoptTheme.typography.body10M,
            color = SoptTheme.colors.primary,
            modifier = Modifier.padding(top = 3.dp, bottom = 1.dp)
        )
        Text(
            text = part,
            style = SoptTheme.typography.body10M,
            color = SoptTheme.colors.onSurface400,
            overflow = TextOverflow.Ellipsis,
            maxLines = 1
        )
    }
}

@Composable
private fun PostContentSection(
    label: String,
    category: String,
    title: String,
    content: String,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(6.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = label,
                style = SoptTheme.typography.label11SB,
                color = Orange300
            )
            Box(
                modifier = Modifier
                    .background(SoptTheme.colors.onSurface600)
                    .size(width = 1.dp, height = 7.dp)
            )
            Text(
                text = category,
                style = SoptTheme.typography.label11SB,
                color = Orange300
            )
        }

        Spacer(modifier = Modifier.height(6.dp))

        Text(
            text = title,
            style = SoptTheme.typography.title16SB,
            color = SoptTheme.colors.primary,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )

        Spacer(modifier = Modifier.height(4.dp))

        Text(
            text = content,
            style = SoptTheme.typography.body13M,
            color = SoptTheme.colors.onSurface400,
            maxLines = 2,
            minLines = 2,
            overflow = TextOverflow.Ellipsis
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF000000)
@Composable
private fun HomePlaygroundSectionPreview() {
    SoptTheme {
        HomePlaygroundSection(
            feedList = feedList,
            navigateToPlayground = {},
            navigateToFeed = {}
        )
    }
}
