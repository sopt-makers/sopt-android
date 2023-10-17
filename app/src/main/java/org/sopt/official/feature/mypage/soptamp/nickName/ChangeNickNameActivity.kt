/*
 * Copyright 2023 SOPT - Shout Our Passion Together
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.sopt.official.feature.mypage.soptamp.nickName

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.jakewharton.rxbinding4.view.clicks
import com.jakewharton.rxbinding4.widget.textChanges
import dagger.hilt.android.AndroidEntryPoint
import io.reactivex.rxjava3.core.BackpressureStrategy
import io.reactivex.rxjava3.disposables.CompositeDisposable
import org.sopt.official.R
import org.sopt.official.databinding.ActivityChangeNickNameBinding
import org.sopt.official.util.drawableOf
import org.sopt.official.util.rx.observeOnMain
import org.sopt.official.util.rx.subscribeBy
import org.sopt.official.util.rx.subscribeOnIo
import org.sopt.official.util.ui.setVisible
import org.sopt.official.util.ui.throttleUi
import org.sopt.official.util.viewBinding

@AndroidEntryPoint
class ChangeNickNameActivity : AppCompatActivity() {
    private val binding by viewBinding(ActivityChangeNickNameBinding::inflate)
    private val viewModel by viewModels<ChangeNickNameViewModel>()

    private val createDisposable = CompositeDisposable()

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
        viewModel.backPressedSignal
            .toFlowable(BackpressureStrategy.LATEST)
            .filter { it }
            .subscribeOnIo()
            .observeOnMain()
            .subscribeBy(
                createDisposable,
                onNext = {
                    this.onBackPressedDispatcher.onBackPressed()
                }
            )
        viewModel.isValidNickName
            .distinctUntilChanged()
            .map {
                val visible = !it
                val backgroundColor = drawableOf(
                    if (it) {
                        R.drawable.layout_edit_text_background
                    } else {
                        R.drawable.layout_edit_text_error_background
                    }
                )

                Pair(visible, backgroundColor)
            }
            .subscribeOnIo()
            .observeOnMain()
            .subscribeBy(
                createDisposable,
                onNext = { (visible, color) ->
                    binding.errorMessage.setVisible(visible)
                    binding.edittext.background = color
                }
            )
    }

    private fun initAction() {
        binding.confirmButton.clicks()
            .throttleUi()
            .onBackpressureLatest()
            .subscribeBy(
                createDisposable,
                onNext = {
                    viewModel.changeNickName()
                }
            )
        binding.edittext.textChanges()
            .throttleUi()
            .distinctUntilChanged()
            .map {
                it.toString() to it.isNotEmpty()
            }
            .observeOnMain()
            .onBackpressureLatest()
            .subscribeBy(
                createDisposable,
                onNext = { (text, isEnable) ->
                    viewModel.nickName.onNext(text)
                    binding.confirmButton.isEnabled = isEnable
                }
            )
    }

    companion object {
        @JvmStatic
        fun getIntent(context: Context) =
            Intent(context, ChangeNickNameActivity::class.java).apply {
            }
    }
}
