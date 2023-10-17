/*
 * Copyright 2023 SOPT - Shout Our Passion Together
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.sopt.official.auth.utils

import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import android.util.Log

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
            if (instance?.isDebug == true) Log.v(TAG, buildLogMessage(log))
        }

        fun d(log: Any?) {
            if (instance?.isDebug == true) Log.d(TAG, buildLogMessage(log))
        }

        fun i(log: Any?) {
            if (instance?.isDebug == true) Log.i(TAG, buildLogMessage(log))
        }

        fun w(log: Any?) {
            if (instance?.isDebug == true) Log.w(TAG, buildLogMessage(log))
        }

        fun e(log: Any?) {
            if (instance?.isDebug == true) Log.e(TAG, buildLogMessage(log))
        }

        private fun buildLogMessage(log: Any?): String {
            val fileName = Thread.currentThread().stackTrace[4].fileName
            val methodName = Thread.currentThread().stackTrace[4].methodName
            val thread = Thread.currentThread().name
            return "[${ thread }>${ fileName }::${ methodName }] $log"
        }
    }
}
