package org.sopt.official.feature.main

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import dagger.hilt.android.AndroidEntryPoint
import org.sopt.official.auth.model.UserStatus
import org.sopt.official.common.navigator.DeepLinkType
import org.sopt.official.designsystem.SoptTheme
import java.io.Serializable

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val startArgs = intent.getSerializableExtra(ARGS) as? StartArgs

        // TODO: HomeActivity에 있는 deepLinkType 처리 로직을 참고하여 구현 필요
        setContent {
            SoptTheme {
                MainScreen(
                    userStatus = startArgs?.userStatus ?: UserStatus.UNAUTHENTICATED,
                )
            }
        }
    }

    data class StartArgs(
        val userStatus: UserStatus,
        val deepLinkType: DeepLinkType? = null,
    ) : Serializable

    companion object {
        private const val ARGS = "args"
        @JvmStatic
        fun getIntent(context: Context, args: StartArgs) = Intent(context, MainActivity::class.java).apply {
            putExtra(ARGS, args)
            addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
        }
    }
}
