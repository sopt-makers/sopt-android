package org.sopt.official.common.util

import android.os.Parcelable
import androidx.core.os.BundleCompat
import androidx.fragment.app.Fragment
import java.io.Serializable
import kotlin.properties.ReadOnlyProperty

fun intArgs() = ReadOnlyProperty<Fragment, Int> { thisRef, property ->
    thisRef.requireArguments().getInt(property.name)
}

fun longArgs() = ReadOnlyProperty<Fragment, Long> { thisRef, property ->
    thisRef.requireArguments().getLong(property.name)
}

fun boolArgs() = ReadOnlyProperty<Fragment, Boolean> { thisRef, property ->
    thisRef.requireArguments().getBoolean(property.name)
}

fun stringArgs() = ReadOnlyProperty<Fragment, String> { thisRef, property ->
    thisRef.requireArguments().getString(property.name, "")
}

inline fun <reified P : Parcelable> parcelableArgs() = ReadOnlyProperty<Fragment, P?> { thisRef, property ->
    BundleCompat.getParcelable(thisRef.requireArguments(), property.name, P::class.java)
}

inline fun <reified S : Serializable> serializableArgs() = ReadOnlyProperty<Fragment, S?> { thisRef, property ->
    BundleCompat.getSerializable(thisRef.requireArguments(), property.name, S::class.java)
}
