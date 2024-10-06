package org.sopt.official.feature.mypage.soptamp.sentence

sealed class AdjustSentenceSideEffect {
    data object NavigateToMyPage : AdjustSentenceSideEffect()
}