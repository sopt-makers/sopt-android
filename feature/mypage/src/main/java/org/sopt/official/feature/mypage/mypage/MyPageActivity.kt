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
package org.sopt.official.feature.mypage.mypage

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.jakewharton.processphoenix.ProcessPhoenix
import com.jakewharton.rxbinding4.view.clicks
import dagger.hilt.android.AndroidEntryPoint
import domain.model.UserActiveState
import io.reactivex.rxjava3.core.BackpressureStrategy
import io.reactivex.rxjava3.disposables.CompositeDisposable
import org.sopt.official.feature.mypage.AlertDialogPositiveNegative
import org.sopt.official.feature.mypage.R
import org.sopt.official.feature.mypage.databinding.ActivityMyPageBinding
import org.sopt.official.feature.mypage.model.MyPageUiState
import org.sopt.official.feature.mypage.soptamp.nickName.ChangeNickNameActivity
import org.sopt.official.feature.mypage.soptamp.sentence.AdjustSentenceActivity
import org.sopt.official.feature.mypage.signOut.SignOutActivity
import org.sopt.official.feature.mypage.util.ui.serializableExtra
import org.sopt.official.feature.mypage.web.WebUrlConstant
import org.sopt.official.feature.mypage.util.rx.observeOnMain
import org.sopt.official.feature.mypage.util.rx.subscribeBy
import org.sopt.official.feature.mypage.util.rx.subscribeOnIo
import org.sopt.official.feature.mypage.util.ui.setVisible
import org.sopt.official.feature.mypage.util.ui.throttleUi
import org.sopt.official.feature.mypage.util.viewBinding
import java.io.Serializable

@AndroidEntryPoint
class MyPageActivity : AppCompatActivity() {
    private val binding by viewBinding(ActivityMyPageBinding::inflate)
    private val viewModel by viewModels<MyPageViewModel>()
    private val args by serializableExtra(StartArgs(UserActiveState.UNAUTHENTICATED))

    private val createDisposable = CompositeDisposable()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        initStartArgs()
        initToolbar()
        initView()
        initClick()
        initRestart()

