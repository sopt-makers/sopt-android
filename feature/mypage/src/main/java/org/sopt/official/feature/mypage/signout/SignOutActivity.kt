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
package org.sopt.official.feature.mypage.signout

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.jakewharton.processphoenix.ProcessPhoenix
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import org.sopt.official.feature.mypage.component.MyPageTopBar
import org.sopt.official.feature.mypage.di.authRepository
import org.sopt.official.feature.mypage.di.soptUserRepository
import org.sopt.official.mds.components.button.MdsActionButton
import org.sopt.official.mds.components.button.MdsActionButtonSize
import org.sopt.official.mds.components.button.MdsActionButtonType
import org.sopt.official.mds.theme.SoptTheme
import timber.log.Timber

@AndroidEntryPoint
class SignOutActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SoptTheme {
                val uriHandler = LocalUriHandler.current
                val scope = rememberCoroutineScope()

                SignOutScreen(
                    onNavigationIconClick = {
                        onBackPressedDispatcher.onBackPressed()
                    },
                    onWithDraw = {
                        scope.launch {
                            soptUserRepository.withdraw()
                                .onSuccess {
                                    authRepository.clearUserToken()
                                    uriHandler.openUri(it.withdrawFormUrl)
                                    ProcessPhoenix.triggerRebirth(this@SignOutActivity)
                                }
                                .onFailure { Timber.e(it) }
                        }
                    }
                )
            }
        }
    }

    companion object {
        @JvmStatic
        fun getIntent(context: Context) = Intent(context, SignOutActivity::class.java)
    }
}

@Composable
private fun SignOutScreen(
    onNavigationIconClick: () -> Unit,
    onWithDraw: () -> Unit,
    modifier: Modifier = Modifier
) {
    Scaffold(
        modifier = modifier
            .fillMaxSize(),
        topBar = {
            MyPageTopBar(
                title = "탈퇴하기",
                onNavigationIconClick = onNavigationIconClick
            )
        },
        containerColor = SoptTheme.colors.bg.layer.basement
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 20.dp)
                .padding(top = 16.dp, bottom = 28.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            SignOutWithdrawInfo()

            MdsActionButton(
                text = "탈퇴하기",
                type = MdsActionButtonType.DANGER,
                size = MdsActionButtonSize.LARGE,
                onClick = onWithDraw
            )
        }
    }
}

@Preview
@Composable
private fun SignOutScreenPreview() {
    SoptTheme {
        SignOutScreen(
            onNavigationIconClick = {},
            onWithDraw = {}
        )
    }
}
