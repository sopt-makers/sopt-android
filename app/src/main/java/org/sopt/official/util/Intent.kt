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
package org.sopt.official.util

import android.app.Activity
import android.content.Intent
import android.os.Build
import android.os.Parcelable
import java.io.Serializable
import kotlin.properties.ReadOnlyProperty

fun stringExtra(defaultValue: String? = null) =
    ReadOnlyProperty<Activity, String?> { thisRef, property ->
        if (defaultValue == null) {
            thisRef.intent.extras?.getString(property.name)
        } else {
            thisRef.intent.extras?.getString(property.name, defaultValue)
        }
    }

inline fun <reified T : Serializable?> Intent.serializableExtra(key: String): T? {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        getSerializableExtra(key, T::class.java)
    } else {
        getSerializableExtra(key) as? T
    }
}

inline fun <reified S : Serializable> serializableExtra(defaultValue: S? = null) =
    ReadOnlyProperty<Activity, S?> { thisRef, property ->
        thisRef.intent.serializableExtra(property.name) ?: defaultValue
    }

inline fun <reified T : Parcelable> parcelableExtra(defaultValue: T? = null) =
    ReadOnlyProperty<Activity, T?> { thisRef, property ->
        thisRef.intent.getParcelableExtra(property.name) ?: defaultValue
    }
