/*
 * MIT License
 * Copyright 2025 SOPT - Shout Our Passion Together
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
