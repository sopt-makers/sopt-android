package org.sopt.official.designsystem.style

import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.graphics.Color

@Stable
class SoptColors(
    primary: Color,
    onPrimary: Color,
    background: Color,
    onBackground: Color,
    surface: Color,
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
    var primary by mutableStateOf(primary)
        private set
    var onPrimary by mutableStateOf(onPrimary)
        private set
    var background by mutableStateOf(background)
        private set
    var onBackground by mutableStateOf(onBackground)
        private set
    var surface by mutableStateOf(surface)
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
        primary,
        onPrimary,
        background,
        onBackground,
        surface,
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
        primary = other.primary
        onPrimary = other.onPrimary
        background = other.background
        onBackground = other.onBackground
        surface = other.surface
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
    primary: Color = Blue500,
    onPrimary: Color = Gray900,
    background: Color = White,
    onBackground: Color = Black,
    surface: Color = White,
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
    primary,
    onPrimary,
    background,
    onBackground,
    surface,
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
