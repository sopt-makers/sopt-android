/*
 * MIT License
 * Copyright 2023-2025 SOPT - Shout Our Passion Together
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package org.sopt.official.feature.mypage.mypage

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import dev.zacsweers.metro.Inject
import kotlinx.collections.immutable.persistentListOf
import org.sopt.official.common.navigator.NavigatorProvider
import org.sopt.official.common.util.serializableExtra
import org.sopt.official.designsystem.SoptTheme
import org.sopt.official.domain.auth.repository.AuthRepository
import org.sopt.official.domain.notification.repository.NotificationRepository
import org.sopt.official.domain.soptamp.repository.StampRepository
import org.sopt.official.feature.mypage.component.MyPageDialog
import org.sopt.official.feature.mypage.component.MyPageSection
import org.sopt.official.feature.mypage.component.MyPageTopBar
import org.sopt.official.feature.mypage.model.MyPageUiModel
import org.sopt.official.feature.mypage.mypage.state.ClearSoptamp
import org.sopt.official.feature.mypage.mypage.state.CloseDialog
import org.sopt.official.feature.mypage.mypage.state.ConfirmLogout
import org.sopt.official.feature.mypage.mypage.state.MyPageDialogState
import org.sopt.official.feature.mypage.mypage.state.RequestLogout
import org.sopt.official.feature.mypage.mypage.state.ResetSoptamp
import org.sopt.official.feature.mypage.mypage.state.rememberMyPageUiState
import org.sopt.official.feature.mypage.signout.SignOutActivity
import org.sopt.official.feature.mypage.soptamp.ui.AdjustSentenceActivity
import org.sopt.official.feature.mypage.web.WebUrlConstant
import org.sopt.official.model.UserStatus
import java.io.Serializable

@Inject
class MyPageActivity(
    private val authRepository: AuthRepository,
    private val stampRepository: StampRepository,
    private val notificationRepository: NotificationRepository,
    private val navigatorProvider: NavigatorProvider
) : AppCompatActivity() {
    private val args by serializableExtra(Argument(UserStatus.UNAUTHENTICATED))

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SoptTheme {
                val uiState = rememberMyPageUiState(
                    userActiveState = args?.userActiveState ?: UserStatus.UNAUTHENTICATED,
                    authRepository = authRepository,
                    stampRepository = stampRepository,
                    notificationRepository = notificationRepository,
                    onRestartApp = { startActivity(navigatorProvider.getAuthActivityIntent()) }
                )

                val context = LocalContext.current

                val scrollState = rememberScrollState()

                val serviceSectionItems = remember {
                    persistentListOf(
                        MyPageUiModel.Header(title = "서비스 이용 방침"),
                        MyPageUiModel.MyPageItem(
                            title = "개인정보 처리 방침",
                            onItemClick = {
                                startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(WebUrlConstant.NOTICE_PRIVATE_INFO)))
                            }
                        ),
                        MyPageUiModel.MyPageItem(
                            title = "서비스 이용약관",
                            onItemClick = {
                                startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(WebUrlConstant.NOTICE_SERVICE_RULE)))
                            }
                        ),
                        MyPageUiModel.MyPageItem(
                            title = "의견 보내기",
                            onItemClick = {
                                startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(WebUrlConstant.OPINION_KAKAO_CHAT)))
                            }
                        )
                    )
                }

                val notificationSectionItems = remember {
                    persistentListOf(
                        MyPageUiModel.Header(title = "알림 설정"),
                        MyPageUiModel.MyPageItem(
                            title = "알림 설정하기",
                            onItemClick = {
                                Intent().apply {
                                    action = Settings.ACTION_APP_NOTIFICATION_SETTINGS
                                    putExtra(Settings.EXTRA_APP_PACKAGE, packageName)
                                    startActivity(this)
                                }
                            }
                        )
                    )
                }

                val soptampSectionItems = remember {
                    persistentListOf(
                        MyPageUiModel.Header(title = "솝탬프 설정"),
                        MyPageUiModel.MyPageItem(
                            title = "한 마디 편집",
                            onItemClick = { startActivity(AdjustSentenceActivity.getIntent(context)) }
                        ),
                        MyPageUiModel.MyPageItem(
                            title = "스탬프 초기화",
                            onItemClick = {
                                uiState.onEventSink(ClearSoptamp)
                            }
                        )
                    )
                }

                val etcSectionItems = remember {
                    persistentListOf(
                        MyPageUiModel.Header(title = "기타"),
                        MyPageUiModel.MyPageItem(
                            title = "로그아웃",
                            onItemClick = {
                                uiState.onEventSink(RequestLogout)
                            }
                        ),
                        MyPageUiModel.MyPageItem(
                            title = "탈퇴하기",
                            onItemClick = { startActivity(SignOutActivity.getIntent(context)) }
                        )
                    )
                }

                val etcLoginSectionItems = remember {
                    persistentListOf(
                        MyPageUiModel.Header(title = "기타"),
                        MyPageUiModel.MyPageItem(
                            title = "로그인",
                            onItemClick = {
                                startActivity(navigatorProvider.getAuthActivityIntent())
                            }
                        )
                    )
                }

                Scaffold(
                    modifier = Modifier
                        .background(SoptTheme.colors.background)
                        .fillMaxSize(),
                    topBar = {
                        MyPageTopBar(
                            title = "마이페이지",
                            onNavigationIconClick = { onBackPressedDispatcher.onBackPressed() }
                        )
                    }
                ) { innerPadding ->
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(innerPadding)
                            .background(SoptTheme.colors.background)
                            .verticalScroll(scrollState)
                    ) {
                        Spacer(modifier = Modifier.height(20.dp))
                        MyPageSection(items = serviceSectionItems)
                        Spacer(modifier = Modifier.height(16.dp))
                        when (uiState.user) {
                            UserStatus.ACTIVE, UserStatus.INACTIVE -> {
                                MyPageSection(items = notificationSectionItems)
                                Spacer(modifier = Modifier.height(16.dp))
                                MyPageSection(items = soptampSectionItems)
                                Spacer(modifier = Modifier.height(16.dp))
                                MyPageSection(items = etcSectionItems)
                            }

                            UserStatus.UNAUTHENTICATED -> {
                                MyPageSection(items = etcLoginSectionItems)
                            }
                        }
                        Spacer(modifier = Modifier.height(32.dp))
                    }

                    if (uiState.dialogState != MyPageDialogState.CLEAR) {
                        ShowMyPageDialog(
                            dialogState = uiState.dialogState,
                            onDismissRequest = { uiState.onEventSink(CloseDialog) },
                            onClearSoptampClick = { uiState.onEventSink(ResetSoptamp) },
                            onLogoutClick = { uiState.onEventSink(ConfirmLogout) }
                        )
                    }
                }
            }
        }
    }

    data class Argument(
        val userActiveState: UserStatus,
    ) : Serializable

    companion object {
        @JvmStatic
        fun getIntent(context: Context, args: Argument) = Intent(context, MyPageActivity::class.java)
            .putExtra("args", args)

    }
}

@Composable
private fun ShowMyPageDialog(
    dialogState: MyPageDialogState,
    onDismissRequest: () -> Unit,
    onClearSoptampClick: () -> Unit,
    onLogoutClick: () -> Unit,
) {
    when (dialogState) {
        MyPageDialogState.CLEAR_SOPTAMP -> {
            MyPageDialog(
                onDismissRequest = onDismissRequest,
                title = "미션을 초기화 하실건가요?",
                subTitle = "사진, 메모가 삭제되고\n전체 미션이 미완료상태로 초기화됩니다.",
                negativeText = "취소",
                positiveText = "초기화",
                onPositiveButtonClick = onClearSoptampClick
            )
        }

        MyPageDialogState.REQUEST_LOGOUT -> {
            MyPageDialog(
                onDismissRequest = onDismissRequest,
                title = "로그아웃",
                subTitle = "정말 로그아웃을 하실 건가요?",
                negativeText = "취소",
                positiveText = "로그아웃",
                onPositiveButtonClick = onLogoutClick
            )
        }

        else -> {}
    }
}