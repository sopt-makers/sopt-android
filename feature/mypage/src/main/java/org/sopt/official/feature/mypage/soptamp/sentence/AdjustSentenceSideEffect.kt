package org.sopt.official.feature.mypage.soptamp.sentence

import androidx.compose.runtime.Immutable

@Immutable
sealed class AdjustSentenceSideEffect {
    data object NavigateToMyPage : AdjustSentenceSideEffect()
}