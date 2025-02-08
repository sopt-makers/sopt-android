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
import androidx.hilt.navigation.compose.hiltViewModel
import coil3.annotation.ExperimentalCoilApi
import coil3.compose.AsyncImagePreviewHandler
import coil3.compose.LocalAsyncImagePreviewHandler
import coil3.test.FakeImage
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import org.sopt.official.designsystem.SoptTheme
import org.sopt.official.feature.soptlog.component.DashBoardItem
import org.sopt.official.feature.soptlog.component.EditProfileButton
import org.sopt.official.feature.soptlog.component.SoptlogDashBoard
import org.sopt.official.feature.soptlog.component.SoptlogIntroduction
import org.sopt.official.feature.soptlog.component.SoptlogProfile
import org.sopt.official.feature.soptlog.component.TodayFortuneBanner

@Composable
fun SoptlogRoute(
    viewModel: SoptLogViewModel = hiltViewModel(),
) {
    val soptLogInfo = viewModel.soptLogInfo

    if (soptLogInfo == null) {

    } else {
        with(soptLogInfo) {
            SoptlogScreen(
                profileImageUrl = profileImageUrl,
                name = userName,
                part = part,
                introduction = profileMessage,
                dashBoardItems = persistentListOf(
                    DashBoardItem(
                        title = "솝트레벨",
                        icon = R.drawable.ic_sopt_level,
                        content = soptLevel,
                    ),
                    DashBoardItem(
                        title = "콕찌르기",
                        icon = R.drawable.ic_poke_hand,
                        content = pokeCount,
                    ),
                    DashBoardItem(
                        title = if (isActive) "솝템프" else "솝트와",
                        icon = if (isActive) R.drawable.ic_soptamp_hand else R.drawable.ic_calender,
                        content = if (isActive) soptampRank else during,
                    )
                ),
                todayFortuneTitle = todayFortuneTitle,
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SoptlogScreen(
    profileImageUrl: String,
    name: String,
    part: String,
    introduction: String?,
    dashBoardItems: ImmutableList<DashBoardItem>,
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

            SoptlogIntroduction(
                introduction = introduction,
                onClick = {
                    // TODO: 자기소개 작성 웹페이지로 이동
                }
            )

            Spacer(modifier = Modifier.height(12.dp))

            SoptlogDashBoard(
                dashBoardItems = dashBoardItems
            )

            Spacer(modifier = Modifier.height(20.dp))

            EditProfileButton(
                onClick = {
                    // TODO: 프로필 수정 화면으로 이동
                }
            )
        }

        Spacer(modifier = Modifier.height(40.dp))

        TodayFortuneBanner(
            title = todayFortuneTitle,
            onClick = {
                // TODO: 오늘의 운세 화면으로 이동
            }
        )
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
                    DashBoardItem(
                        title = "솝트레벨",
                        icon = R.drawable.ic_sopt_level,
                        content = "Lv.6",
                    ),
                    DashBoardItem(
                        title = "콕찌르기",
                        icon = R.drawable.ic_poke_hand,
                        content = "208회",
                    ),
                    DashBoardItem(
                        title = "솝트와",
                        icon = R.drawable.ic_calender,
                        content = "33개월",
                    )
                ),
                todayFortuneTitle = "오늘 내 운세는?"
            )
        }
    }
}
