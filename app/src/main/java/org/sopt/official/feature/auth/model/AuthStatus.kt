/*
 * MIT License
 * Copyright 2025 SOPT - Shout Our Passion Together
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
package org.sopt.official.feature.auth.model

// TODO: mock서버 삭제 시 authName, phone, token은 같이 없어질 예정입니다.
enum class AuthStatus(
    val authName: String,
    val phone: String,
    val type: String,
    val token: String
) {
    // 회원가입
    REGISTER(
        authName = "Mock-Success-Register",
        phone = "01012345678",
        type = "REGISTER",
        token = "123456"
    ),
    // 소셜계정 변경
    CHANGE(
        authName = "Mock-Success-Change",
        phone = "01087654321",
        type = "CHANGE",
        token = "654321"
    ),
    // 소셜계정 찾기
    SEARCH(
        authName = "Mock-Success-Search",
        phone = "01013245768",
        type = "SEARCH",
        token = "132456"
    )
}
