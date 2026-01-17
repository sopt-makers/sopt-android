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
package org.sopt.official.common.navigator

import android.content.Intent
import org.sopt.official.model.UserStatus

interface NavigatorProvider {
    fun getAuthActivityIntent(): Intent
    fun getNotificationActivityIntent(): Intent
    fun getNotificationDetailActivityIntent(notificationId: String): Intent
    fun getMyPageActivityIntent(name: String): Intent
    fun getAdjustSentenceActivityIntent(): Intent
    fun getAttendanceActivityIntent(): Intent
    fun getSoptampActivityIntent(): Intent
    fun getAppjamtampActivityIntent(): Intent
    fun getPokeNotificationActivityIntent(name: String): Intent
    fun getPokeFriendListSummaryActivityIntent(name: String, friendType: String?): Intent
    fun getPokeActivityIntent(userStatus: UserStatus): Intent
    fun getPokeOnboardingActivityIntent(currentGeneration: Int, userStatus: UserStatus): Intent
    fun getFortuneActivityIntent(): Intent
    fun getScheduleActivityIntent(): Intent
    fun getSoptLogIntent(): Intent

    fun getSoptampMissionDetailActivityIntent(
        id: Int,
        missionId: Int,
        isMine: Boolean,
        nickname: String?,
        part: String?,
        level: Int,
        title: String?
    ): Intent

    fun getMainActivityIntent(
        userStatus: UserStatus,
        deepLinkType: DeepLinkType?,
    ): Intent

    fun getSchemeActivityIntent(
        notificationId: String,
        link: String,
    ): Intent
}

interface NavigatorGraph {
    val navigatorProvider: NavigatorProvider
}
