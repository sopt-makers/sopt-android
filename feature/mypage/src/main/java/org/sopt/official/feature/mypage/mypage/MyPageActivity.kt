/*
 * MIT License
 * Copyright 2023-2024 SOPT - Shout Our Passion Together
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
import androidx.activity.viewModels
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.flowWithLifecycle
import com.jakewharton.processphoenix.ProcessPhoenix
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.collections.immutable.persistentListOf
import org.sopt.official.auth.model.UserActiveState
import org.sopt.official.common.navigator.NavigatorProvider
import org.sopt.official.common.util.serializableExtra
import org.sopt.official.designsystem.SoptTheme
import org.sopt.official.feature.mypage.component.MyPageDialog
import org.sopt.official.feature.mypage.component.MyPageSection
import org.sopt.official.feature.mypage.component.MyPageTopBar
import org.sopt.official.feature.mypage.model.MyPageUiModel
import org.sopt.official.feature.mypage.model.MyPageUiState
import org.sopt.official.feature.mypage.signOut.SignOutActivity
import org.sopt.official.feature.mypage.soptamp.sentence.AdjustSentenceActivity
import org.sopt.official.feature.mypage.web.WebUrlConstant
import java.io.Serializable
import javax.inject.Inject

enum class ResultCode {
    LOG_IN
}

@AndroidEntryPoint
class MyPageActivity : AppCompatActivity() {
    private val viewModel by viewModels<MyPageViewModel>()
    private val args by serializableExtra(StartArgs(UserActiveState.UNAUTHENTICATED))

    @Inject
    lateinit var navigatorProvider: NavigatorProvider

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SoptTheme {
                val context = LocalContext.current
                val lifecycleOwner = LocalLifecycleOwner.current

                val isAuthenticated by viewModel.userActiveState.collectAsStateWithLifecycle(initialValue = false)
                val dialogState by viewModel.dialogState.collectAsStateWithLifecycle()
                val scrollState = rememberScrollState()

                val serviceSectionItems = remember {
                    persistentListOf(
                        MyPageUiModel.Header(title = "서비스 이용 방침"),
                        MyPageUiModel.MyPageItem(
                            title = "개인정보 처리 방침",
                            onItemClick = { startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(WebUrlConstant.NOTICE_PRIVATE_INFO))) }
                        ),
                        MyPageUiModel.MyPageItem(
                            title = "서비스 이용약관",
                            onItemClick = { startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(WebUrlConstant.NOTICE_SERVICE_RULE))) }
                        ),
                        MyPageUiModel.MyPageItem(
                            title = "의견 보내기",
                            onItemClick = { startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(WebUrlConstant.OPINION_KAKAO_CHAT))) }
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
                            onItemClick = { viewModel.showDialogState(MyPageAction.CLEAR_SOPTAMP) }
                        )
                    )
                }

                val etcSectionItems = remember {
                    persistentListOf(
                        MyPageUiModel.Header(title = "기타"),
                        MyPageUiModel.MyPageItem(
                            title = "로그아웃",
                            onItemClick = { viewModel.showDialogState(MyPageAction.LOGOUT) }
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
                                setResult(ResultCode.LOG_IN.ordinal)
                                onBackPressedDispatcher.onBackPressed()
                            }
                        )
                    )
                }

                LaunchedEffect(Unit) {
                    args?.userActiveState?.let {
                        viewModel.setUserActiveState(MyPageUiState.User(it))
                    }
                }

                LaunchedEffect(viewModel.finish, lifecycleOwner) {
                    viewModel.finish.flowWithLifecycle(lifecycle = lifecycleOwner.lifecycle)
                        .collect {
                            ProcessPhoenix.triggerRebirth(context, navigatorProvider.getAuthActivityIntent())
                        }
                }

                if (dialogState is MyPageUiState.Dialog) {
                    ShowMyPageDialog(
                        action = (dialogState as MyPageUiState.Dialog).action,
                        onDismissRequest = viewModel::onDismiss,
                        onClearSoptampClick = viewModel::resetSoptamp,
                        onLogoutClick = viewModel::logOut
                    )
                }

                Scaffold(modifier = Modifier
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
                        if (isAuthenticated) {
                            MyPageSection(items = notificationSectionItems)
                            Spacer(modifier = Modifier.height(16.dp))
                            MyPageSection(items = soptampSectionItems)
                            Spacer(modifier = Modifier.height(16.dp))
                            MyPageSection(items = etcSectionItems)
                        } else {
                            MyPageSection(items = etcLoginSectionItems)
                        }
                        Spacer(modifier = Modifier.height(32.dp))
                    }
                }
            }
        }
    }

    data class StartArgs(
        val userActiveState: UserActiveState
    ) : Serializable

    companion object {
        @JvmStatic
        fun getIntent(context: Context, args: StartArgs) = Intent(context, MyPageActivity::class.java).apply {
            putExtra("args", args)
        }
    }
}

@Composable
private fun ShowMyPageDialog(
    action: MyPageAction,
    onDismissRequest: () -> Unit,
    onClearSoptampClick: () -> Unit,
    onLogoutClick: () -> Unit
) {
    when (action) {
        MyPageAction.CLEAR_SOPTAMP -> {
            MyPageDialog(
                onDismissRequest = onDismissRequest,
                title = "미션을 초기화 하실건가요?",
                subTitle = "사진, 메모가 삭제되고\n전체 미션이 미완료상태로 초기화됩니다.",
                negativeText = "취소",
                positiveText = "초기화",
                onPositiveButtonClick = onClearSoptampClick
            )
        }

        MyPageAction.LOGOUT -> {
            MyPageDialog(
                onDismissRequest = onDismissRequest,
                title = "로그아웃",
                subTitle = "정말 로그아웃을 하실 건가요?",
                negativeText = "취소",
                positiveText = "로그아웃",
                onPositiveButtonClick = onLogoutClick
            )
        }
    }
}
