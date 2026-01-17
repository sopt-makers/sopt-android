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
import dev.zacsweers.metro.Inject
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import timber.log.Timber

@Inject
class PlaygroundLog private constructor(val isDebug: Boolean) {

    companion object {
        @Volatile
        private var instance: PlaygroundLog? = null

        private const val TAG = "Playground"

        fun init(applicationInfo: ApplicationInfo) {
            instance ?: synchronized(this) {
                instance ?: PlaygroundLog(isDebuggable(applicationInfo)).also {
                    instance = it
                }
            }
        }

        private fun isDebuggable(applicationInfo: ApplicationInfo): Boolean = try {
            (0 != (applicationInfo.flags and ApplicationInfo.FLAG_DEBUGGABLE))
        } catch (exception: PackageManager.NameNotFoundException) {
            false
        }

        fun v(log: Any?) {
            if (instance?.isDebug == true) Timber.tag(TAG).v(buildLogMessage(log))
        }

        fun d(log: Any?) {
            if (instance?.isDebug == true) Timber.tag(TAG).d(buildLogMessage(log))
        }

        fun i(log: Any?) {
            if (instance?.isDebug == true) Timber.tag(TAG).i(buildLogMessage(log))
        }

        fun w(log: Any?) {
            if (instance?.isDebug == true) Timber.tag(TAG).w(buildLogMessage(log))
        }

        fun e(log: Any?) {
            if (instance?.isDebug == true) Timber.tag(TAG).e(buildLogMessage(log))
        }

        private fun buildLogMessage(log: Any?): String {
            val fileName = Thread.currentThread().stackTrace[4].fileName
            val methodName = Thread.currentThread().stackTrace[4].methodName
            val thread = Thread.currentThread().name
            return "[${thread}>${fileName}::${methodName}] $log"
        }
    }
}
