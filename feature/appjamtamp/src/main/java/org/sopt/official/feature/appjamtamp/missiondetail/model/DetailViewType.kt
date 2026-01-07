package org.sopt.official.feature.appjamtamp.missiondetail.model

import androidx.annotation.DrawableRes
import org.sopt.official.feature.appjamtamp.R

enum class DetailViewType(
    @field:DrawableRes val toolbarIcon: Int? = null
) {
    WRITE,
    READ_ONLY,
    COMPLETE(R.drawable.ic_write_32),
    EDIT(R.drawable.ic_delete_32)
}
