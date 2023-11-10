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
package org.sopt.official.feature.mypage.signOut

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.jakewharton.processphoenix.ProcessPhoenix
import com.jakewharton.rxbinding4.view.clicks
import dagger.hilt.android.AndroidEntryPoint
import io.reactivex.rxjava3.disposables.CompositeDisposable
import org.sopt.official.feature.mypage.databinding.ActivitySignOutBinding
import org.sopt.official.feature.mypage.util.rx.observeOnMain
import org.sopt.official.feature.mypage.util.rx.subscribeBy
import org.sopt.official.feature.mypage.util.rx.subscribeOnIo
import org.sopt.official.feature.mypage.util.ui.throttleUi
import org.sopt.official.feature.mypage.util.viewBinding

@AndroidEntryPoint
class SignOutActivity : AppCompatActivity() {
    private val binding by viewBinding(ActivitySignOutBinding::inflate)
    private val viewModel by viewModels<SignOutViewModel>()

    private val createDisposable = CompositeDisposable()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        initToolbar()
        initClick()
        initRestart()
    }

    private fun initToolbar() {
        binding.toolbar.setNavigationOnClickListener {
            this.onBackPressedDispatcher.onBackPressed()
        }
    }

    private fun initClick() {
        binding.confirmButton.clicks()
            .throttleUi()
            .observeOnMain()
            .onBackpressureLatest()
            .subscribeBy(
                createDisposable,
                onNext = {
                    viewModel.signOut()
                    this.finish()
                }
            )
    }

    private fun initRestart() {
        viewModel.restartSignal
            .distinctUntilChanged()
            .filter { it }
            .subscribeOnIo()
            .observeOnMain()
            .onBackpressureLatest()
            .subscribeBy(
                createDisposable,
                onNext = {
                    ProcessPhoenix.triggerRebirth(this)
                }
            )
    }

    override fun onDestroy() {
        super.onDestroy()

        createDisposable.dispose()
    }

    companion object {
        @JvmStatic
        fun getIntent(context: Context) = Intent(context, SignOutActivity::class.java)
    }
}
