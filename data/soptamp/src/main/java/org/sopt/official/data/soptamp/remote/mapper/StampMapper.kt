package org.sopt.official.data.soptamp.remote.mapper

import org.sopt.official.data.soptamp.remote.model.request.StampRequest
import org.sopt.official.domain.soptamp.model.Stamp

internal fun Stamp.toData(): StampRequest = StampRequest(
    missionId = missionId,
    image = image,
    contents = contents,
    activityDate = activityDate
)
