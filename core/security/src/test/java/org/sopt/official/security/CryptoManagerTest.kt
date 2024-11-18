package org.sopt.official.security

import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource

class CryptoManagerTest {

    @ParameterizedTest
    @MethodSource("generateEncryptTestData")
    @DisplayName("keyAlias와 암호화 할 데이터를 입력하면 암호화가 잘 되었는지 여부를 반환한다")
    fun testEncryptSuccess(keyAlias: String, bytes: ByteArray) {
        // when
        val encryptResult = CryptoManager.encrypt(keyAlias = keyAlias, bytes = bytes)

        // then
        assertTrue(encryptResult.isSuccess, "암호화가 성공적으로 진행되었습니다.")
    }

    companion object {
        @JvmStatic
        fun generateEncryptTestData() = listOf(
            Arguments.of("ACCESS_TOKEN", "accessToken".toByteArray(Charsets.UTF_8)),
            Arguments.of("REFRESH_TOKEN", "refreshToken".toByteArray(Charsets.UTF_8)),
            Arguments.of("PLAYGROUND_TOKEN", "playgroundToken".toByteArray(Charsets.UTF_8)),
            Arguments.of("USER_STATUS", "userStatus".toByteArray(Charsets.UTF_8)),
            Arguments.of("PUSH_TOKEN", "pushToken".toByteArray(Charsets.UTF_8))
        )
    }
}
