/*
 * MIT License
 * Copyright 2023 SOPT - Shout Our Passion Together
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
package org.sopt.official.feature.mypage.soptamp.nickName

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.core.widget.doAfterTextChanged
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import org.sopt.official.common.util.drawableOf
import org.sopt.official.common.util.setOnSingleClickListener
import org.sopt.official.common.util.viewBinding
import org.sopt.official.feature.mypage.R
import org.sopt.official.feature.mypage.databinding.ActivityChangeNickNameBinding

@AndroidEntryPoint
class ChangeNickNameActivity : AppCompatActivity() {
    private val binding by viewBinding(ActivityChangeNickNameBinding::inflate)
    private val viewModel by viewModels<ChangeNickNameViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        initToolbar()
        initView()
        initAction()
    }

    private fun initToolbar() {
        binding.toolbar.setNavigationOnClickListener {
            this.onBackPressedDispatcher.onBackPressed()
        }
    }

    private fun initView() {
        viewModel.finish
            .flowWithLifecycle(lifecycle)
            .onEach {
                this.onBackPressedDispatcher.onBackPressed()
            }.launchIn(lifecycleScope)
        viewModel.isValidNickName
            .flowWithLifecycle(lifecycle)
            .onEach {
                binding.errorMessage.isVisible = !it
                binding.edittext.background = drawableOf(
                    if (it) {
                        R.drawable.layout_edit_text_background
                    } else {
                        R.drawable.layout_edit_text_error_background
                    }
                )
            }
        viewModel.isConfirmEnabled
            .flowWithLifecycle(lifecycle)
            .onEach {
                binding.confirmButton.isEnabled = it
            }.launchIn(lifecycleScope)
    }

    private fun initAction() {
        binding.confirmButton.setOnSingleClickListener {
            viewModel.changeNickName()
        }
        binding.edittext.doAfterTextChanged {
            viewModel.onChangeNickName(it.toString())
        }
    }

    companion object {
        @JvmStatic
        fun getIntent(context: Context) = Intent(context, ChangeNickNameActivity::class.java)
    }
}
