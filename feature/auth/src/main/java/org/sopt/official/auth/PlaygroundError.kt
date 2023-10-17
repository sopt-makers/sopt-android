/*
 * Copyright 2023 SOPT - Shout Our Passion Together
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.sopt.official.auth

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
sealed class PlaygroundError(
    private val exceptionMessage: String
) : Exception(exceptionMessage), Parcelable {
    @Parcelize
    data class IllegalConnect(
        override val message: String = "비정상적인 접근 방법입니다."
    ) : PlaygroundError(message)

    @Parcelize
    data class NotFoundStateCode(
        override val message: String = "state 인증 코드를 찾을 수 없습니다"
    ) : PlaygroundError(message)

    @Parcelize
    data class IllegalStateCode(
        override val message: String = "state 인증 코드를 처리할 수 없습니다."
    ) : PlaygroundError(message)

    @Parcelize
    data class NetworkUnavailable(
        override val message: String = "Network 에 연결할 수 없습니다."
    ) : PlaygroundError(message)

    @Parcelize
    data class InconsistentStateCode(
        override val message: String = "state 인증 코드가 일치하지 않습니다."
    ) : PlaygroundError(message)

    @Parcelize
    data class NotSupported(
        override val message: String = "지원하지 않는 기능입니다."
    ) : PlaygroundError(message)

    @Parcelize
    data class Cancelled(
        override val message: String = "인증 요청이 취소되었습니다."
    ) : PlaygroundError(message)
}
