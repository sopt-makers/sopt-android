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
package org.sopt.official.feature.mypage.soptamp.sentence

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.viewModels
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.flowWithLifecycle
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import org.sopt.official.common.view.toast
import org.sopt.official.designsystem.SoptTheme
import org.sopt.official.feature.mypage.R
import org.sopt.official.feature.mypage.component.MyPageButton
import org.sopt.official.feature.mypage.component.MyPageTextField
import org.sopt.official.feature.mypage.component.MyPageTopBar

@AndroidEntryPoint
class AdjustSentenceActivity : AppCompatActivity() {
    private val viewModel by viewModels<AdjustSentenceViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SoptTheme {
                val lifecycleOwner = LocalLifecycleOwner.current

                val sentence by viewModel.sentence.collectAsStateWithLifecycle()
                val isConfirmed by viewModel.isConfirmed.collectAsStateWithLifecycle(initialValue = false)

                val keyboardController = LocalSoftwareKeyboardController.current

                LaunchedEffect(viewModel.sideEffect, lifecycleOwner) {
                    viewModel.sideEffect.flowWithLifecycle(lifecycle = lifecycleOwner.lifecycle)
                        .collect { sideEffect ->
                            when (sideEffect) {
                                is AdjustSentenceSideEffect.NavigateToMyPage -> {
                                    keyboardController?.hide()
                                     delay(10)
                                    toast("한마디가 변경되었습니다")
                                    onBackPressedDispatcher.onBackPressed()
                                }
                            }
                        }
                }

                Scaffold(
                    modifier = Modifier
                        .background(SoptTheme.colors.background)
                        .fillMaxSize(),
                    topBar = {
                        MyPageTopBar(
                            title = "한 마디 편집",
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
                        Spacer(modifier = Modifier.height(20.dp))
                        MyPageTextField(
                            sentence = sentence,
                            modifier = Modifier.padding(horizontal = 20.dp),
                            onTextChange = { viewModel.onChange(it) },
                        )
                        Spacer(modifier = Modifier.height(32.dp))
                        MyPageButton(
                            paddingVertical = 16.dp,
                            modifier = Modifier
                                .padding(20.dp)
                                .fillMaxWidth(),
                            onClick = { viewModel.adjustSentence() },
                            isEnabled = isConfirmed
                        ) {
                            Text(
                                text = stringResource(R.string.adjust_sentence_button),
                                style = SoptTheme.typography.heading18B
                            )
                        }
                    }
                }
            }
        }
    }

    companion object {
        @JvmStatic
        fun getIntent(context: Context) = Intent(context, AdjustSentenceActivity::class.java)
    }
}
