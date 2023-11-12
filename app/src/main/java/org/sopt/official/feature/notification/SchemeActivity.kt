package org.sopt.official.feature.notification

import android.app.TaskStackBuilder
import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import dagger.hilt.android.AndroidEntryPoint
import org.sopt.official.data.persistence.SoptDataStore
import org.sopt.official.domain.entity.auth.UserStatus
import org.sopt.official.feature.home.HomeActivity
import org.sopt.official.feature.notification.enums.DeepLinkType
import org.sopt.official.util.serializableExtra
import java.io.Serializable
import javax.inject.Inject

@AndroidEntryPoint
class SchemeActivity : AppCompatActivity() {
    @Inject
    lateinit var dataStore: SoptDataStore
    private val defaultDestination = DeepLinkType.NOTIFICATION_LIST.link
    private val args by serializableExtra(StartArgs(defaultDestination))

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        handleDeepLink()
    }

    private fun handleDeepLink() {
        val userStatus = UserStatus.of(dataStore.userStatus)
        val linkIntent = when (
            args?.link?.contains("http://") == true
            || args?.link?.contains("https://") == true
        ) {
            true -> Intent(Intent.ACTION_VIEW, Uri.parse(args!!.link))
            false -> {
                val deepLinkType = DeepLinkType.invoke(args?.link ?: defaultDestination)
                deepLinkType.getIntent(
                    this,
                    userStatus,
                    args?.link ?: defaultDestination
                )
            }
        }

        when (!isTaskRoot) {
            true -> startActivity(linkIntent)
            false -> TaskStackBuilder.create(this).apply {
                if (!isIntentToHome(linkIntent)) addNextIntentWithParentStack(
                    DeepLinkType.getHomeIntent(this@SchemeActivity, userStatus)
                )
                addNextIntent(linkIntent)
            }.startActivities()
        }
        finish()
    }

    private fun isIntentToHome(intent: Intent): Boolean {
        return when (intent.component?.className) {
            HomeActivity::class.java.name -> true
            else -> false
        }
    }

    data class StartArgs(
        val link: String
    ) : Serializable

    companion object {
        @JvmStatic
        fun getIntent(context: Context, args: StartArgs) =
            Intent(context, SchemeActivity::class.java).apply {
                putExtra("args", args)
            }
    }
}