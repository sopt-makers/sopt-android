/*
 * MIT License
 * Copyright 2023-2025 SOPT - Shout Our Passion Together
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package org.sopt.official.auth.utils

import android.content.ActivityNotFoundException
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.content.pm.PackageManager
import android.content.pm.ResolveInfo
import android.net.Uri
import android.os.Build
import androidx.browser.customtabs.CustomTabsClient
import androidx.browser.customtabs.CustomTabsIntent
import androidx.browser.customtabs.CustomTabsService
import androidx.browser.customtabs.CustomTabsServiceConnection
import org.sopt.official.auth.PlaygroundError

object PlaygroundCustomTabManager {

    fun open(context: Context, uri: Uri): Result<Unit> {
        val urlOpenResult = kotlin.runCatching { buildCustomTabIntent().launchUrl(context, uri) }
        return when (val exception = urlOpenResult.exceptionOrNull()) {
            null -> urlOpenResult
            is ActivityNotFoundException -> {
                Result.failure(PlaygroundError.NotSupported("기기에서 설치된 Browser를 찾을 수 없습니다."))
            }

            else -> Result.failure(exception)
        }
    }

    fun openWithDefaultBrowser(context: Context, uri: Uri): Result<ServiceConnection> {
        val packageName = resolveCustomTabPackage(context, uri)
            ?: return Result.failure(PlaygroundError.NotSupported("기기에서 설치된 Browser를 찾을 수 없습니다."))
        if (!packageName.isChromePackageName()) {
            return Result.failure(PlaygroundError.NotSupported("기본 앱이 Chrome 이 아닙니다."))
        }
        PlaygroundLog.d("open $packageName as custom tab browser")
        val playgroundConnection =
            PlaygroundServiceConnection(context, buildCustomTabIntent(), uri, packageName)
        val boundPlaygroundService = CustomTabsClient.bindCustomTabsService(
            context,
            packageName,
            playgroundConnection
        )
        return if (boundPlaygroundService) {
            Result.success(playgroundConnection)
        } else {
            Result.failure(PlaygroundError.NotSupported("기기에서 설치된 Browser를 찾을 수 없습니다."))
        }
    }

    private fun resolveCustomTabPackage(context: Context, uri: Uri): String? {
        var chromePackage: String? = null

        val resolveActivityPackageName =
            resolveCustomTabInfo(context, uri)?.activityInfo?.packageName
        queryCustomTabsConnectionServices(context).forEach {
            if (chromePackage == null && it.serviceInfo.packageName.isChromePackageName()) {
                chromePackage = it.serviceInfo.packageName
            }
            if (it.serviceInfo.packageName == resolveActivityPackageName) {
                return resolveActivityPackageName
            }
        }
        return chromePackage
    }

    private fun resolveCustomTabInfo(context: Context, uri: Uri): ResolveInfo? = Intent(
        Intent.ACTION_VIEW,
        uri
    ).let {
        context.packageManager.resolveActivityInfo(it)
    }

    private fun queryCustomTabsConnectionServices(context: Context): List<ResolveInfo> = Intent().setAction(
        CustomTabsService.ACTION_CUSTOM_TABS_CONNECTION
    ).let {
        context.packageManager.queryServicesInfo(it)
    }

    private fun String.isChromePackageName(): Boolean = arrayOf(
        "com.android.chrome",
        "com.chrome.beta",
        "com.chrome.dev"
    ).contains(this)

    private fun buildCustomTabIntent() = CustomTabsIntent.Builder()
        .setUrlBarHidingEnabled(true)
        .setShowTitle(false)
        .build()

    class PlaygroundServiceConnection(
        private val context: Context,
        private val customTabsIntent: CustomTabsIntent,
        private val uri: Uri,
        private val packageName: String,
    ) : CustomTabsServiceConnection() {
        override fun onServiceDisconnected(name: ComponentName?) {
            PlaygroundLog.d("Playground Service Connection : onServiceDisconnected - $name")
        }

        override fun onCustomTabsServiceConnected(name: ComponentName, client: CustomTabsClient) {
            PlaygroundLog.d("Playground Service Connection : onCustomTabsServiceConnected - $name")
            customTabsIntent.intent.apply {
                data = uri
                setPackage(packageName)
            }.run {
                context.startActivity(customTabsIntent.intent)
            }
        }
    }
}

fun PackageManager.resolveActivityInfo(intent: Intent): ResolveInfo? {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        resolveActivity(
            intent,
            PackageManager.ResolveInfoFlags.of(PackageManager.MATCH_DEFAULT_ONLY.toLong())
        )
    } else {
        resolveActivity(intent, PackageManager.MATCH_DEFAULT_ONLY)
    }
}

fun PackageManager.queryServicesInfo(intent: Intent): List<ResolveInfo> {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        queryIntentServices(
            intent,
            PackageManager.ResolveInfoFlags.of(PackageManager.SIGNATURE_MATCH.toLong())
        )
    } else {
        queryIntentServices(intent, PackageManager.SIGNATURE_MATCH)
    }
}
