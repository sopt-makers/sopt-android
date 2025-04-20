package org.sopt.official.feature.auth.utils

import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation
import kotlin.math.max

fun phoneNumberVisualTransformation(): VisualTransformation = VisualTransformation { text ->
    val originalText = text.text

    // 변환된 문자열 구성
    val transformedText = buildString {
        originalText.forEachIndexed { index, char ->
            // 3번째 글자 뒤, 7번째 글자 뒤에 '-' 추가
            if (index == 3 || index == 7) {
                append('-')
            }
            append(char)
        }
    }

    // 커서 위치 매핑을 담당할 OffsetMapping
    val phoneOffsetMapping = object : OffsetMapping {
        /**
         * 원본 → 변환 (입력에 따라 실제 표시 문자열이 달라질 때
         * 원본 offset이 변환된 문자열에서 몇 번째 offset에 해당하는지 계산)
         */
        override fun originalToTransformed(offset: Int): Int {
            // 원본이 3글자 이하라면 하이픈 전혀 없음
            // 원본이 7글자 이하라면 하이픈 1개
            // 원본이 8글자 이상이라면 하이픈 2개
            val hyphenCount = when {
                offset <= 3 -> 0
                offset <= 7 -> 1
                else -> 2
            }
            return offset + hyphenCount
        }

        /**
         * 변환 → 원본 (표시 문자열 상 offset이 실제 원본에서는 몇 번째에 해당하는지 계산)
         */
        override fun transformedToOriginal(offset: Int): Int {
            // 변환된 문자열에서 하이픈 위치: index 3, 8 (0-based)
            // offset 이 3 미만이면(=하이픈 이전이면) 그대로
            // offset 이 3 이상 8 미만이면 하이픈 1개 반영
            // offset 이 8 이상이면 하이픈 2개 반영
            val hyphenAdjustment = when {
                offset <= 4 -> 0
                offset <= 9 -> 1
                else -> 2
            }
            return max(offset - hyphenAdjustment, 0)
        }
    }

    TransformedText(
        text = AnnotatedString(transformedText),
        offsetMapping = phoneOffsetMapping
    )
}