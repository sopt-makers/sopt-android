/*
 * MIT License
 * Copyright 2023 SOPT - Shout Our Passion Together
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
