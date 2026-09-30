/*
 * MIT License
 * Copyright 2025-2026 SOPT - Shout Our Passion Together
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
package org.sopt.official.feature.home.component

import androidx.annotation.ColorRes
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment.Companion.Center
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kotlinx.collections.immutable.ImmutableList
import org.sopt.official.feature.home.model.HomeUserSoptLogDashboardModel
import org.sopt.official.mds.components.tag.MdsTag
import org.sopt.official.mds.components.tag.MdsTagEmphasis
import org.sopt.official.mds.components.tag.MdsTagShape
import org.sopt.official.mds.components.tag.MdsTagSize
import org.sopt.official.mds.components.tag.MdsTagType
import org.sopt.official.mds.theme.SoptTheme

@Composable
internal fun HomeGenerationChips(
    homeUserSoptLogDashboardModel: HomeUserSoptLogDashboardModel,
    modifier: Modifier = Modifier,
) {
    val tagType = if (homeUserSoptLogDashboardModel.isActivated) MdsTagType.PRIMARY else MdsTagType.DEFAULT

    Row(modifier = modifier) {
        RecentGenerationChip(
            tagType = tagType,
            text = homeUserSoptLogDashboardModel.recentGenerationDescription
        )

        Spacer(modifier = Modifier.width(width = 8.dp))

        LastGenerationChips(homeUserSoptLogDashboardModel.lastGenerations)
    }
}

@Composable
internal fun RecentGenerationChip(
    text: String,
    tagType: MdsTagType,
    modifier: Modifier = Modifier
) {
    MdsTag(
        text = text,
        modifier = modifier,
        type = tagType,
        emphasis = MdsTagEmphasis.SOLID,
        size = MdsTagSize.SMALL,
        shape = MdsTagShape.PILL
    )
}

@Composable
private fun LastGenerationChips(generations: ImmutableList<Long>) {
    generations.forEachIndexed { index, generation ->
        when (index) {
            0 -> GenerationChip(
                chipColor = SoptTheme.colors.bg.neutral.default,
                textColor = SoptTheme.colors.fg.neutral.bold,
                text = generation.toString(),
            )

            1 -> GenerationChip(
                chipColor = SoptTheme.colors.bg.neutral.subtle,
                textColor = SoptTheme.colors.fg.neutral.bold,
                text = generation.toString(),
            )

            2 -> GenerationChip(
                chipColor = SoptTheme.colors.bg.neutral.ghost,
                textColor = SoptTheme.colors.fg.neutral.default,
                text = generation.toString(),
            )

            3 -> GenerationChip(
                chipColor = SoptTheme.colors.bg.neutral.ghost,
                textColor = SoptTheme.colors.fg.neutral.subtle,
                text = generation.toString(),
            )

            4 -> GenerationChip(
                chipColor = SoptTheme.colors.bg.neutral.ghost,
                textColor = SoptTheme.colors.fg.neutral.subtle,
                text = generation.toString(),
            )

            5 -> {
                GenerationChip(
                    chipColor = SoptTheme.colors.bg.neutral.ghost,
                    textColor = SoptTheme.colors.fg.neutral.bold,
                    text = "+1",
                )
                return@forEachIndexed
            }
        }
        Spacer(modifier = Modifier.width(width = 4.dp))
    }
}

@Composable
private fun GenerationChip(
    @ColorRes chipColor: Color,
    @ColorRes textColor: Color,
    text: String,
    modifier: Modifier = Modifier,
) {
    Box(
        contentAlignment = Center,
        modifier = modifier
            .background(
                color = chipColor,
                shape = CircleShape,
            )
            .size(size = 24.dp)
    ) {
        Text(
            text = text,
            style = SoptTheme.typography.label4,
            color = textColor
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun HomeGenerationChipsPreview() {
    SoptTheme {
        HomeGenerationChips(
            homeUserSoptLogDashboardModel = HomeUserSoptLogDashboardModel()
        )
    }
}
