package org.sopt.official.feature.sopletter.main.model

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

private val memoDateFormatter: DateTimeFormatter = DateTimeFormatter.ofPattern("MM.dd")

internal fun String.formatCreatedAt(): String = runCatching {
    LocalDateTime.parse(this).format(memoDateFormatter)
}.getOrDefault("")
