package org.sopt.official.feature.home.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color.Companion.Unspecified
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.ParagraphStyle
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import androidx.compose.ui.unit.sp
import org.sopt.official.designsystem.SoptTheme
import org.sopt.official.designsystem.SoptTheme.colors
import org.sopt.official.designsystem.SoptTheme.typography
import org.sopt.official.designsystem.SuitBold
import org.sopt.official.feature.home.R.drawable.ic_soptlog
import org.sopt.official.feature.home.component.UserSoptState.Member
import org.sopt.official.feature.home.component.UserSoptState.NonMember

@Composable
internal fun HomeUserSoptLogDashBoard(
    homeUserSoptLogModel: HomeUserSoptLogModel,
    userSoptState: UserSoptState,
    modifier: Modifier = Modifier,
) {
    val introduceText by remember {
        mutableStateOf(
            when (userSoptState) {
                is NonMember -> buildAnnotatedString {
                    withStyle(style = ParagraphStyle(lineHeight = 28.sp)) {
                        withStyle(
                            style = SpanStyle(
                                fontFamily = SuitBold,
                                fontSize = 18.sp,
                                letterSpacing = (-0.02).em
                            )
                        ) { append("안녕하세요\nSOPT의 열정이 되어주세요!") }
                    }
                }

                is Member -> buildAnnotatedString {
                    withStyle(style = ParagraphStyle(lineHeight = 28.sp)) {
                        withStyle(
                            style = SpanStyle(
                                fontFamily = SuitBold,
                                fontSize = 18.sp,
                                letterSpacing = (-0.02).em
                            )
                        ) { append(homeUserSoptLogModel.emphasizedDescription) }
                        append(homeUserSoptLogModel.remainingDescription)
                    }
                }
            }
        )
    }

    val recentGenerationText by remember {
        mutableStateOf(
            when (userSoptState) {
                is NonMember -> "비회원"
                is Member -> userSoptState.recentGeneration
            }
        )
    }

    val isActivated by remember {
        mutableStateOf(
            when (userSoptState) {
                is NonMember -> false
                is Member -> userSoptState.isActivated
            }
        )
    }

    val generations by remember {
        mutableStateOf(
            when (userSoptState) {
                is NonMember -> emptyList()
                is Member -> userSoptState.lastGenerations
            }
        )
    }

    HomeBox(
        modifier = modifier.fillMaxWidth(),
        content = {
            Row(
                verticalAlignment = CenterVertically,
                modifier = Modifier.padding(all = 16.dp),
            ) {
                Column {
                    Text(
                        text = introduceText,
                        style = typography.body18M,
                        color = colors.onBackground,
                    )
                    Spacer(modifier = Modifier.height(height = 12.dp))
                    HomeGenerationChips(
                        isUserActivated = isActivated,
                        userRecentGeneration = recentGenerationText,
                        generations = generations,
                    )
                }
                Spacer(modifier = Modifier.weight(weight = 1f))
                Icon(
                    imageVector = ImageVector.vectorResource(ic_soptlog),
                    contentDescription = null,
                    tint = Unspecified,
                )
            }
        }
    )
}

@Preview
@Composable
private fun HomeUserSoptLogDashBoardPreview() {
    SoptTheme {
        HomeUserSoptLogDashBoard(
            homeUserSoptLogModel = HomeUserSoptLogModel(
                activityDescription = "<b>김승환</b>님은\nSOPT와 15개월째"
            ),
            userSoptState = Member(
                isActivated = true,
                generations = listOf(1, 2, 3, 4, 5, 6, 7)
            ),
        )
    }
}

data class HomeUserSoptLogModel(
    val activityDescription: String,
) {
    private val regex by lazy { Regex("<b>(.*?)</b>") }

    val emphasizedDescription: String =
        regex.find(activityDescription)?.groups?.get(1)?.value.orEmpty()

    val remainingDescription: String =
        " " + regex.replace(activityDescription, "").trim()
}
