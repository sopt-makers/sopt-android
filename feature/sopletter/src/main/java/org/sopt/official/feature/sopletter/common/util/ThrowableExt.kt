package org.sopt.official.feature.sopletter.common.util

import retrofit2.HttpException

internal fun Throwable.isSopletterNotFound(): Boolean =
    this is HttpException && code() == 404
