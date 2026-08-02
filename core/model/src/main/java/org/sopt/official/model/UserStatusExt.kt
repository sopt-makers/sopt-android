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
package org.sopt.official.model

/**
 * [UserStatus]를 Amplitude의 `view_type` 프로퍼티 값으로 변환하는 함수
 *
 * @return 활동 유저는 `active`, 비활동 유저는 `inactive`, 비회원은 `visitor`
 */
fun UserStatus.toViewType(): String = when (this) {
    UserStatus.ACTIVE -> "active"
    UserStatus.INACTIVE -> "inactive"
    UserStatus.UNAUTHENTICATED -> "visitor"
}

/**
 * 문자열로 전달된 유저 상태를 Amplitude의 `view_type` 프로퍼티 값으로 변환하는 함수
 *
 * 대소문자를 구분하지 않으며, 값이 `null`이거나 지원하지 않는 값이면 `visitor`를 반환합니다.
 *
 * @return `active`, `inactive`, `visitor` 중 하나
 */
fun String?.toViewType(): String =
    when (this?.uppercase()) {
        UserStatus.ACTIVE.value -> UserStatus.ACTIVE.toViewType()
        UserStatus.INACTIVE.value -> UserStatus.INACTIVE.toViewType()
        UserStatus.UNAUTHENTICATED.value -> UserStatus.UNAUTHENTICATED.toViewType()
        else -> UserStatus.UNAUTHENTICATED.toViewType()
    }
