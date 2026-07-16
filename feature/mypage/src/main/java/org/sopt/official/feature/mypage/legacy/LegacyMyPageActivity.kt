package org.sopt.official.feature.mypage.legacy

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.google.firebase.messaging.FirebaseMessaging
import dagger.hilt.android.AndroidEntryPoint
import io.github.takahirom.rin.rememberRetained
import javax.inject.Inject
import kotlinx.collections.immutable.persistentListOf
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import org.sopt.official.common.navigator.NavigatorProvider
import org.sopt.official.designsystem.SoptTheme
import org.sopt.official.domain.auth.repository.AuthRepository
import org.sopt.official.domain.notification.repository.NotificationRepository
import org.sopt.official.domain.soptamp.repository.StampRepository
import org.sopt.official.feature.mypage.component.MyPageSection
import org.sopt.official.feature.mypage.component.MyPageTopBar
import org.sopt.official.feature.mypage.di.authRepository
import org.sopt.official.feature.mypage.di.notificationRepository
import org.sopt.official.feature.mypage.di.stampRepository
import org.sopt.official.feature.mypage.model.MyPageDialogState
import org.sopt.official.feature.mypage.model.MyPageUiModel
import org.sopt.official.feature.mypage.soptamp.ui.AdjustSentenceActivity
import org.sopt.official.feature.mypage.web.WebUrlConstant
import org.sopt.official.model.UserStatus
import timber.log.Timber

/** Temporary fallback isolated for removal after the redesigned MyPage is enabled again. */
@AndroidEntryPoint
class LegacyMyPageActivity : AppCompatActivity() {
    @Inject
    lateinit var navigatorProvider: NavigatorProvider

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val userStatus = intent.getStringExtra(EXTRA_USER_STATUS)
            ?.let(UserStatus::of)
            ?: UserStatus.UNAUTHENTICATED

        setContent {
            SoptTheme {
                LegacyMyPageRoute(
                    userStatus = userStatus,
                    authRepository = authRepository,
                    stampRepository = stampRepository,
                    notificationRepository = notificationRepository,
                    onRestartApp = { startActivity(navigatorProvider.getAuthActivityIntent()) },
                    onBack = onBackPressedDispatcher::onBackPressed,
                )
            }
        }
    }

    companion object {
        private const val EXTRA_USER_STATUS = "legacy_mypage_user_status"

        fun getIntent(context: Context, userStatus: UserStatus): Intent =
            Intent(context, LegacyMyPageActivity::class.java)
                .putExtra(EXTRA_USER_STATUS, userStatus.name)
    }
}

@Composable
private fun LegacyMyPageRoute(
    userStatus: UserStatus,
    authRepository: AuthRepository,
    stampRepository: StampRepository,
    notificationRepository: NotificationRepository,
    onRestartApp: () -> Unit,
    onBack: () -> Unit,
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val scrollState = rememberScrollState()
    var dialogState by rememberRetained { mutableStateOf(MyPageDialogState.CLEAR) }

    val serviceSectionItems = remember {
        persistentListOf(
            MyPageUiModel.Header(title = "서비스 이용 방침"),
            MyPageUiModel.MyPageItem(title = "개인정보 처리 방침") {
                context.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(WebUrlConstant.NOTICE_PRIVATE_INFO)))
            },
            MyPageUiModel.MyPageItem(title = "서비스 이용약관") {
                context.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(WebUrlConstant.NOTICE_SERVICE_RULE)))
            },
            MyPageUiModel.MyPageItem(title = "의견 보내기") {
                context.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(WebUrlConstant.OPINION_KAKAO_CHAT)))
            },
        )
    }
    val notificationSectionItems = remember {
        persistentListOf(
            MyPageUiModel.Header(title = "알림 설정"),
            MyPageUiModel.MyPageItem(title = "알림 설정하기") {
                context.startActivity(
                    Intent(Settings.ACTION_APP_NOTIFICATION_SETTINGS)
                        .putExtra(Settings.EXTRA_APP_PACKAGE, context.packageName)
                )
            },
        )
    }
    val soptampSectionItems = remember {
        persistentListOf(
            MyPageUiModel.Header(title = "솝탬프 설정"),
            MyPageUiModel.MyPageItem(title = "한 마디 편집") {
                context.startActivity(AdjustSentenceActivity.getIntent(context))
            },
            MyPageUiModel.MyPageItem(title = "스탬프 초기화") {
                dialogState = MyPageDialogState.CLEAR_SOPTAMP
            },
        )
    }
    val etcSectionItems = remember {
        persistentListOf(
            MyPageUiModel.Header(title = "기타"),
            MyPageUiModel.MyPageItem(title = "로그아웃") {
                dialogState = MyPageDialogState.REQUEST_LOGOUT
            },
            MyPageUiModel.MyPageItem(title = "탈퇴하기") {
                context.startActivity(LegacySignOutActivity.getIntent(context))
            },
        )
    }
    val loginSectionItems = remember {
        persistentListOf(
            MyPageUiModel.Header(title = "기타"),
            MyPageUiModel.MyPageItem(title = "로그인", onItemClick = onRestartApp),
        )
    }

    Scaffold(
        modifier = Modifier
            .background(SoptTheme.colors.background)
            .fillMaxSize(),
        topBar = { MyPageTopBar(title = "마이페이지", onNavigationIconClick = onBack) },
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(SoptTheme.colors.background)
                .verticalScroll(scrollState),
        ) {
            Spacer(modifier = Modifier.height(20.dp))
            MyPageSection(items = serviceSectionItems)
            Spacer(modifier = Modifier.height(16.dp))
            when (userStatus) {
                UserStatus.ACTIVE, UserStatus.INACTIVE -> {
                    MyPageSection(items = notificationSectionItems)
                    Spacer(modifier = Modifier.height(16.dp))
                    MyPageSection(items = soptampSectionItems)
                    Spacer(modifier = Modifier.height(16.dp))
                    MyPageSection(items = etcSectionItems)
                }

                UserStatus.UNAUTHENTICATED -> MyPageSection(items = loginSectionItems)
            }
            Spacer(modifier = Modifier.height(32.dp))
        }

        when (dialogState) {
            MyPageDialogState.CLEAR_SOPTAMP -> LegacyMyPageDialog(
                onDismissRequest = { dialogState = MyPageDialogState.CLEAR },
                title = "미션을 초기화 하실건가요?",
                subTitle = "사진, 메모가 삭제되고\n전체 미션이 미완료상태로 초기화됩니다.",
                negativeText = "취소",
                positiveText = "초기화",
                onPositiveButtonClick = {
                    scope.launch {
                        stampRepository.deleteAllStamps()
                            .onSuccess { dialogState = MyPageDialogState.CLEAR }
                            .onFailure(Timber::e)
                    }
                },
            )

            MyPageDialogState.REQUEST_LOGOUT -> LegacyMyPageDialog(
                onDismissRequest = { dialogState = MyPageDialogState.CLEAR },
                title = "로그아웃",
                subTitle = "정말 로그아웃을 하실 건가요?",
                negativeText = "취소",
                positiveText = "로그아웃",
                onPositiveButtonClick = {
                    scope.launch {
                        runCatching {
                            notificationRepository.deleteToken(FirebaseMessaging.getInstance().token.await())
                        }.onFailure(Timber::e)
                        withContext(Dispatchers.IO) { authRepository.clearUserToken() }
                        onRestartApp()
                    }
                },
            )

            MyPageDialogState.CLEAR -> Unit
        }
    }
}
