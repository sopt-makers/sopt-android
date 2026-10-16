/*
 * MIT License
 * Copyright 2026 SOPT - Shout Our Passion Together
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
package org.sopt.official.feature.poke.v2.navigation

import android.content.Intent

/**
 * 콕 찌르기 딥링크 구조.
 *
 * 콕 찌르기는 자체 URI 스킴 파서를 갖지 않고, `app` 모듈의 `NavigatorProviderIntent`가
 * `MainActivity`로 향하는 Intent 에 아래 extra 플래그를 실어 보내면
 * `feature:main`의 `MainScreen`이 `LaunchedEffect`에서 이를 읽어 해당 화면으로 이동시킨다.
 *
 * v2 전환 시 `MainScreen`은 [PokeDeepLinkArgs.from] 로 Intent 를 한 번에 파싱하고,
 * 각 화면 패키지의 `navigateToPokeXxx` 확장 함수로 이동하면 된다.
 *
 * | 목적지            | 트리거 extra          | 부가 extra                    |
 * |------------------|----------------------|------------------------------|
 * | 콕 찌르기 탭       | `isPokeDeepLink`     | -                            |
 * | 콕 찌르기 알림     | `isPokeNotification` | `userStatus`                 |
 * | 친한 친구 리스트   | `isPokeFriendList`   | `userStatus`, `friendType`   |
 *
 * 온보딩으로의 직접 딥링크는 없다. 홈 배너에서 `navigateToPoke(url, isNewPoke, currentDestination)`
 * 호출 시 `isNewPoke == true` 이면 화면 단에서 `navigateToPokeOnboarding` 로 분기한다.
 */
object PokeDeepLink {
    const val EXTRA_IS_POKE = "isPokeDeepLink"
    const val EXTRA_IS_POKE_NOTIFICATION = "isPokeNotification"
    const val EXTRA_IS_POKE_FRIEND_LIST = "isPokeFriendList"
    const val EXTRA_USER_STATUS = "userStatus"
    const val EXTRA_FRIEND_TYPE = "friendType"
}

/**
 * [Intent] 에서 파싱한 콕 찌르기 딥링크 목적지.
 */
sealed interface PokeDeepLinkArgs {
    /** 콕 찌르기 탭 진입(= 브릿지 화면부터 시작). */
    data object Poke : PokeDeepLinkArgs

    /** 콕 찌르기 알림 화면. */
    data class Notification(val userStatus: String) : PokeDeepLinkArgs

    /** 친한 친구 리스트 화면. */
    data class FriendList(
        val userStatus: String,
        val friendType: String?,
    ) : PokeDeepLinkArgs

    companion object {
        /** 콕 찌르기 딥링크가 아니면 `null`. 우선순위는 알림 > 친구 리스트 > 탭 진입. */
        fun from(intent: Intent?): PokeDeepLinkArgs? {
            if (intent == null) return null
            val userStatus = intent.getStringExtra(PokeDeepLink.EXTRA_USER_STATUS).orEmpty()
            return when {
                intent.getBooleanExtra(PokeDeepLink.EXTRA_IS_POKE_NOTIFICATION, false) ->
                    Notification(userStatus)

                intent.getBooleanExtra(PokeDeepLink.EXTRA_IS_POKE_FRIEND_LIST, false) ->
                    FriendList(userStatus, intent.getStringExtra(PokeDeepLink.EXTRA_FRIEND_TYPE))

                intent.getBooleanExtra(PokeDeepLink.EXTRA_IS_POKE, false) -> Poke

                else -> null
            }
        }
    }
}

/** 처리 후 재진입 시 중복 내비게이션을 막기 위해 소비한 플래그를 내린다. */
fun Intent.consumePokeDeepLink() {
    putExtra(PokeDeepLink.EXTRA_IS_POKE, false)
    putExtra(PokeDeepLink.EXTRA_IS_POKE_NOTIFICATION, false)
    putExtra(PokeDeepLink.EXTRA_IS_POKE_FRIEND_LIST, false)
}
