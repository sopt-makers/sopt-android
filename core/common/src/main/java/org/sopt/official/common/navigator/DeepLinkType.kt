/*
 * MIT License
 * Copyright 2024-2026 SOPT - Shout Our Passion Together
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
import org.sopt.official.common.context.appContext
import org.sopt.official.common.util.extractQueryParameter
import org.sopt.official.model.UserStatus
import timber.log.Timber

internal val navigator by lazy {
    EntryPointAccessors.fromApplication(appContext, NavigatorEntryPoint::class.java).navigatorProvider()
}

enum class DeepLinkType(
    val link: String,
) {
    HOME("home") {
        override fun getIntent(context: Context, userStatus: UserStatus, deepLink: String) = getIntent(userStatus)
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
    MY_PAGE_SOPTAMP("mypage/soptamp") {
        override fun getIntent(context: Context, userStatus: UserStatus, deepLink: String) =
            userStatus.setIntent(navigator.getAdjustSentenceActivityIntent())
    },
    ATTENDANCE("home/attendance") {
        override fun getIntent(context: Context, userStatus: UserStatus, deepLink: String) =
            userStatus.setIntent(navigator.getAttendanceActivityIntent())
    },
    ATTENDANCE_MODAL("home/attendance/attendance-modal") {
        override fun getIntent(context: Context, userStatus: UserStatus, deepLink: String) =
            userStatus.setIntent(navigator.getAttendanceActivityIntent())
    },
    SOPTAMP("soptamp") {
        override fun getIntent(context: Context, userStatus: UserStatus, deepLink: String) =
            userStatus.setIntent(navigator.getSoptampActivityIntent())
    },
    SOPTAMP_ENTIRE_RANKING("soptamp/entire-ranking") {
        override fun getIntent(context: Context, userStatus: UserStatus, deepLink: String) =
            userStatus.setIntent(navigator.getSoptampActivityIntent())
    },
    SOPTAMP_MISSION_DETAIL("soptamp/entire-part-ranking/part-ranking/missions/missionDetail") {
        override fun getIntent(context: Context, userStatus: UserStatus, deepLink: String): Intent {
            val id = deepLink.extractQueryParameter("id").toIntOrNull() ?: -1
            val missionId = deepLink.extractQueryParameter("missionId").toIntOrNull() ?: -1
            val isMine = deepLink.extractQueryParameter("isMine").toBoolean()
            val nickname = deepLink.extractQueryParameter("nickname")
            val part = deepLink.extractQueryParameter("part")
            val level = deepLink.extractQueryParameter("level").toIntOrNull() ?: -1
            val title = deepLink.extractQueryParameter("missionTitle")

            val intent = navigator.getSoptampMissionDetailActivityIntent(
                id = id,
                missionId = missionId,
                isMine = isMine,
                nickname = nickname,
                part = part,
                level = level,
                title = title
            )

            return userStatus.setIntent(intent)
        }
    },
    SOPTAMP_CURRENT_GENERATION_RANKING("soptamp/current-generation-ranking") {
        override fun getIntent(context: Context, userStatus: UserStatus, deepLink: String) =
            userStatus.setIntent(navigator.getSoptampActivityIntent())
    },
    APPJAMTAMP("appjamtamp") {
        override fun getIntent(context: Context, userStatus: UserStatus, deepLink: String) =
            userStatus.setIntent(navigator.getAppjamtampActivityIntent())
    },

    // TODO - 콕찌르기 이슈 해결되면 딥링크에서 home 제거해야 함 (서버 변경으로 home 제거)
    POKE_NOTIFICATION_LIST("home/poke/notification-list") {
        override fun getIntent(context: Context, userStatus: UserStatus, deepLink: String) =
            userStatus.setIntent(navigator.getPokeNotificationActivityIntent(userStatus.name))
    },
    POKE_FRIEND_LIST_SUMMARY("home/poke/friend-list-summary") {
        override fun getIntent(context: Context, userStatus: UserStatus, deepLink: String): Intent {
            val friendType = deepLink.extractQueryParameter("type")
            val intent = navigator.getPokeFriendListSummaryActivityIntent(userStatus.name, friendType)
            return userStatus.setIntent(intent)
        }
    },
    POKE("home/poke") {
        override fun getIntent(context: Context, userStatus: UserStatus, deepLink: String) =
            userStatus.setIntent(navigator.getPokeActivityIntent(userStatus))
    },
    FORTUNE(SOPTLOG_FORTUNE) {
        override fun getIntent(context: Context, userStatus: UserStatus, deepLink: String) =
            userStatus.setIntent(navigator.getFortuneActivityIntent())
    },
    SCHEDULE("home/schedule") {
        override fun getIntent(context: Context, userStatus: UserStatus, deepLink: String) =
            userStatus.setIntent(navigator.getScheduleActivityIntent())
    },
    UNKNOWN("unknown-deep-link") {
        override fun getIntent(context: Context, userStatus: UserStatus, deepLink: String) = getIntent(userStatus, UNKNOWN)
    },
    EXPIRED("expired") {
        override fun getIntent(context: Context, userStatus: UserStatus, deepLink: String) = getIntent(userStatus, EXPIRED)
    };

    abstract fun getIntent(context: Context, userStatus: UserStatus, deepLink: String): Intent

    companion object {
        private fun UserStatus.setIntent(intent: Intent): Intent {
            /*return when (this == UserStatus.UNAUTHENTICATED) {
                true -> navigator.getAuthActivityIntent()
                false -> intent
            }*/
            return intent
        }

        fun getIntent(userStatus: UserStatus, deepLinkType: DeepLinkType? = null): Intent =
            userStatus.setIntent(navigator.getMainActivityIntent(userStatus, deepLinkType))


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

const val SOPTLOG_FORTUNE = "soptlog/fortune"
