package org.sopt.official.feature.appjamtamp.model

import androidx.annotation.DrawableRes
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import org.sopt.official.designsystem.Mint100
import org.sopt.official.designsystem.Mint300
import org.sopt.official.designsystem.Orange300
import org.sopt.official.designsystem.Pink100
import org.sopt.official.designsystem.Pink300
import org.sopt.official.designsystem.Purple100
import org.sopt.official.designsystem.Purple300
import org.sopt.official.designsystem.SoptTheme
import org.sopt.official.domain.appjamtamp.entity.MissionLevel
import org.sopt.official.feature.appjamtamp.R

enum class Stamp(
    val missionLevel: MissionLevel,
    @field:DrawableRes val lottieImage: Int,
) {
    LEVEL1(MissionLevel.of(1), R.drawable.pinkstamp_image),
    LEVEL2(MissionLevel.of(2), R.drawable.purplestamp_image),
    LEVEL3(MissionLevel.of(3), R.drawable.greenstamp_image),
    LEVEL10(MissionLevel.of(10), R.drawable.orangestamp_iamge),
    ;

    val starColor: Color
        @Composable get() =
            when (this) {
                LEVEL1 -> Pink300
                LEVEL2 -> Purple300
                LEVEL3 -> Mint300
                LEVEL10 -> Orange300
            }

    val background: Color
        @Composable get() =
            when (this) {
                LEVEL1 -> Pink100
                LEVEL2 -> Purple100
                LEVEL3 -> Mint100
                LEVEL10 -> Orange300
            }

    fun hasStampLevel(level: MissionLevel): Boolean {
        return this.missionLevel == level
    }

    companion object {
        val defaultStarColor: Color
            @Composable get() = SoptTheme.colors.onSurface30

        fun findStampByLevel(level: MissionLevel): Stamp =
            entries.find {
                it.hasStampLevel(level)
            } ?: throw IllegalArgumentException("$level 에 해당하는 Stamp 가 없습니다.")
    }
}
