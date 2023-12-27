package org.sopt.official.feature.poke.util

import android.graphics.drawable.GradientDrawable
import android.widget.ImageView
import androidx.core.content.ContextCompat
import org.sopt.official.common.util.dp
import org.sopt.official.domain.poke.type.PokeFriendType

fun ImageView.setRelationStrokeColor(relationName: String) {
    (background as GradientDrawable).setStroke(
        2.dp,
        ContextCompat.getColor(
            context,
            when (relationName) {
                PokeFriendType.NEW.readableName -> org.sopt.official.designsystem.R.color.mds_blue_900
                PokeFriendType.BEST_FRIEND.readableName -> org.sopt.official.designsystem.R.color.mds_information
                PokeFriendType.SOULMATE.readableName -> org.sopt.official.designsystem.R.color.mds_secondary
                else -> org.sopt.official.designsystem.R.color.transparent
            }
        )
    )
}