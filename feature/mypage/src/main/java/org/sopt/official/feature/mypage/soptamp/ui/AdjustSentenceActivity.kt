/*
 * MIT License
 * Copyright 2023-2026 SOPT - Shout Our Passion Together
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
package org.sopt.official.feature.mypage.soptamp.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.input.TextFieldLineLimits
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import dagger.hilt.android.AndroidEntryPoint
import java.io.Serializable
import javax.inject.Inject
import org.sopt.official.analytics.Tracker
import org.sopt.official.analytics.compose.LocalTracker
import org.sopt.official.analytics.compose.ProvideTracker
import org.sopt.official.analytics.trackViewType
import org.sopt.official.common.util.serializableExtra
import org.sopt.official.common.view.toast
import org.sopt.official.feature.mypage.MypageAnalyticsEvent
import org.sopt.official.feature.mypage.component.MyPageTopBar
import org.sopt.official.feature.mypage.di.userRepository
import org.sopt.official.feature.mypage.soptamp.state.rememberModifyProfileState
import org.sopt.official.mds.components.button.MdsActionButton
import org.sopt.official.mds.components.button.MdsActionButtonSize
import org.sopt.official.mds.components.button.MdsActionButtonType
import org.sopt.official.mds.components.input.MdsTextArea
import org.sopt.official.mds.theme.SoptTheme
import org.sopt.official.model.UserStatus
import org.sopt.official.model.toViewType

@AndroidEntryPoint
class AdjustSentenceActivity : AppCompatActivity() {

    @Inject
    lateinit var analyticsTracker: Tracker

    private val args by serializableExtra(StartArgs(UserStatus.UNAUTHENTICATED.name))

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val userStatus = args?.userStatus?.let { UserStatus.of(it) } ?: UserStatus.UNAUTHENTICATED

        setContent {
            SoptTheme {
                ProvideTracker(analyticsTracker) {
                    val context = LocalContext.current
                    val tracker = LocalTracker.current
                    val viewType = userStatus.toViewType()

                    val uiState = rememberModifyProfileState(
                        userRepository = userRepository,
                        onShowToast = { context.toast(it) }
                    )

                    Scaffold(
                        modifier = Modifier.fillMaxSize(),
                        topBar = {
                            MyPageTopBar(
                                title = "한 마디 편집",
                                onNavigationIconClick = { onBackPressedDispatcher.onBackPressed() }
                            )
                        },
                        containerColor = SoptTheme.colors.bg.layer.basement
                    ) { innerPadding ->
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(innerPadding)
                                .padding(vertical = 16.dp)
                        ) {
                            MdsTextArea(
                                state = uiState.current,
                                placeholder = "설정된 한 마디가 없습니다.",
                                inputLimits = TextFieldLineLimits.MultiLine(2, 2),
                                modifier = Modifier.padding(horizontal = 20.dp)
                            )

                            Spacer(modifier = Modifier.height(44.dp))

                            MdsActionButton(
                                text = "저장",
                                type = MdsActionButtonType.PRIMARY,
                                size = MdsActionButtonSize.LARGE,
                                enabled = uiState.isConfirmed,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 12.dp)
                            ) {
                                tracker.trackViewType(MypageAnalyticsEvent.CLICK_DONE_EDIT_STATUSMESSAGE, viewType)
                                uiState.onUpdate()
                            }
                        }
                    }
                }
            }
        }
    }

    data class StartArgs(
        val userStatus: String,
    ) : Serializable

    companion object {
        @JvmStatic
        fun getIntent(context: Context, args: StartArgs) = Intent(context, AdjustSentenceActivity::class.java).apply {
            putExtra("args", args)
        }
    }
}
