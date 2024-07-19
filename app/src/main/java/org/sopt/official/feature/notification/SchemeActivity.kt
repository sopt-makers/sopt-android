/*
 * MIT License
 * Copyright 2023-2024 SOPT - Shout Our Passion Together
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
package org.sopt.official.feature.notification

import android.app.TaskStackBuilder
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import dagger.hilt.android.AndroidEntryPoint
import java.io.Serializable
import javax.inject.Inject
import org.sopt.official.auth.model.UserStatus
import org.sopt.official.common.util.extractQueryParameter
import org.sopt.official.common.util.isExpiredDate
import org.sopt.official.common.util.serializableExtra
import org.sopt.official.feature.home.HomeActivity
import org.sopt.official.feature.notification.enums.DeepLinkType
import org.sopt.official.network.persistence.SoptDataStore
import timber.log.Timber

@AndroidEntryPoint
class SchemeActivity : AppCompatActivity() {
    @Inject
    lateinit var dataStore: SoptDataStore
    private val args by serializableExtra(StartArgs("", ""))

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        handleDeepLink()
    }

    private fun handleDeepLink() {
        val link = args?.link
        val linkIntent = when (link.isNullOrBlank()) {
            true -> NotificationDetailActivity.getIntent(
                this,
                NotificationDetailActivity.StartArgs(
                    notificationId = args?.notificationId ?: ""
                )
            )
            false -> checkLinkExpiration(link)
        }

        when (!isTaskRoot) {
            true -> startActivity(linkIntent)
            false -> TaskStackBuilder.create(this).apply {
                if (!isIntentToHome(linkIntent)) {
                    addNextIntentWithParentStack(
                        DeepLinkType.getHomeIntent(this@SchemeActivity, UserStatus.of(dataStore.userStatus))
                    )
                }
                addNextIntent(linkIntent)
            }.startActivities()
        }
        finish()
    }

    private fun checkLinkExpiration(link: String): Intent {
        return try {
            val expiredAt = link.extractQueryParameter("expiredAt")
            when (expiredAt.isExpiredDate()) {
                true -> DeepLinkType.getHomeIntent(
                    this,
                    UserStatus.of(dataStore.userStatus),
                    DeepLinkType.EXPIRED
                )
                else -> when (link.contains("http://") || link.contains("https://")) {
                    true -> Intent(Intent.ACTION_VIEW, Uri.parse(link))
                    false -> DeepLinkType.invoke(link).getIntent(
                        this,
                        UserStatus.of(dataStore.userStatus),
                        link
                    )
                }
            }
        } catch (exception: Exception) {
            Timber.e(exception)
            DeepLinkType.getHomeIntent(
                this,
                UserStatus.of(dataStore.userStatus),
                DeepLinkType.UNKNOWN
            )
        }
    }

    private fun isIntentToHome(intent: Intent): Boolean {
        return when (intent.component?.className) {
            HomeActivity::class.java.name -> true
            else -> false
        }
    }

    data class StartArgs(
        val notificationId: String,
        val link: String
    ) : Serializable

    companion object {
        @JvmStatic
        fun getIntent(context: Context, args: StartArgs) = Intent(context, SchemeActivity::class.java).apply {
            putExtra("args", args)
        }
    }
}
