package org.sopt.official.feature.sopletter.common.util

private val INVALID_FILE_NAME_REGEX = Regex("""[\\/:*?"<>|]""")
private val MULTIPLE_WHITESPACE_REGEX = Regex("""\s+""")

internal fun String.toSafeFileName(defaultName: String = "sopletter"): String =
    trim()
        .replace(INVALID_FILE_NAME_REGEX, "_")
        .replace(MULTIPLE_WHITESPACE_REGEX, " ")
        .take(50)
        .ifBlank { defaultName }
