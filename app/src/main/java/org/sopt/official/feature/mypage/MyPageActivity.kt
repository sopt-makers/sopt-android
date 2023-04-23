package org.sopt.official.feature.mypage

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.material.AlertDialog
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import org.sopt.official.R
import org.sopt.official.databinding.ActivityMyPageBinding
import org.sopt.official.designsystem.AlertDialogPositiveNegative
import org.sopt.official.domain.entity.UserState
import org.sopt.official.domain.entity.auth.UserStatus
import org.sopt.official.feature.main.MainActivity
import org.sopt.official.feature.mypage.rule.PrivateInfoActivity
import org.sopt.official.feature.mypage.rule.ServiceRuleActivity
import org.sopt.official.feature.mypage.signOut.SignOutActivity
import org.sopt.official.feature.mypage.soptamp.nickName.ChangeNickNameActivity
import org.sopt.official.feature.mypage.soptamp.sentence.AdjustSentenceActivity
import org.sopt.official.util.serializableExtra
import org.sopt.official.util.ui.setVisible
import org.sopt.official.util.viewBinding
import org.sopt.official.util.wrapper.asNullableWrapper
import java.io.Serializable
import java.util.*

@AndroidEntryPoint
class MyPageActivity : AppCompatActivity() {
    private val binding by viewBinding(ActivityMyPageBinding::inflate)
    private val viewModel by viewModels<MyPageViewModel>()
    private val args by serializableExtra(MyPageActivity.StartArgs(UserState.UNAUTHENTICATED))

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        initStartArgs()
        initToolbar()
        initView()
        initClick()
    }

    private fun initStartArgs() {
        viewModel.userState.value = args?.userState.asNullableWrapper()
    }

    private fun initToolbar() {
        binding.toolbar.setNavigationOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }
    }

    private fun initView() {
        viewModel.userState
            .flowWithLifecycle(lifecycle)
            .onEach {
                val isMember = !(it.get() == UserState.UNAUTHENTICATED)
                binding.containerSoptampInfo.setVisible(isMember)
                binding.textLogIn.setVisible(!isMember)
                binding.iconLogIn.setVisible(!isMember)
                binding.textLogOut.setVisible(isMember)
                binding.iconLogOut.setVisible(isMember)
                binding.textSignOut.setVisible(isMember)
                binding.iconSignOut.setVisible(isMember)
            }
            .launchIn(lifecycleScope)
    }

    private fun initClick() {
        binding.iconPrivateInfo.setOnClickListener {
            this.startActivity(PrivateInfoActivity.getIntent(this))
        }
        binding.iconServiceRule.setOnClickListener {
            this.startActivity(ServiceRuleActivity.getIntent(this))
        }
        binding.iconSendOpinion.setOnClickListener {
            // 구글폼 이동 (아직 url X)
        }
        binding.iconAdjustSentence.setOnClickListener {
            this.startActivity(AdjustSentenceActivity.getIntent(this))
        }
        binding.iconChangeNickname.setOnClickListener {
            this.startActivity(ChangeNickNameActivity.getIntent(this))
        }
        binding.iconResetStamp.setOnClickListener {
            AlertDialogPositiveNegative(this)
                .setTitle(R.string.mypage_alert_soptamp_reset_title)
                .setSubtitle(R.string.mypage_alert_soptamp_reset_subtitle)
                .setPositiveButton(R.string.mypage_alert_soptamp_reset_positive) {
                    viewModel.resetSoptamp()
                }
                .setNegativeButton(R.string.mypage_alert_soptamp_reset_negative)
                .show()
        }
        binding.iconLogOut.setOnClickListener {
            AlertDialogPositiveNegative(this)
                .setTitle(R.string.mypage_alert_log_out_title)
                .setSubtitle(R.string.mypage_alert_log_out_subtitle)
                .setPositiveButton(R.string.mypage_alert_log_out_positive) {
                    viewModel.logOut()
                }
                .setNegativeButton(R.string.mypage_alert_log_out_negative)
                .show()
        }
        binding.iconSignOut.setOnClickListener {
            this.startActivity(SignOutActivity.getIntent(this))
        }
        binding.iconLogIn.setOnClickListener {
            setResult(ResultCode.LOG_IN.ordinal)
            onBackPressedDispatcher.onBackPressed()
            // Main 에서 requestActivityForResult 필요
        }
    }

    enum class ResultCode {
        LOG_IN,
    }

    data class StartArgs(
        val userState: UserState
    ) : Serializable

    companion object {
        @JvmStatic
        fun getIntent(context: Context, args: MyPageActivity.StartArgs) = Intent(context, MyPageActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            putExtra("args", args)
        }
    }
}