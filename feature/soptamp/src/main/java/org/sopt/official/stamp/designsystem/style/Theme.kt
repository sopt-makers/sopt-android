/*
 * Copyright 2022-2023 SOPT - Shout Our Passion Together
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
package org.sopt.official.stamp.designsystem.style

import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

@Stable
class SoptColors(
    white: Color,
    black: Color,
    black100: Color,
    purple300: Color,
    purple200: Color,
    purple100: Color,
    pink300: Color,
    pink200: Color,
    pink100: Color,
    mint300: Color,
    mint200: Color,
    mint100: Color,
    error300: Color,
    error200: Color,
    error100: Color,
    access300: Color,
    onSurface: Color,
    onSurface90: Color,
    onSurface80: Color,
    onSurface70: Color,
    onSurface60: Color,
    onSurface50: Color,
    onSurface40: Color,
    onSurface30: Color,
    onSurface20: Color,
    onSurface10: Color,
    onSurface5: Color,
    isLight: Boolean
) {
    var white by mutableStateOf(white)
        private set
    var black by mutableStateOf(black)
        private set
    var black100 by mutableStateOf(black100)
        private set
    var purple300 by mutableStateOf(purple300)
        private set
    var purple200 by mutableStateOf(purple200)
        private set
    var purple100 by mutableStateOf(purple100)
        private set
    var pink300 by mutableStateOf(pink300)
        private set
    var pink200 by mutableStateOf(pink200)
        private set
    var pink100 by mutableStateOf(pink100)
        private set
    var mint300 by mutableStateOf(mint300)
        private set
    var mint200 by mutableStateOf(mint200)
        private set
    var mint100 by mutableStateOf(mint100)
        private set
    var error300 by mutableStateOf(error300)
        private set
    var error200 by mutableStateOf(error200)
        private set
    var error100 by mutableStateOf(error100)
        private set
    var access300 by mutableStateOf(access300)
        private set
    var onSurface by mutableStateOf(onSurface)
        private set
    var onSurface90 by mutableStateOf(onSurface90)
        private set
    var onSurface80 by mutableStateOf(onSurface80)
        private set
    var onSurface70 by mutableStateOf(onSurface70)
        private set
    var onSurface60 by mutableStateOf(onSurface60)
        private set
    var onSurface50 by mutableStateOf(onSurface50)
        private set
    var onSurface40 by mutableStateOf(onSurface40)
        private set
    var onSurface30 by mutableStateOf(onSurface30)
        private set
    var onSurface20 by mutableStateOf(onSurface20)
        private set
    var onSurface10 by mutableStateOf(onSurface10)
        private set
    var onSurface5 by mutableStateOf(onSurface5)
        private set
    var isLight by mutableStateOf(isLight)

    fun copy(): SoptColors = SoptColors(
        white = Color.White,
        black = Color.Black,
        black100 = black100,
        purple300 = purple300,
        purple200 = purple200,
        purple100 = purple100,
        pink300 = pink300,
        pink200 = pink200,
        pink100 = pink100,
        mint300 = mint300,
        mint200 = mint200,
        mint100 = mint100,
        error300 = error300,
        error200 = error200,
        error100 = error100,
        access300 = access300,
        onSurface,
        onSurface90,
        onSurface80,
        onSurface70,
        onSurface60,
        onSurface50,
        onSurface40,
        onSurface30,
        onSurface20,
        onSurface10,
        onSurface5,
        isLight
    )

    fun update(other: SoptColors) {
        white = other.white
        purple300 = other.purple300
        purple200 = other.purple200
        purple100 = other.purple100
        pink300 = other.pink300
        pink200 = other.pink200
        pink100 = other.pink100
        mint300 = other.mint300
        mint200 = other.mint200
        mint100 = other.mint100
        error300 = other.error300
        error200 = other.error200
        error100 = other.error100
        access300 = other.access300
        onSurface = other.onSurface
        onSurface90 = other.onSurface90
        onSurface80 = other.onSurface80
        onSurface70 = other.onSurface70
        onSurface60 = other.onSurface60
        onSurface50 = other.onSurface50
        onSurface40 = other.onSurface40
        onSurface30 = other.onSurface30
        onSurface20 = other.onSurface20
        onSurface10 = other.onSurface10
        onSurface5 = other.onSurface5
        isLight = other.isLight
    }
}

fun soptLightColors(
    white: Color = Color.White,
    black: Color = Color.Black,
    black100: Color = Black100,
    purple300: Color = Purple300,
    purple200: Color = Purple200,
    purple100: Color = Purple100,
    pink300: Color = Pink300,
    pink200: Color = Pink200,
    pink100: Color = Pink100,
    mint300: Color = Mint300,
    mint200: Color = Mint200,
    mint100: Color = Mint100,
    error300: Color = Red300,
    error200: Color = Red200,
    error100: Color = Red100,
    access300: Color = Access300,
    onSurface: Color = Black,
    onSurface90: Color = Gray900,
    onSurface80: Color = Gray800,
    onSurface70: Color = Gray700,
    onSurface60: Color = Gray600,
    onSurface50: Color = Gray500,
    onSurface40: Color = Gray400,
    onSurface30: Color = Gray300,
    onSurface20: Color = Gray200,
    onSurface10: Color = Gray100,
    onSurface5: Color = Gray50
) = SoptColors(
    white,
    black,
    black100,
    purple300,
    purple200,
    purple100,
    pink300,
    pink200,
    pink100,
    mint300,
    mint200,
    mint100,
    error300,
    error200,
    error100,
    access300,
    onSurface,
    onSurface90,
    onSurface80,
    onSurface70,
    onSurface60,
    onSurface50,
    onSurface40,
    onSurface30,
    onSurface20,
    onSurface10,
    onSurface5,
    isLight = true
)

private val LocalSoptColors = staticCompositionLocalOf<SoptColors> {
    error("No SoptColors provided")
}

private val LocalSoptTypography = staticCompositionLocalOf<SoptTypography> {
    error("No SoptTypography provided")
}

/*
* SoptTheme
*
* Color에 접근하고 싶을때 SoptTheme.colors.primary 이런식으로 접근하면 됩니다.
* Typo를 변경하고 싶다면 SoptTheme.typography.h1 이런식으로 접근하면 됩니다.
* */
object SoptTheme {
    val colors: SoptColors @Composable get() = LocalSoptColors.current

    val typography: SoptTypography @Composable get() = LocalSoptTypography.current
}

@Composable
fun ProvideSoptColorsAndTypography(
    colors: SoptColors,
    typography: SoptTypography,
    content: @Composable () -> Unit
) {
    val provideColors = remember { colors.copy() }
    provideColors.update(colors)
    val provideTypography = remember { typography.copy() }
    provideTypography.update(typography)
    CompositionLocalProvider(
        LocalSoptColors provides provideColors,
        LocalSoptTypography provides provideTypography,
        content = content
    )
}

@Composable
fun SoptTheme(
    darkTheme: Boolean = false,
    content: @Composable () -> Unit
) {
    val colors = soptLightColors()
    val typography = SoptTypography()
    ProvideSoptColorsAndTypography(colors, typography) {
        MaterialTheme(content = content)
    }
}
