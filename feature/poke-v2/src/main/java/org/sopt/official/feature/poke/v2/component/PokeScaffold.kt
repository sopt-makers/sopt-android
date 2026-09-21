package org.sopt.official.feature.poke.v2.component

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.ScaffoldDefaults
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLayoutDirection
import org.sopt.official.mds.theme.SoptTheme

/**
 * 콕찌르기 화면 공통 Scaffold.
 *
 * @param snackbarHostState      스낵바 상태. NavHost 레벨에서 하나 만들어 모든 화면이 공유
 * @param isSnackbarHostVisible  false 이면 스낵바 호스트를 그리지 않음
 * @param contentWindowInsets    콘텐츠에 적용할 window insets
 * @param content                화면 콘텐츠
 */
@Composable
internal fun PokeScaffold(
    snackbarHostState: SnackbarHostState,
    modifier: Modifier = Modifier,
    isSnackbarHostVisible: Boolean = true,
    contentWindowInsets: WindowInsets = ScaffoldDefaults.contentWindowInsets,
    content: @Composable (innerPadding: PaddingValues) -> Unit,
) {
    val layoutDirection = LocalLayoutDirection.current
    val contentPadding = contentWindowInsets.asPaddingValues()

    Scaffold(
        contentWindowInsets = contentWindowInsets,
        snackbarHost = {
            Box(modifier = Modifier.fillMaxSize()) {
                if (isSnackbarHostVisible) {
                    PokeSnackBarHost(
                        hostState = snackbarHostState,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(
                                top = contentPadding.calculateTopPadding() + SoptTheme.spacing.s16,
                                start = contentPadding.calculateStartPadding(layoutDirection) + SoptTheme.spacing.s16,
                                end = contentPadding.calculateEndPadding(layoutDirection) + SoptTheme.spacing.s16,
                            )
                            .align(Alignment.TopCenter),
                    )
                }
            }
        },
        containerColor = SoptTheme.colors.bg.layer.basement,
        modifier = modifier.fillMaxSize(),
        content = content,
    )
}
