/*
 * MIT License
 * Copyright 2023-2026 SOPT - Shout Our Passion Together
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
package org.sopt.official.feature.navigator

import android.app.Application
import android.content.Intent
import dev.zacsweers.metro.Inject
import org.sopt.official.common.navigator.DeepLinkType
import org.sopt.official.common.navigator.NavigatorProvider
import org.sopt.official.feature.attendance.AttendanceActivity
import org.sopt.official.feature.auth.AuthActivity
import org.sopt.official.feature.fortune.FortuneActivity
import org.sopt.official.feature.main.MainActivity
import org.sopt.official.feature.mypage.mypage.MyPageActivity
import org.sopt.official.feature.mypage.soptamp.ui.AdjustSentenceActivity
import org.sopt.official.feature.notification.SchemeActivity
import org.sopt.official.feature.notification.all.NotificationActivity
import org.sopt.official.feature.notification.detail.NotificationDetailActivity
import org.sopt.official.feature.schedule.ScheduleActivity
import org.sopt.official.model.UserStatus
import org.sopt.official.stamp.feature.navigation.SoptampMissionArgs

class NavigatorProviderIntent @Inject constructor(
    private val application: Application,
) : NavigatorProvider {
    private val context get() = application.applicationContext

    override fun getAuthActivityIntent(): Intent = AuthActivity.newInstance(context)
    override fun getNotificationActivityIntent() = NotificationActivity.newInstance(context)
    override fun getNotificationDetailActivityIntent(notificationId: String) = NotificationDetailActivity.getIntent(
        context,
        notificationId
    )

    override fun getMyPageActivityIntent(name: String) = MyPageActivity.getIntent(
        context,
        MyPageActivity.Argument(UserStatus.valueOf(name))
    )

    override fun getAdjustSentenceActivityIntent(): Intent = AdjustSentenceActivity.getIntent(
        context
    )

    override fun getPokeActivityIntent(userStatus: UserStatus): Intent {
        return Intent(context, MainActivity::class.java).apply {
            // MainScreen이 Pke 탭으로 이동 시키기 위한 트리거용
            putExtra("isPokeDeepLink", true)
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP)
        }
    }

    override fun getPokeOnboardingActivityIntent(currentGeneration: Int, userStatus: UserStatus): Intent {
        return Intent(context, MainActivity::class.java).apply {
            // MainScreen이 Pke 탭으로 이동 시키기 위한 트리거용
            putExtra("isPokeDeepLink", true)
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP)
        }
    }

    override fun getAttendanceActivityIntent() = AttendanceActivity.newInstance(context)

    override fun getSoptampActivityIntent(): Intent {
        return Intent(context, MainActivity::class.java).apply {
            // MainScreen이 솝탬프 탭으로 이동 시키기 위한 트리거용
            putExtra("isSoptampDeepLink", true)
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP)
        }
    }

    override fun getSoptampMissionDetailActivityIntent(
        id: Int,
        missionId: Int,
        isMine: Boolean,
        nickname: String?,
        part: String?,
        level: Int,
        title: String?
    ): Intent {
        val args = SoptampMissionArgs(
            id = id,
            missionId = missionId,
            isMine = isMine,
            nickname = nickname,
            part = part,
            level = level,
            title = title
        )

        return Intent(context, MainActivity::class.java).apply {
            putExtra("soptampArgs", args)
            putExtra("isSoptampDeepLink", true)
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP)
        }
    }

    override fun getAppjamtampActivityIntent(): Intent {
        return Intent(context, MainActivity::class.java).apply {
            putExtra("isAppjamtampDeepLink", true)
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP)
        }
    }

    override fun getPokeNotificationActivityIntent(name: String): Intent {
        return Intent(context, MainActivity::class.java).apply {
            putExtra("userStatus", name)
            putExtra("isPokeNotification", true)
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP)
        }
    }

    override fun getPokeFriendListSummaryActivityIntent(name: String, friendType: String?): Intent {
        return Intent(context, MainActivity::class.java).apply {
            putExtra("userStatus", name)
            putExtra("friendType", friendType)
            putExtra("isPokeFriendList", true)
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP)
        }
    }

    override fun getFortuneActivityIntent(): Intent = FortuneActivity.getIntent(context)
    override fun getScheduleActivityIntent(): Intent = ScheduleActivity.getIntent(context)

    override fun getSoptLogIntent(): Intent {
        return Intent(context, MainActivity::class.java).apply {
            // MainScreen이 솝트로그 탭으로 이동 시키기 위한 트리거용
            putExtra("isSoptLogDeepLink", true)
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP)
        }
    }

    override fun getSchemeActivityIntent(
        notificationId: String,
        link: String,
    ) = SchemeActivity.getIntent(
        context,
        SchemeActivity.Argument(
            notificationId,
            link
        )
    )

    override fun getMainActivityIntent(userStatus: UserStatus, deepLinkType: DeepLinkType?): Intent =
        MainActivity.getIntent(context, MainActivity.StartArgs(userStatus = userStatus, deepLinkType = deepLinkType))
}