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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.flowWithLifecycle
import com.jakewharton.processphoenix.ProcessPhoenix
import dagger.hilt.android.AndroidEntryPoint
import org.sopt.official.auth.model.UserActiveState
import org.sopt.official.common.navigator.NavigatorProvider
import org.sopt.official.common.util.serializableExtra
import org.sopt.official.designsystem.SoptTheme
import org.sopt.official.feature.mypage.R
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
                val soptampDialogState by viewModel.soptampDialogState.collectAsStateWithLifecycle()
                val logoutDialogState by viewModel.logoutDialogState.collectAsStateWithLifecycle()
                val scrollState = rememberScrollState()

                val serviceSectionItems = listOf(
                    MyPageUiModel.Header(title = stringResource(R.string.mypage_service_info_title)),
                    MyPageUiModel.MyPageItem(
                        title = stringResource(R.string.mypage_private_info),
                        onItemClick = { startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(WebUrlConstant.NOTICE_PRIVATE_INFO))) }
                    ),
                    MyPageUiModel.MyPageItem(
                        title = stringResource(R.string.mypage_service_rule),
                        onItemClick = { startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(WebUrlConstant.NOTICE_SERVICE_RULE))) }
                    ),
                    MyPageUiModel.MyPageItem(
                        title = stringResource(R.string.mypage_send_opinion),
                        onItemClick = { startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(WebUrlConstant.OPINION_KAKAO_CHAT))) }
                    )
                )

                val notificationSectionItems = listOf(
                    MyPageUiModel.Header(title = stringResource(R.string.mypage_notification_setting)),
                    MyPageUiModel.MyPageItem(
                        title = stringResource(R.string.mypage_notification),
                        onItemClick = {
                            Intent().apply {
                                action = Settings.ACTION_APP_NOTIFICATION_SETTINGS
                                putExtra(Settings.EXTRA_APP_PACKAGE, packageName)
                                startActivity(this)
                            }
                        }
                    )
                )

                val soptampSectionItems = listOf(
                    MyPageUiModel.Header(title = stringResource(R.string.mypage_soptamp_setting_title)),
                    MyPageUiModel.MyPageItem(
                        title = stringResource(R.string.mypage_adjust_sentence),
                        onItemClick = { startActivity(AdjustSentenceActivity.getIntent(context)) }
                    ),
                    MyPageUiModel.MyPageItem(
                        title = stringResource(R.string.mypage_reset_stamp),
                        onItemClick = { viewModel.updateSoptampDialog(true) }
                    )
                )

                val etcSectionItems = listOf(
                    MyPageUiModel.Header(title = stringResource(R.string.mypage_etc_title)),
                    MyPageUiModel.MyPageItem(
                        title = stringResource(R.string.mypage_log_out),
                        onItemClick = { viewModel.updateLogoutDialog(true) }
                    ),
                    MyPageUiModel.MyPageItem(
                        title = stringResource(R.string.mypage_sign_out),
                        onItemClick = { startActivity(SignOutActivity.getIntent(context)) }
                    )
                )

                val etcLoginSectionItems = listOf(
                    MyPageUiModel.Header(title = stringResource(R.string.mypage_etc_title)),
                    MyPageUiModel.MyPageItem(
                        title = stringResource(R.string.mypage_log_in),
                        onItemClick = {
                            setResult(ResultCode.LOG_IN.ordinal)
                            onBackPressedDispatcher.onBackPressed()
                        }
                    )
                )

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

                if (soptampDialogState) {
                    MyPageDialog(
                        onDismissRequest = { viewModel.updateSoptampDialog(false) },
                        title = R.string.mypage_alert_soptamp_reset_title,
                        subTitle = R.string.mypage_alert_soptamp_reset_subtitle,
                        negativeText = R.string.mypage_alert_soptamp_reset_negative,
                        positiveText = R.string.mypage_alert_soptamp_reset_positive,
                        onNegativeButtonClick = { viewModel.updateSoptampDialog(false) },
                        onPositiveButtonClick = viewModel::resetSoptamp
                    )
                }

                if (logoutDialogState) {
                    MyPageDialog(
                        onDismissRequest = { viewModel.updateLogoutDialog(false) },
                        title = R.string.mypage_alert_log_out_title,
                        subTitle = R.string.mypage_alert_log_out_subtitle,
                        negativeText = R.string.mypage_alert_log_out_negative,
                        positiveText = R.string.mypage_alert_log_out_positive,
                        onNegativeButtonClick = { viewModel.updateLogoutDialog(false) },
                        onPositiveButtonClick = viewModel::logOut
                    )
                }

                Scaffold(modifier = Modifier
                    .background(SoptTheme.colors.background)
                    .fillMaxSize(),
                    topBar = {
                        MyPageTopBar(
                            title = R.string.toolbar_mypage,
                            onIconClick = { onBackPressedDispatcher.onBackPressed() }
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
