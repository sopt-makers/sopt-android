/*
 * MIT License
 * Copyright 2023-2025 SOPT - Shout Our Passion Together
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
package org.sopt.official.feature.mypage.di

import org.sopt.official.common.context.appContext
import org.sopt.official.domain.auth.repository.AuthRepository
import org.sopt.official.domain.mypage.repository.UserRepository
import org.sopt.official.domain.notification.repository.NotificationRepository
import org.sopt.official.domain.soptamp.repository.StampRepository

interface AuthGraph {
    val authRepository: AuthRepository
    val userRepository: UserRepository
    val stampRepository: StampRepository
    val notificationRepository: NotificationRepository
}

val authRepository by lazy {
    (appContext as AuthGraph).authRepository
}

val userRepository by lazy {
    (appContext as AuthGraph).userRepository
}

val stampRepository by lazy {
    (appContext as AuthGraph).stampRepository
}

val notificationRepository by lazy {
    (appContext as AuthGraph).notificationRepository
}
