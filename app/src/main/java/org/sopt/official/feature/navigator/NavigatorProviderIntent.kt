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
package org.sopt.official.feature.navigator

import android.content.Context
import android.content.Intent
import dagger.hilt.android.qualifiers.ApplicationContext
import org.sopt.official.auth.model.UserActiveState
import org.sopt.official.auth.model.UserStatus
import org.sopt.official.common.navigator.DeepLinkType
import org.sopt.official.common.navigator.NavigatorProvider
import org.sopt.official.feature.attendance.AttendanceActivity
import org.sopt.official.feature.auth.AuthActivity
import org.sopt.official.feature.home.HomeActivity
import org.sopt.official.feature.mypage.mypage.MyPageActivity
import org.sopt.official.feature.notification.all.NotificationActivity
import org.sopt.official.feature.notification.detail.NotificationDetailActivity
import org.sopt.official.feature.poke.notification.PokeNotificationActivity
import org.sopt.official.stamp.SoptampActivity
import javax.inject.Inject

class NavigatorProviderIntent @Inject constructor(
  @ApplicationContext private val context: Context
) : NavigatorProvider {
  override fun getAuthActivityIntent(): Intent = AuthActivity.newInstance(context)
  override fun getNotificationActivityIntent() = NotificationActivity.newInstance(context)
  override fun getNotificationDetailActivityIntent(notificationId: String) = NotificationDetailActivity.getIntent(context, notificationId)
  override fun getMyPageActivityIntent(name: String) =
    MyPageActivity.getIntent(context, MyPageActivity.Argument(UserActiveState.valueOf(name)))

  override fun getAttendanceActivityIntent() = AttendanceActivity.newInstance(context)

  override fun getSoptampActivityIntent() = SoptampActivity.getIntent(context)

  override fun getPokeNotificationActivityIntent(name: String) =
    PokeNotificationActivity.getIntent(context, PokeNotificationActivity.Argument(name))

  override fun getHomeActivityIntent(userStatus: UserStatus, deepLinkType: DeepLinkType) =
    HomeActivity.getIntent(context, HomeActivity.StartArgs(userStatus, deepLinkType))
}
