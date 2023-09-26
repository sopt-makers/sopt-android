package org.sopt.official.feature.mypage

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.jakewharton.processphoenix.ProcessPhoenix
import com.jakewharton.rxbinding4.view.clicks
import dagger.hilt.android.AndroidEntryPoint
import io.reactivex.rxjava3.core.BackpressureStrategy
import io.reactivex.rxjava3.disposables.CompositeDisposable
import org.sopt.official.R
import org.sopt.official.databinding.ActivityMyPageBinding
import org.sopt.official.designsystem.AlertDialogPositiveNegative
import org.sopt.official.domain.entity.UserActiveState
import org.sopt.official.feature.mypage.model.MyPageUiState
import org.sopt.official.feature.mypage.signOut.SignOutActivity
import org.sopt.official.feature.mypage.soptamp.nickName.ChangeNickNameActivity
import org.sopt.official.feature.mypage.soptamp.sentence.AdjustSentenceActivity
import org.sopt.official.feature.web.WebUrlConstant
import org.sopt.official.util.rx.observeOnMain
import org.sopt.official.util.rx.subscribeBy
import org.sopt.official.util.rx.subscribeOnIo
import org.sopt.official.util.serializableExtra
import org.sopt.official.util.setOnSingleClickListener
import org.sopt.official.util.ui.setVisible
import org.sopt.official.util.ui.throttleUi
import org.sopt.official.util.viewBinding
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
    }

    private fun initStartArgs() {
        args?.userActiveState?.let {
            viewModel.userActiveState.onNext(MyPageUiState.User(it))
        }
    }

    private fun initToolbar() {
        binding.icBack.setOnSingleClickListener {
            onBackPressedDispatcher.onBackPressed()
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
                onNext = {
                    binding.containerSoptampInfo.setVisible(it)
                    binding.textLogIn.setVisible(!it)
                    binding.iconLogIn.setVisible(!it)
                    binding.textLogOut.setVisible(it)
                    binding.iconLogOut.setVisible(it)
                    binding.textSignOut.setVisible(it)
                    binding.iconSignOut.setVisible(it)
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