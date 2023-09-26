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