        initNotificationSettingClickListener()
    }

    private fun initStartArgs() {
        args?.userActiveState?.let {
            viewModel.userActiveState.onNext(MyPageUiState.User(it))
        }
    }

    private fun initToolbar() {
        binding.includeAppBarBackArrow.apply {
            textViewTitle.text = getString(R.string.toolbar_mypage)
            toolbar.clicks()
                .throttleUi()
                .observeOnMain()
                .onBackpressureLatest()
                .subscribeBy(
                    createDisposable,
                    onNext = {
                        onBackPressedDispatcher.onBackPressed()
                    }
                )
        }
    }

    private fun initView() {
        viewModel.userActiveState
            .distinctUntilChanged()
            .filter { it is MyPageUiState.User }
            .map { (it as MyPageUiState.User).activeState != UserActiveState.UNAUTHENTICATED }
            .subscribeOnIo()
            .observeOnMain()
            .onBackpressureLatest()
            .subscribeBy(
                createDisposable,
                onNext = { isAuthenticated ->
                    binding.containerNotificationSetting.setVisible(isAuthenticated)
                    binding.containerSoptampInfo.setVisible(isAuthenticated)
                    binding.textLogIn.setVisible(!isAuthenticated)
                    binding.iconLogIn.setVisible(!isAuthenticated)
                    binding.textLogOut.setVisible(isAuthenticated)
                    binding.iconLogOut.setVisible(isAuthenticated)
                    binding.textSignOut.setVisible(isAuthenticated)
                    binding.iconSignOut.setVisible(isAuthenticated)
                }
            )
    }

    private fun initClick() {
        binding.iconPrivateInfo.clicks()
            .throttleUi()
            .observeOnMain()
            .onBackpressureLatest()
            .subscribeBy(
                createDisposable,
                onNext = {
                    this.startActivity(
                        Intent(Intent.ACTION_VIEW, Uri.parse(WebUrlConstant.NOTICE_PRIVATE_INFO))
                    )
                }
            )

        binding.iconServiceRule.clicks()
            .throttleUi()
            .observeOnMain()
            .onBackpressureLatest()
            .subscribeBy(
                createDisposable,
                onNext = {
                    this.startActivity(
                        Intent(Intent.ACTION_VIEW, Uri.parse(WebUrlConstant.NOTICE_SERVICE_RULE))
                    )
                }
            )
        binding.iconSendOpinion.clicks()
            .throttleUi()
            .observeOnMain()
            .onBackpressureLatest()
            .subscribeBy(
                createDisposable,
                onNext = {
                    this.startActivity(
                        Intent(Intent.ACTION_VIEW, Uri.parse(WebUrlConstant.OPINION_GOOGLE_FORM))
                    )
                }
            )
        binding.iconAdjustSentence.clicks()
            .throttleUi()
            .observeOnMain()
            .onBackpressureLatest()
            .subscribeBy(
                createDisposable,
                onNext = {
                    this.startActivity(AdjustSentenceActivity.getIntent(this))
                }
            )
        binding.iconChangeNickname.clicks()
            .throttleUi()
            .observeOnMain()
            .onBackpressureLatest()
            .subscribeBy(
                createDisposable,
                onNext = {
                    this.startActivity(ChangeNickNameActivity.getIntent(this))
                }
            )
        binding.iconResetStamp.clicks()
            .throttleUi()
            .observeOnMain()
            .onBackpressureLatest()
            .subscribeBy(
                createDisposable,
                onNext = {
                    AlertDialogPositiveNegative(this)
                        .setTitle(R.string.mypage_alert_soptamp_reset_title)
                        .setSubtitle(R.string.mypage_alert_soptamp_reset_subtitle)
                        .setPositiveButton(R.string.mypage_alert_soptamp_reset_positive) {
                            viewModel.resetSoptamp()
                        }
                        .setNegativeButton(R.string.mypage_alert_soptamp_reset_negative)
                        .show()
                }
            )
        binding.iconLogOut.clicks()
            .throttleUi()
            .observeOnMain()
            .onBackpressureLatest()
            .subscribeBy(
                createDisposable,
                onNext = {
                    AlertDialogPositiveNegative(this)
                        .setTitle(R.string.mypage_alert_log_out_title)
                        .setSubtitle(R.string.mypage_alert_log_out_subtitle)
                        .setPositiveButton(R.string.mypage_alert_log_out_positive) {
                            viewModel.logOut()
                        }
                        .setNegativeButton(R.string.mypage_alert_log_out_negative)
                        .show()
                }
            )
        binding.iconSignOut.clicks()
            .throttleUi()
            .observeOnMain()
            .onBackpressureLatest()
            .subscribeBy(
                createDisposable,
                onNext = {
                    this.startActivity(SignOutActivity.getIntent(this))
                }
            )
        binding.iconLogIn.clicks()
            .throttleUi()
            .observeOnMain()
            .onBackpressureLatest()
            .subscribeBy(
                createDisposable,
                onNext = {
                    setResult(ResultCode.LOG_IN.ordinal)
                    onBackPressedDispatcher.onBackPressed()
                }
            )
    }

    private fun initRestart() {
        viewModel.restartSignal.toFlowable(BackpressureStrategy.LATEST)
            .distinctUntilChanged()
            .filter { it }
            .subscribeOnIo()
            .observeOnMain()
            .onBackpressureLatest()
            .subscribeBy(
                createDisposable,
                onNext = {
                    val intent = packageManager.getLaunchIntentForPackage(packageName)
                    val componentName = intent?.component
                    val mainIntent = Intent.makeRestartActivityTask(componentName)
                    ProcessPhoenix.triggerRebirth(this, mainIntent)
                }
            )
    }

    private fun initNotificationSettingClickListener() {
        binding.linearLayoutNotificationSettingContainer.setOnClickListener {
            Intent().apply {
                action = Settings.ACTION_APP_NOTIFICATION_SETTINGS
                putExtra(Settings.EXTRA_APP_PACKAGE, packageName)
                startActivity(this)
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()

        createDisposable.dispose()
    }

    enum class ResultCode {
        LOG_IN,
    }

    data class StartArgs(
        val userActiveState: UserActiveState
    ) : Serializable

    companion object {
        @JvmStatic
        fun getIntent(context: Context, args: StartArgs) =
            Intent(context, MyPageActivity::class.java).apply {
                putExtra("args", args)
            }
    }
}
