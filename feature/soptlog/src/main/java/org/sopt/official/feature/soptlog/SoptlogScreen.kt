package org.sopt.official.feature.soptlog

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil3.annotation.ExperimentalCoilApi
import coil3.compose.AsyncImagePreviewHandler
import coil3.compose.LocalAsyncImagePreviewHandler
import coil3.test.FakeImage
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import org.sopt.official.designsystem.SoptTheme
import org.sopt.official.feature.soptlog.component.EditProfileButton
import org.sopt.official.feature.soptlog.component.SoptlogDashBoard
import org.sopt.official.feature.soptlog.component.SoptlogDashBoardItemData
import org.sopt.official.feature.soptlog.component.SoptlogIntroduction
import org.sopt.official.feature.soptlog.component.SoptlogProfile
import org.sopt.official.feature.soptlog.component.TodayFortuneBanner

@Composable
fun SoptlogRoute() {
    SoptlogScreen(
        profileImageUrl = "https://sopt.org/wp-content/uploads/2021/06/sopt_logo.png",
        name = "차은우",
        part = "안드로이드",
        introduction = "자기소개는 15글자까지",
        dashBoardItems = persistentListOf(
            SoptlogDashBoardItemData(
                title = "솝트레벨",
                iconUrl = "https://sopt.org/wp-content/uploads/2021/06/sopt_logo.png",
                content = "Lv.6",
            ),
            SoptlogDashBoardItemData(
                title = "콕찌르기",
                iconUrl = "https://sopt.org/wp-content/uploads/2021/06/sopt_logo2.png",
                content = "208회",
            ),
            SoptlogDashBoardItemData(
                title = "솝트와",
                iconUrl = "https://sopt.org/wp-content/uploads/2021/06/sopt_logo2.png",
                content = "33개월",
            )
        ),
        todayFortuneTitle = "오늘 내 운세는?",
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SoptlogScreen(
    profileImageUrl: String,
    name: String,
    part: String,
    introduction: String?,
    dashBoardItems: ImmutableList<SoptlogDashBoardItemData>,
    todayFortuneTitle: String,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(SoptTheme.colors.background)
    ) {
        CenterAlignedTopAppBar(
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = SoptTheme.colors.background,
                titleContentColor = SoptTheme.colors.surface
            ),
            title = {
                Text(
                    text = "솝트로그",
                    style = SoptTheme.typography.body16M,
                )
            }
        )

        SoptlogContents {
            SoptlogProfile(
                profileImageUrl = profileImageUrl,
                name = name,
                part = part,
            )

            Spacer(modifier = Modifier.height(12.dp))

            SoptlogIntroduction(introduction = introduction)

            Spacer(modifier = Modifier.height(12.dp))

            SoptlogDashBoard(
                dashBoardItems = dashBoardItems
            )

            Spacer(modifier = Modifier.height(20.dp))

            EditProfileButton() {
                // TODO: 프로필 수정 화면으로 이동
            }
        }

        Spacer(modifier = Modifier.height(40.dp))

        TodayFortuneBanner(
            title = todayFortuneTitle
        ) {
            // TODO: 오늘의 운세 화면으로 이동
        }
    }
}

@Composable
fun SoptlogContents(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp)
    ) {
        content()
    }
}

@OptIn(ExperimentalCoilApi::class)
@Preview
@Composable
fun PreviewSoptlogScreen() {
    SoptTheme {
        val previewHandler = AsyncImagePreviewHandler {
            FakeImage(color = 0xFFE0E0E0.toInt())
        }

        CompositionLocalProvider(LocalAsyncImagePreviewHandler provides previewHandler) {
            SoptlogScreen(
                profileImageUrl = "https://sopt.org/wp-content/uploads/2021/06/sopt_logo.png",
                name = "차은우",
                part = "안드로이드",
                introduction = "자기소개는 15글자까지",
                dashBoardItems = persistentListOf(
                    SoptlogDashBoardItemData(
                        title = "솝트레벨",
                        iconUrl = "https://sopt.org/wp-content/uploads/2021/06/sopt_logo.png",
                        content = "Lv.6",
                    ),
                    SoptlogDashBoardItemData(
                        title = "콕찌르기",
                        iconUrl = "https://sopt.org/wp-content/uploads/2021/06/sopt_logo2.png",
                        content = "208회",
                    ),
                    SoptlogDashBoardItemData(
                        title = "솝트와",
                        iconUrl = "https://sopt.org/wp-content/uploads/2021/06/sopt_logo2.png",
                        content = "33개월",
                    )
                ),
                todayFortuneTitle = "오늘 내 운세는?"
            )
        }
    }
}
