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
package org.sopt.official.security

import com.google.common.truth.Truth.assertThat
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import org.sopt.official.security.util.getDecryptedDataOrDefault
import org.sopt.official.security.util.getEncryptedDataOrDefault

class CryptoManagerTest {

    @ParameterizedTest
    @MethodSource("generateEncryptedTestData")
    @DisplayName("keyAlias와 암호화 할 데이터를 입력하면 암호화를 진행한다")
    fun testEncryptSuccess(keyAlias: String, bytes: ByteArray) {
        // when
        val encryptedResult = CryptoManager.encrypt(keyAlias = keyAlias, bytes = bytes)

        // then
        assertTrue(encryptedResult.isSuccess, "암호화가 성공적으로 진행되었습니다.")
    }

    @ParameterizedTest
    @MethodSource("generateCryptoTestData")
    @DisplayName("keyAlias와 데이터를 입력하면 암호화, 복호화를 순차적으로 진행해 기존의 데이터를 반환한다")
    fun testCryptoSuccess(keyAlias: String, data: String) {
        // given
        val encryptedData = data.getEncryptedDataOrDefault(keyAlias = keyAlias)

        // when
        val decryptedData = encryptedData.getDecryptedDataOrDefault(keyAlias = keyAlias)

        // then
        assertThat(decryptedData).isEqualTo(data)
    }

    companion object {
        @JvmStatic
        fun generateEncryptedTestData() = listOf(
            Arguments.of("ACCESS_TOKEN", "accessToken".toByteArray()),
            Arguments.of("REFRESH_TOKEN", "refreshToken".toByteArray()),
            Arguments.of("PLAYGROUND_TOKEN", "playgroundToken".toByteArray()),
            Arguments.of("USER_STATUS", "userStatus".toByteArray()),
            Arguments.of("PUSH_TOKEN", "pushToken".toByteArray())
        )

        @JvmStatic
        fun generateCryptoTestData() = listOf(
            Arguments.of("ACCESS_TOKEN", "accessToken"),
            Arguments.of("REFRESH_TOKEN", "refreshToken"),
            Arguments.of("PLAYGROUND_TOKEN", "playgroundToken"),
            Arguments.of("USER_STATUS", "userStatus"),
            Arguments.of("PUSH_TOKEN", "pushToken")
        )
    }
}
