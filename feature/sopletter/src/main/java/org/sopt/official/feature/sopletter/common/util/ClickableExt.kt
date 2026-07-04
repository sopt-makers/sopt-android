package org.sopt.official.feature.sopletter.common.util

import androidx.compose.ui.Modifier
import org.sopt.official.common.util.noRippleClickable

internal fun Modifier.consumeClicks(): Modifier =
    noRippleClickable(onClick = {})
