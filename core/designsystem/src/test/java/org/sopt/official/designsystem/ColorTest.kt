@file:Suppress("NonAsciiCharacters")

package org.sopt.official.designsystem

import androidx.compose.ui.graphics.Color
import com.google.common.truth.Truth.assertThat
import org.junit.jupiter.api.extension.ExtensionContext
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.ArgumentsProvider
import org.junit.jupiter.params.provider.ArgumentsSource
import java.util.stream.Stream

class ColorAlphaArgumentsProvider : ArgumentsProvider {
    override fun provideArguments(context: ExtensionContext): Stream<out Arguments> {
        return Stream.of(
            Arguments.of(100, 0xFFFFFFFF),
            Arguments.of(90, 0xE5FFFFFF),
            Arguments.of(80, 0xCCFFFFFF),
            Arguments.of(70, 0xB2FFFFFF),
            Arguments.of(60, 0x99FFFFFF),
            Arguments.of(50, 0x7FFFFFFF),
            Arguments.of(40, 0x66FFFFFF),
            Arguments.of(30, 0x4CFFFFFF),
            Arguments.of(20, 0x33FFFFFF),
            Arguments.of(10, 0x19FFFFFF),
            Arguments.of(0, 0x00FFFFFF)
        )
    }
}

class ColorTest {
    @ParameterizedTest
    @ArgumentsSource(ColorAlphaArgumentsProvider::class)
    fun `Alpha 값을 100을 주면 alpha 채널은 FF이다`(alpha: Int, expectedHex: Long) {
        // given
        val expected = Color(expectedHex)

        // when
        val colorHex = 0xFFFFFF
        val actual = AlphaColor(colorHex, alpha)

        assertThat(actual).isEqualTo(expected)
    }
}
