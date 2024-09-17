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
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.core.widget.doAfterTextChanged
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import org.sopt.official.common.util.viewBinding
import org.sopt.official.designsystem.SoptTheme
import org.sopt.official.feature.mypage.R
import org.sopt.official.feature.mypage.component.MyPageButton
import org.sopt.official.feature.mypage.component.MyPageTextField
import org.sopt.official.feature.mypage.databinding.ActivityAdjustSentenceBinding

@AndroidEntryPoint
class AdjustSentenceActivity : AppCompatActivity() {
    private val binding by viewBinding(ActivityAdjustSentenceBinding::inflate)
    private val viewModel by viewModels<AdjustSentenceViewModel>()

    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SoptTheme {
                Scaffold(modifier = Modifier
                    .background(SoptTheme.colors.background)
                    .fillMaxSize(),
                    topBar = {
                        CenterAlignedTopAppBar(
                            title = {
                                Text(
                                    text = stringResource(id = R.string.toolbar_adjust_sentence),
                                    style = SoptTheme.typography.body16M
                                )
                            },
                            navigationIcon = {
                                IconButton(onClick = { onBackPressedDispatcher.onBackPressed() }) {
                                    Icon(
                                        painterResource(R.drawable.btn_arrow_left),
                                        contentDescription = null,
                                        tint = SoptTheme.colors.onBackground
                                    )
                                }
                            },
                            colors = TopAppBarDefaults.topAppBarColors(
                                containerColor = SoptTheme.colors.background,
                                titleContentColor = SoptTheme.colors.onBackground,
                                actionIconContentColor = SoptTheme.colors.primary
                            )
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
                        MyPageTextField(modifier = Modifier.padding(horizontal = 20.dp))
                        Spacer(modifier = Modifier.height(52.dp))
                        MyPageButton(
                            paddingVertical = 16.dp,
                            style = SoptTheme.typography.body14R,
                            paddingShape = 10.dp,
                            modifier = Modifier.padding(20.dp),
                            onButtonClick = { viewModel.adjustSentence() },
                            text = R.string.adjust_sentence_button,
                            isEnabled = false
                        )
                    }
                }
            }
        }
    }

    private fun initView() {
        viewModel.finish
            .flowWithLifecycle(lifecycle)
            .onEach {
                this.onBackPressedDispatcher.onBackPressed()
            }.launchIn(lifecycleScope)

        binding.edittext.doAfterTextChanged {
            viewModel.onChange(it.toString())
        }

        viewModel.isConfirmed
            .flowWithLifecycle(lifecycle)
            .onEach {
                binding.confirmButton.isEnabled = it
            }.launchIn(lifecycleScope)
    }

    companion object {
        @JvmStatic
        fun getIntent(context: Context) = Intent(context, AdjustSentenceActivity::class.java)
    }
}
