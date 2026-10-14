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
package org.sopt.official.feature.home.navigation

import androidx.compose.runtime.Stable

@Stable
sealed interface HomeNavigation {

    @Stable
    interface HomeShortcutNavigation : HomeNavigation {
        fun navigateToPlaygroundHome()
        fun navigateToPlaygroundCommunity()
        fun navigateToPlaygroundGroup()
        fun navigateToPlaygroundMember()
        fun navigateToPlaygroundProject()
        fun navigateToPlaygroundCoffeeChat()
        fun navigateToSoptHomepage()
        fun navigateToSoptReview()
        fun navigateToSoptProject()
        fun navigateToSoptInstagram()
    }

    @Stable
    interface HomeDashboardNavigation : HomeNavigation {
        fun navigateToNotification()
        fun navigateToSchedule()
        fun navigateToEditProfile()
        fun navigateToAttendance()

        // TODO(poke-v2): 정식 연결점이 아닌, 홈 로고 롱클릭을 통한 개발자 전용 테스트 진입점이다.
        //  정식 연결점이 정해지면 MainTab/Route 기반 네비게이션으로 교체하고 이 메서드는 제거한다.
        fun navigateToPokeV2Test()
    }

    @Stable
    interface HomeAppServicesNavigation : HomeNavigation {
        fun navigateToDeepLink(url: String)
        fun navigateToWebUrl(url: String)
        fun navigateToPoke(url: String, isNewPoke: Boolean, currentDestination: Int)
        fun navigateToPlaygroundMemberProfile(userId: Int)
    }
}
