/*
 * Copyright 2022-2023 SOPT - Shout Our Passion Together
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
package org.sopt.official.config.initializer

import android.content.Context
import android.util.Log
import androidx.startup.Initializer
import com.google.firebase.crashlytics.FirebaseCrashlytics
import org.sopt.official.BuildConfig
import timber.log.Timber

class TimberInitializer : Initializer<Unit> {
    override fun create(context: Context) {
        Timber.plant(if (BuildConfig.DEBUG) SOPTDebugTree() else SOPTTree())
    }

    private class SOPTDebugTree : Timber.DebugTree() {
        override fun createStackElementTag(element: StackTraceElement): String {
            return "SOPT://${element.fileName}:${element.lineNumber}#${element.methodName}"
        }
    }

    private class SOPTTree : Timber.Tree() {
        override fun log(priority: Int, tag: String?, message: String, t: Throwable?) {
            when (priority) {
                Log.WARN, Log.ERROR -> t?.let {
                    val newThrowable = Throwable().initCause(t).apply {
                        stackTrace = Thread.currentThread().stackTrace
                    }
                    FirebaseCrashlytics.getInstance().recordException(newThrowable)
                }
                else -> FirebaseCrashlytics.getInstance().log("$tag | $message")
            }
        }
    }

    override fun dependencies(): MutableList<Class<out Initializer<*>>> {
        return mutableListOf()
    }
}
