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
