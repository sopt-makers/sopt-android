package org.sopt.official.common.util

fun String.extractQueryParameter(paramName: String): String {
    val pattern = "$paramName=([^&]+)".toRegex()
    val matchResult = pattern.find(this)
    return matchResult?.groupValues?.get(1) ?: ""
}