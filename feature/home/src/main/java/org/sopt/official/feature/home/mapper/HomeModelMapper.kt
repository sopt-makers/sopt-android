package org.sopt.official.feature.home.mapper

import org.sopt.official.domain.home.model.FloatingToast
import org.sopt.official.domain.home.model.ReviewForm
import org.sopt.official.feature.home.model.HomeFloatingToastData
import org.sopt.official.feature.home.model.HomeSurveyData

internal fun ReviewForm.toModel(): HomeSurveyData = HomeSurveyData(
    title = title,
    description = subTitle,
    buttonText = actionButtonName,
    surveyLink = linkUrl
)

internal fun FloatingToast.toModel(): HomeFloatingToastData = HomeFloatingToastData(
    imageUrl = imageUrl,
    title = title,
    toastDescription = expandedSubTitle,
    buttonDescription = collapsedSubtitle,
    buttonText = actionButtonName,
    linkUrl = linkUrl,
    active = active
)