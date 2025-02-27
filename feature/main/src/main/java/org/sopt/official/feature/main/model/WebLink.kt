package org.sopt.official.feature.main.model

import org.sopt.official.common.BuildConfig

internal object SoptWebLink {
    const val OFFICIAL_HOMEPAGE = "https://sopt.org"
    const val PROJECT = "https://sopt.org/project"
    const val REVIEW = "https://sopt.org/review"
    const val INSTAGRAM = "https://www.instagram.com/sopt_official"
}

internal object PlaygroundWebLink {
    const val OFFICIAL_HOMEPAGE = BuildConfig.PLAYGROUND_API
    const val MEMBER = OFFICIAL_HOMEPAGE + "members"
    const val EDIT_PROFILE = OFFICIAL_HOMEPAGE + "members/edit"
    const val PROJECT = OFFICIAL_HOMEPAGE + "projects"
    const val GROUP_STUDY = OFFICIAL_HOMEPAGE + "group"
}
