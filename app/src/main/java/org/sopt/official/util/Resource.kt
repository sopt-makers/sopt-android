package org.sopt.official.util

import android.content.Context
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.appcompat.content.res.AppCompatResources
import androidx.fragment.app.Fragment

fun Context.stringOf(@StringRes id: Int) = getString(id)

fun Context.stringOf(@StringRes id: Int, arg1: String) = getString(id, arg1)

fun Context.stringOf(@StringRes id: Int, vararg args: String) = getString(id, args)

fun Context.drawableOf(@DrawableRes id: Int) = AppCompatResources.getDrawable(this, id)

fun Fragment.stringOf(@StringRes id: Int) = requireContext().stringOf(id)

fun Fragment.drawableOf(@DrawableRes id: Int) = requireContext().drawableOf(id)
