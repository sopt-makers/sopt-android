/*
 * MIT License
 * Copyright 2024 SOPT - Shout Our Passion Together
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
package org.sopt.official.common.navigator

import android.content.Context
import android.content.Intent
import dagger.hilt.android.EntryPointAccessors
import org.sopt.official.auth.model.UserStatus
import org.sopt.official.common.context.appContext
import org.sopt.official.common.util.extractQueryParameter
import timber.log.Timber

internal val navigator by lazy {
    EntryPointAccessors.fromApplication(appContext, NavigatorEntryPoint::class.java).navigatorProvider()
}

enum class DeepLinkType(
    val link: String,
) {
    HOME("home") {
        override fun getIntent(context: Context, userStatus: UserStatus, deepLink: String) = getHomeIntent(userStatus)
    },
    NOTIFICATION_LIST("home/notification") {
        override fun getIntent(context: Context, userStatus: UserStatus, deepLink: String) =
            userStatus.setIntent(navigator.getNotificationActivityIntent())
    },
    NOTIFICATION_DETAIL("home/notification/detail") {
        override fun getIntent(context: Context, userStatus: UserStatus, deepLink: String): Intent {
            val notificationId = deepLink.extractQueryParameter("id")
            return userStatus.setIntent(navigator.getNotificationDetailActivityIntent(notificationId))
        }
    },
    MY_PAGE("home/mypage") {
        override fun getIntent(context: Context, userStatus: UserStatus, deepLink: String) =
            userStatus.setIntent(navigator.getMyPageActivityIntent(userStatus.name))
    },
    ATTENDANCE("home/attendance") {
        override fun getIntent(context: Context, userStatus: UserStatus, deepLink: String) =
            userStatus.setIntent(navigator.getAttendanceActivityIntent())
    },
    ATTENDANCE_MODAL("home/attendance/attendance-modal") {
        override fun getIntent(context: Context, userStatus: UserStatus, deepLink: String) =
            userStatus.setIntent(navigator.getAttendanceActivityIntent())
    },
    SOPTAMP("home/soptamp") {
        override fun getIntent(context: Context, userStatus: UserStatus, deepLink: String) =
            userStatus.setIntent(navigator.getSoptampActivityIntent())
    },
    SOPTAMP_ENTIRE_RANKING("home/soptamp/entire-ranking") {
        override fun getIntent(context: Context, userStatus: UserStatus, deepLink: String) =
            userStatus.setIntent(navigator.getSoptampActivityIntent())
    },
    SOPTAMP_CURRENT_GENERATION_RANKING("home/soptamp/current-generation-ranking") {
        override fun getIntent(context: Context, userStatus: UserStatus, deepLink: String) =
            userStatus.setIntent(navigator.getSoptampActivityIntent())
    },
    POKE_NOTIFICATION_LIST("home/poke/notification-list") {
        override fun getIntent(context: Context, userStatus: UserStatus, deepLink: String) =
            userStatus.setIntent(navigator.getPokeNotificationActivityIntent(userStatus.name))
    },
    FORTUNE("home/fortune") {
        override fun getIntent(context: Context, userStatus: UserStatus, deepLink: String) =
            userStatus.setIntent(navigator.getFortuneActivityIntent())
    },
    UNKNOWN("unknown-deep-link") {
        override fun getIntent(context: Context, userStatus: UserStatus, deepLink: String) = getHomeIntent(userStatus, UNKNOWN)
    },
    EXPIRED("expired") {
        override fun getIntent(context: Context, userStatus: UserStatus, deepLink: String) = getHomeIntent(userStatus, EXPIRED)
    };

    abstract fun getIntent(context: Context, userStatus: UserStatus, deepLink: String): Intent

    companion object {
        private fun UserStatus.setIntent(intent: Intent): Intent {
            return when (this == UserStatus.UNAUTHENTICATED) {
                true -> navigator.getAuthActivityIntent()
                false -> intent
            }
        }

        fun getHomeIntent(userStatus: UserStatus, deepLinkType: DeepLinkType? = null) =
            userStatus.setIntent(navigator.getHomeActivityIntent(userStatus, deepLinkType))

        fun of(deepLink: String): DeepLinkType {
            return try {
                val link = deepLink.split("?")[0]
                entries.find { it.link == link } ?: UNKNOWN
            } catch (exception: Exception) {
                Timber.e(exception)
                UNKNOWN
            }
        }
    }
}
