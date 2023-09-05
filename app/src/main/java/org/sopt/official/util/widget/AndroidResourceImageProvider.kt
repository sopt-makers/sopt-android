package org.sopt.official.util.widget

import androidx.annotation.DrawableRes
import androidx.glance.ImageProvider

class AndroidResourceImageProvider(@DrawableRes val resId: Int) : ImageProvider {
    override fun toString() = "AndroidResourceImageProvider(resId=$resId)"
}
