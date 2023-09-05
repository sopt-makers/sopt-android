package org.sopt.official.util.widget

import android.content.Context
import androidx.annotation.ColorRes
import androidx.compose.ui.graphics.Color
import androidx.glance.unit.ColorProvider

class LocalColorProvider(@ColorRes val resId: Int) : ColorProvider {
    override fun getColor(context: Context): Color {
        return Color(context.getColor(resId))
    }
}
