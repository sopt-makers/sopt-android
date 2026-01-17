/*
 * MIT License
 * Copyright 2023-2025 SOPT - Shout Our Passion Together
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
import dev.zacsweers.metro.Inject
import org.sopt.official.common.navigator.DeepLinkType
import org.sopt.official.common.util.extractQueryParameter
import org.sopt.official.common.util.isExpiredDate
import org.sopt.official.common.util.serializableExtra
import org.sopt.official.feature.notification.detail.NotificationDetailActivity
import org.sopt.official.model.UserStatus
import org.sopt.official.network.persistence.SoptDataStore
import timber.log.Timber
import java.io.Serializable

class SchemeActivity(
    private val dataStore: SoptDataStore
) : AppCompatActivity() {
    private val args by serializableExtra(Argument("", ""))

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        handleDeepLink()
    }

    private fun handleDeepLink() {
        val link = args?.link
        val linkIntent = if (link.isNullOrBlank()) {
            NotificationDetailActivity.getIntent(
                this,
                args?.notificationId.orEmpty()
            )
        } else {
            checkLinkExpiration(link)
        }

        when (!isTaskRoot) {
            true -> startActivity(linkIntent)
            false -> TaskStackBuilder.create(this).apply {
                if (!isIntentToHome()) {
                    addNextIntentWithParentStack(
                        DeepLinkType.getIntent(UserStatus.of(dataStore.userStatus))
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
                true -> DeepLinkType.getIntent(
                    UserStatus.of(dataStore.userStatus),
                    DeepLinkType.EXPIRED
                )

                else -> when (link.contains("http://") || link.contains("https://")) {
                    true -> Intent(
                        Intent.ACTION_VIEW,
                        Uri.parse(link)
                    )

                    false -> DeepLinkType.of(link).getIntent(
                        this,
                        UserStatus.of(dataStore.userStatus),
                        link
                    )
                }
            }
        } catch (exception: Exception) {
            Timber.e(exception)
            DeepLinkType.getIntent(
                UserStatus.of(dataStore.userStatus),
                DeepLinkType.UNKNOWN
            )
        }
    }

    private fun isIntentToHome(): Boolean {
        return intent.action == Intent.ACTION_MAIN && (intent.categories?.contains(Intent.CATEGORY_LAUNCHER) == true)
    }

    data class Argument(
        val notificationId: String,
        val link: String
    ) : Serializable

    companion object {
        @JvmStatic
        fun getIntent(context: Context, args: Argument) = Intent(context, SchemeActivity::class.java)
            .putExtra("args", args)
    }
}