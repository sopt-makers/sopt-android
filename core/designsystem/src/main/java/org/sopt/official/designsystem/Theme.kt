/*
 * MIT License
 * Copyright 2022-2024 SOPT - Shout Our Passion Together
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
package org.sopt.official.designsystem

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

@Stable
class SoptColors(
    primary: Color,
    onPrimary: Color,
    background: Color,
    onBackground: Color,
    backgroundDimmed: Color,
    surface: Color,
    success: Color,
    error: Color,
    information: Color,
    attention: Color,
    onSurface: Color,
    onSurface950: Color,
    onSurface900: Color,
    onSurface800: Color,
    onSurface700: Color,
    onSurface600: Color,
    onSurface500: Color,
    onSurface400: Color,
    onSurface300: Color,
    onSurface200: Color,
    onSurface100: Color,
    onSurface50: Color,
    onSurface30: Color,
    onSurface10: Color,
    isLight: Boolean
) {
    var primary by mutableStateOf(primary)
        private set
    var onPrimary by mutableStateOf(onPrimary)
        private set
    var background by mutableStateOf(background)
        private set
    var onBackground by mutableStateOf(onBackground)
        private set
    var backgroundDimmed by mutableStateOf(backgroundDimmed)
        private set
    var surface by mutableStateOf(surface)
        private set
    var success by mutableStateOf(success)
        private set
    var error by mutableStateOf(error)
        private set
    var information by mutableStateOf(information)
        private set
    var attention by mutableStateOf(attention)
        private set
    var onSurface by mutableStateOf(onSurface)
        private set
    var onSurface950 by mutableStateOf(onSurface950)
        private set
    var onSurface900 by mutableStateOf(onSurface900)
        private set
    var onSurface800 by mutableStateOf(onSurface800)
        private set
    var onSurface700 by mutableStateOf(onSurface700)
        private set
    var onSurface600 by mutableStateOf(onSurface600)
        private set
    var onSurface500 by mutableStateOf(onSurface500)
        private set
    var onSurface400 by mutableStateOf(onSurface400)
        private set
    var onSurface300 by mutableStateOf(onSurface300)
        private set
    var onSurface200 by mutableStateOf(onSurface200)
        private set
    var onSurface100 by mutableStateOf(onSurface100)
        private set
    var onSurface50 by mutableStateOf(onSurface50)
        private set
    var onSurface30 by mutableStateOf(onSurface30)
        private set
    var onSurface10 by mutableStateOf(onSurface10)
        private set
    var isLight by mutableStateOf(isLight)

    fun copy(): SoptColors = SoptColors(
        primary,
        onPrimary,
        background,
        onBackground,
        backgroundDimmed,
        surface,
        success,
        error,
        information,
        attention,
        onSurface,
        onSurface950,
        onSurface900,
        onSurface800,
        onSurface700,
        onSurface600,
        onSurface500,
        onSurface400,
        onSurface300,
        onSurface200,
        onSurface100,
        onSurface50,
        onSurface30,
        onSurface10,
        isLight
    )

    fun update(other: SoptColors) {
        primary = other.primary
        onPrimary = other.onPrimary
        background = other.background
        onBackground = other.onBackground
        backgroundDimmed = other.backgroundDimmed
        surface = other.surface
        success = other.success
        error = other.error
        information = other.information
        attention = other.attention
        onSurface = other.onSurface
        onSurface950 = other.onSurface950
        onSurface900 = other.onSurface900
        onSurface800 = other.onSurface800
        onSurface700 = other.onSurface700
        onSurface600 = other.onSurface600
        onSurface500 = other.onSurface500
        onSurface400 = other.onSurface400
        onSurface300 = other.onSurface300
        onSurface200 = other.onSurface200
        onSurface100 = other.onSurface100
        onSurface50 = other.onSurface50
        onSurface30 = other.onSurface30
        onSurface10 = other.onSurface10
        isLight = other.isLight
    }
}

fun soptLightColors(
    primary: Color = White,
    onPrimary: Color = Gray900,
    background: Color = Gray950,
    onBackground: Color = White,
    backgroundDimmed: Color = GrayAlpha100,
    surface: Color = White,
    success: Color = Blue400,
    error: Color = Red400,
    information: Color = Green400,
    attention: Color = Yellow400,
    onSurface: Color = Black,
    onSurface950: Color = Gray950,
    onSurface900: Color = Gray900,
    onSurface800: Color = Gray800,
    onSurface700: Color = Gray700,
    onSurface600: Color = Gray600,
    onSurface500: Color = Gray500,
    onSurface400: Color = Gray400,
    onSurface300: Color = Gray300,
    onSurface200: Color = Gray200,
    onSurface100: Color = Gray100,
    onSurface50: Color = Gray50,
    onSurface30: Color = Gray30,
    onSurface10: Color = Gray10
) = SoptColors(
    primary,
    onPrimary,
    background,
    onBackground,
    backgroundDimmed,
    surface,
    success,
    error,
    information,
    attention,
    onSurface,
    onSurface950,
    onSurface900,
    onSurface800,
    onSurface700,
    onSurface600,
    onSurface500,
    onSurface400,
    onSurface300,
    onSurface200,
    onSurface100,
    onSurface50,
    onSurface30,
    onSurface10,
    isLight = true
)

private val LocalSoptColors = staticCompositionLocalOf<SoptColors> {
    error("No SoptColors provided")
}

private val LocalSoptTypography = staticCompositionLocalOf<SoptTypography> {
    error("No SoptTypography provided")
}

private val LocalLegacyTypography = staticCompositionLocalOf<LegacySoptTypography> {
    error("No LegacyTypography provided")
}

/*
* SoptTheme
*
* Color에 접근하고 싶을때 SoptTheme.colors.primary 이런식으로 접근하면 됩니다.
* Typo를 변경하고 싶다면 SoptTheme.typography.heading48B 혹은
* SoptTheme.legacyTypography.h1 이런식으로 접근하면 됩니다.
* */
object SoptTheme {
    val colors: SoptColors
        @Composable
        @ReadOnlyComposable
        get() = LocalSoptColors.current

    val typography: SoptTypography
        @Composable
        @ReadOnlyComposable
        get() = LocalSoptTypography.current

    val legacyTypography: LegacySoptTypography
        @Composable
        @ReadOnlyComposable
        get() = LocalLegacyTypography.current
}

@Composable
fun ProvideSoptColorsAndTypography(
    colors: SoptColors,
    typography: SoptTypography,
    legacyTypography: LegacySoptTypography,
    content: @Composable () -> Unit
) {
    val provideColors = remember { colors.copy() }
    provideColors.update(colors)
    val provideTypography = remember { typography.copy() }.apply { update(typography) }
    val provideLegacyTypography = remember { legacyTypography.copy() }
    provideLegacyTypography.update(legacyTypography)
    CompositionLocalProvider(
        LocalSoptColors provides provideColors,
        LocalSoptTypography provides provideTypography,
        LocalLegacyTypography provides provideLegacyTypography,
        content = content
    )
}

@Composable
fun SoptTheme(darkTheme: Boolean = false, content: @Composable () -> Unit) {
    val colors = soptLightColors()
    val typography = SoptTypography()
    val legacyTypography = LegacySoptTypography()
    ProvideSoptColorsAndTypography(colors, typography, legacyTypography) {
        MaterialTheme(content = content)
    }
}
