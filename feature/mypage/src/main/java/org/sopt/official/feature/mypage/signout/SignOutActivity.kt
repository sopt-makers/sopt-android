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
package org.sopt.official.feature.mypage.signout

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.jakewharton.processphoenix.ProcessPhoenix
import dev.zacsweers.metro.ContributesIntoMap
import dev.zacsweers.metro.Inject
import dev.zacsweers.metro.binding
import dev.zacsweers.metrox.android.ActivityKey
import kotlinx.coroutines.launch
import org.sopt.official.common.di.AppScope
import org.sopt.official.designsystem.Gray300
import org.sopt.official.designsystem.SoptTheme
import org.sopt.official.designsystem.White
import org.sopt.official.domain.auth.repository.AuthRepository
import org.sopt.official.feature.mypage.R
import org.sopt.official.feature.mypage.component.MyPageButton
import org.sopt.official.feature.mypage.component.MyPageTopBar

@ContributesIntoMap(AppScope::class, binding<Activity>())
@ActivityKey(SignOutActivity::class)
class SignOutActivity @Inject constructor(
    private val authRepository: AuthRepository
) : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SoptTheme {
                val scope = rememberCoroutineScope()

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
                    ) {
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = stringResource(id = R.string.sign_out_title),
                            color = White,
                            style = SoptTheme.typography.heading16B,
                            modifier = Modifier.padding(start = 20.dp)
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = stringResource(id = R.string.sign_out_subtitle),
                            color = Gray300,
                            style = SoptTheme.typography.body14R,
                            modifier = Modifier.padding(horizontal = 20.dp)
                        )
                        Spacer(modifier = Modifier.weight(1f))
                        MyPageButton(
                            paddingVertical = 16.dp,
                            modifier = Modifier
                                .padding(20.dp)
                                .fillMaxWidth(),
                            onClick = {
                                scope.launch {
                                    //TODO: 탈퇴 API가 없어요?
//                                    authRepository.withdraw()
//                                        .onSuccess { ProcessPhoenix.triggerRebirth(this@SignOutActivity) }
//                                        .onFailure(Timber::e)
                                    authRepository.clearUserToken()
                                    ProcessPhoenix.triggerRebirth(this@SignOutActivity)
                                }
                            },
                        ) {
                            Text(
                                text = stringResource(R.string.sign_out_button),
                                style = SoptTheme.typography.heading18B
                            )
                        }
                        Spacer(modifier = Modifier.height(14.dp))
                    }
                }
            }
        }
    }

    companion object {
        @JvmStatic
        fun getIntent(context: Context) = Intent(context, SignOutActivity::class.java)
    }
}
