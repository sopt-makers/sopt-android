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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import org.sopt.official.designsystem.Gray400
import org.sopt.official.designsystem.Gray900
import org.sopt.official.designsystem.SoptTheme
import org.sopt.official.feature.mypage.AlertDialogPositiveNegative
import org.sopt.official.feature.mypage.R
import org.sopt.official.feature.mypage.component.MyPageDialog
import org.sopt.official.feature.mypage.component.MyPageItem
import org.sopt.official.feature.mypage.component.MyPageTopBar
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

                if (dialogState) {
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

                Scaffold(modifier = Modifier
                    .background(SoptTheme.colors.background)
                    .fillMaxSize(),
                    topBar = {
                        MyPageTopBar(
                            title = R.string.toolbar_mypage,
                            onIconClick = { onBackPressedDispatcher.onBackPressed() }
                        )
                    }) { innerPadding ->
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(innerPadding)
                            .background(SoptTheme.colors.background)
                            .verticalScroll(scrollState)
                    ) {
                        Spacer(modifier = Modifier.height(20.dp))
                        ServiceInfo(
                            onPrivateClick = {
                                startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(WebUrlConstant.NOTICE_PRIVATE_INFO)))
                            },
                            onServiceClick = {
                                startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(WebUrlConstant.NOTICE_SERVICE_RULE)))
                            },
                            onOpinionClick = {
                                startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(WebUrlConstant.OPINION_KAKAO_CHAT)))
                            }
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        if (isAuthenticated) {
                            NotificationSetting(
                                onNotificationClick = {
                                    Intent().apply {
                                        action = Settings.ACTION_APP_NOTIFICATION_SETTINGS
                                        putExtra(Settings.EXTRA_APP_PACKAGE, packageName)
                                        startActivity(this)
                                    }
                                }
                            )
                            Spacer(modifier = Modifier.height(16.dp))
                            SoptampInfo(
                                onAdjustSentenceClick = {
                                    startActivity(AdjustSentenceActivity.getIntent(context))
                                },
                                onResetStampClick = {
                                    viewModel.updateSoptampDialog(true)
                                }
                            )
                            Spacer(modifier = Modifier.height(16.dp))
                            Etc(
                                onLogoutClick = {
                                    AlertDialogPositiveNegative(context)
                                        .setTitle(R.string.mypage_alert_log_out_title)
                                        .setSubtitle(R.string.mypage_alert_log_out_subtitle)
                                        .setPositiveButton(R.string.mypage_alert_log_out_positive) {
                                            viewModel.logOut()
                                        }
                                        .setNegativeButton(R.string.mypage_alert_log_out_negative)
                                        .show()
                                },
                                onSignOutClick = {
                                    startActivity(SignOutActivity.getIntent(context))
                                }
                            )
                        } else {
                            EtcLogin(
                                onLoginClick = {
                                    setResult(ResultCode.LOG_IN.ordinal)
                                    onBackPressedDispatcher.onBackPressed()
                                }
                            )
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
private fun ServiceInfo(
    onPrivateClick: () -> Unit,
    onServiceClick: () -> Unit,
    onOpinionClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .padding(horizontal = 20.dp)
            .background(
                color = Gray900,
                shape = RoundedCornerShape(10.dp)
            )
    ) {
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = stringResource(id = R.string.mypage_service_info_title),
            style = SoptTheme.typography.label12SB,
            color = Gray400,
            modifier = Modifier.padding(start = 16.dp)
        )
        Spacer(modifier = Modifier.height(23.dp))
        MyPageItem(
            text = stringResource(id = R.string.mypage_private_info),
            onButtonClick = onPrivateClick
        )
        Spacer(modifier = Modifier.height(22.dp))
        MyPageItem(
            text = stringResource(id = R.string.mypage_service_rule),
            onButtonClick = onServiceClick
        )
        Spacer(modifier = Modifier.height(22.dp))
        MyPageItem(
            text = stringResource(id = R.string.mypage_send_opinion),
            onButtonClick = onOpinionClick
        )
        Spacer(modifier = Modifier.height(27.dp))
    }
}

@Composable
private fun NotificationSetting(
    modifier: Modifier = Modifier,
    onNotificationClick: () -> Unit,
) {
    Column(
        modifier = modifier
            .padding(horizontal = 20.dp)
            .background(
                color = Gray900,
                shape = RoundedCornerShape(10.dp)
            )
    ) {
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = stringResource(id = R.string.mypage_notification_setting),
            style = SoptTheme.typography.label12SB,
            color = Gray400,
            modifier = Modifier.padding(start = 16.dp)
        )
        Spacer(modifier = Modifier.height(23.dp))
        MyPageItem(
            text = stringResource(id = R.string.mypage_notification),
            onButtonClick = onNotificationClick
        )
        Spacer(modifier = Modifier.height(27.dp))
    }
}

@Composable
private fun SoptampInfo(
    modifier: Modifier = Modifier,
    onAdjustSentenceClick: () -> Unit,
    onResetStampClick: () -> Unit
) {
    Column(
        modifier = modifier
            .padding(horizontal = 20.dp)
            .background(
                color = Gray900,
                shape = RoundedCornerShape(10.dp)
            )
    ) {
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = stringResource(id = R.string.mypage_soptamp_setting_title),
            style = SoptTheme.typography.label12SB,
            color = Gray400,
            modifier = Modifier.padding(start = 16.dp)
        )
        Spacer(modifier = Modifier.height(23.dp))
        MyPageItem(
            text = stringResource(id = R.string.mypage_adjust_sentence),
            onButtonClick = onAdjustSentenceClick
        )
        Spacer(modifier = Modifier.height(22.dp))
        MyPageItem(
            text = stringResource(id = R.string.mypage_reset_stamp),
            onButtonClick = onResetStampClick
        )
        Spacer(modifier = Modifier.height(27.dp))
    }
}

@Composable
private fun Etc(
    modifier: Modifier = Modifier,
    onLogoutClick: () -> Unit,
    onSignOutClick: () -> Unit
) {
    Column(
        modifier = modifier
            .padding(horizontal = 20.dp)
            .background(
                color = Gray900,
                shape = RoundedCornerShape(10.dp)
            )
    ) {
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = stringResource(id = R.string.mypage_etc_title),
            style = SoptTheme.typography.label12SB,
            color = Gray400,
            modifier = Modifier.padding(start = 16.dp)
        )
        Spacer(modifier = Modifier.height(23.dp))
        MyPageItem(
            text = stringResource(id = R.string.mypage_log_out),
            onButtonClick = onLogoutClick
        )
        Spacer(modifier = Modifier.height(22.dp))
        MyPageItem(
            text = stringResource(id = R.string.mypage_sign_out),
            onButtonClick = onSignOutClick
        )
        Spacer(modifier = Modifier.height(27.dp))
    }
}

@Composable
private fun EtcLogin(
    modifier: Modifier = Modifier,
    onLoginClick: () -> Unit,
) {
    Column(
        modifier = modifier
            .padding(horizontal = 20.dp)
            .background(
                color = Gray900,
                shape = RoundedCornerShape(10.dp)
            )
    ) {
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = stringResource(id = R.string.mypage_etc_title),
            style = SoptTheme.typography.label12SB,
            color = Gray400,
            modifier = Modifier.padding(start = 16.dp)
        )
        Spacer(modifier = Modifier.height(23.dp))
        MyPageItem(
            text = stringResource(id = R.string.mypage_log_in),
            onButtonClick = onLoginClick
        )
        Spacer(modifier = Modifier.height(27.dp))
    }
}