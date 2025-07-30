package org.sopt.official.common.util

import android.content.Context
import android.content.Intent
import androidx.core.net.toUri
import timber.log.Timber

private const val PLAY_STORE_URL =
    "https://play.google.com/store/apps/details?id="

fun Context.getVersionName(): String? = runCatching {
    packageManager.getPackageInfo(packageName, 0).versionName
}.onFailure(Timber::e).getOrNull()

fun Context.launchPlayStore() = runCatching {
    val playStoreUri = (PLAY_STORE_URL + packageName).toUri()
    Intent(Intent.ACTION_VIEW).apply { data = playStoreUri }
}.onSuccess {
    startActivity(it)
}.onFailure(Timber::e)
