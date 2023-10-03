package org.sopt.official.feature.auth

import android.animation.ObjectAnimator
import android.content.Context
import android.content.Intent
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Intent
import android.graphics.Paint
import android.os.Bundle
import android.view.animation.AnimationUtils
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import androidx.core.view.isVisible
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import org.sopt.official.BuildConfig
import org.sopt.official.R
import org.sopt.official.auth.PlaygroundAuth
import org.sopt.official.auth.data.PlaygroundAuthDatasource
import org.sopt.official.auth.data.remote.model.response.OAuthToken
import org.sopt.official.config.messaging.SoptFirebaseMessagingService.Companion.REMOTE_MESSAGE_EVENT_LINK
import org.sopt.official.config.messaging.SoptFirebaseMessagingService.Companion.REMOTE_MESSAGE_EVENT_TYPE
import org.sopt.official.common.di.Auth
import org.sopt.official.data.model.request.AuthRequest
import org.sopt.official.data.persistence.SoptDataStore
import org.sopt.official.data.service.AuthService
import org.sopt.official.databinding.ActivityAuthBinding
import org.sopt.official.domain.entity.auth.UserStatus
import org.sopt.official.feature.home.HomeActivity
import org.sopt.official.util.dp
import org.sopt.official.util.setOnAnimationEndListener
import org.sopt.official.util.setOnSingleClickListener
import org.sopt.official.util.toEntity
import org.sopt.official.util.viewBinding
import javax.inject.Inject

@AndroidEntryPoint
class AuthActivity : AppCompatActivity() {
    private val binding by viewBinding(ActivityAuthBinding::inflate)
    private val viewModel by viewModels<AuthViewModel>()

    @Auth
    @Inject
    lateinit var authService: AuthService

    @Inject
    lateinit var dataStore: SoptDataStore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (dataStore.accessToken.isNotEmpty()) {
            onNewIntent(intent)
        }
        setContentView(binding.root)
        initNotificationChannel()

        initUi()
        initAnimation()
        collectUiEvent()
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)

        intent?.let {
            val remoteMessageEventType = it.getStringExtra(REMOTE_MESSAGE_EVENT_TYPE) ?: ""
            val remoteMessageEventLink = it.getStringExtra(REMOTE_MESSAGE_EVENT_LINK) ?: ""

            if (
                dataStore.userStatus.isNotBlank()
                && dataStore.userStatus != UserStatus.UNAUTHENTICATED.name
                && remoteMessageEventType.isNotBlank()
            ) {
                startActivity(HomeActivity.getIntent(this,
                    HomeActivity.StartArgs(
                        UserStatus.of(dataStore.userStatus),
                        remoteMessageEventType,
                        remoteMessageEventLink
                    )
                ))
            }
        }
    }

    private fun initNotificationChannel() {
        NotificationChannel(
            getString(R.string.toolbar_notification_filter_all),
            getString(R.string.toolbar_notification_filter_all),
            NotificationManager.IMPORTANCE_HIGH
        ).apply {
            setSound(null, null)
            enableLights(false)
            enableVibration(false)
            lockscreenVisibility = NotificationCompat.VISIBILITY_PUBLIC
            (getSystemService(NOTIFICATION_SERVICE) as NotificationManager).createNotificationChannel(this)
        }
    }

    private fun collectUiEvent() {
        viewModel.uiEvent
            .flowWithLifecycle(lifecycle)
            .onEach { event ->
                val remoteMessageEventType = intent.getStringExtra(REMOTE_MESSAGE_EVENT_TYPE) ?: ""
                val remoteMessageEventLink = intent.getStringExtra(REMOTE_MESSAGE_EVENT_LINK) ?: ""
                when (event) {
                    is AuthUiEvent.Success -> {
                        startActivity(HomeActivity.getIntent(this,
                            HomeActivity.StartArgs(
                                event.userStatus,
                                remoteMessageEventType,
                                remoteMessageEventLink
                            )
                        ))
                    }

                    is AuthUiEvent.Failure -> {
                        startActivity(HomeActivity.getIntent(this, HomeActivity.StartArgs(UserStatus.UNAUTHENTICATED)))
                    }
                }
            }.launchIn(lifecycleScope)
    }

    private fun initAnimation() {
        val fadeInAnimation = AnimationUtils.loadAnimation(this, R.anim.anim_fade_in).apply {
            startOffset = 700
        }
        fadeInAnimation.setOnAnimationEndListener {
            binding.groupBottomAuth.isVisible = true
        }
        ObjectAnimator.ofFloat(
            binding.imgSoptLogo,
            "translationY",
            -140.dp.toFloat()
        ).apply {
            duration = 1000
            startDelay = 700
            interpolator = AnimationUtils.loadInterpolator(
                this@AuthActivity,
                android.R.interpolator.fast_out_slow_in
            )
        }.start()
        binding.groupBottomAuth.startAnimation(fadeInAnimation)
    }

    private fun initUi() {
        binding.btnSoptNotMember.paintFlags = Paint.UNDERLINE_TEXT_FLAG
        binding.btnSoptLogin.setOnSingleClickListener {
            PlaygroundAuth.authorizeWithWebTab(
                context = this,
                isDebug = BuildConfig.DEBUG,
                authDataSource = object : PlaygroundAuthDatasource {
                    override suspend fun oauth(code: String): Result<OAuthToken> {
                        return kotlin.runCatching {
                            authService
                                .authenticate(AuthRequest(code, dataStore.pushToken))
                                .toOAuthToken()
                        }
                    }
                }
            ) {
                it.onSuccess { token ->
                    lifecycleScope.launch {
                        viewModel.onLogin(token.toEntity())
                    }
                }.onFailure {
                    lifecycleScope.launch {
                        viewModel.onFailure(it)
                    }
                }
            }
        }
        binding.btnSoptNotMember.setOnSingleClickListener {
            startActivity(HomeActivity.getIntent(this, HomeActivity.StartArgs(
                UserStatus.UNAUTHENTICATED
            )))
        }
    }

    companion object {
        @JvmStatic
        fun newInstance(context: Context) = Intent(context, AuthActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
    }
}
