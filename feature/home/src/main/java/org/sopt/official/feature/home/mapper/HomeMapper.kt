package org.sopt.official.feature.home.mapper

import org.sopt.official.domain.home.model.ReviewForm
import org.sopt.official.feature.home.model.HomeSurveyData

internal fun ReviewForm.toModel(): HomeSurveyData = HomeSurveyData(
    title = title,
    description = subTitle,
    buttonText = actionButtonName,
    surveyLink = linkUrl
)
