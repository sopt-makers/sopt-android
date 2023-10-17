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

import android.content.Context
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.appcompat.content.res.AppCompatResources
import androidx.fragment.app.Fragment

fun Context.stringOf(@StringRes id: Int) = getString(id)

fun Context.stringOf(@StringRes id: Int, arg1: String) = getString(id, arg1)

fun Context.stringOf(@StringRes id: Int, vararg args: String) = getString(id, *args)

fun Context.drawableOf(@DrawableRes id: Int) = AppCompatResources.getDrawable(this, id)

fun Context.colorOf(@ColorRes id: Int) = getColor(id)

fun Fragment.stringOf(@StringRes id: Int) = requireContext().stringOf(id)

fun Fragment.drawableOf(@DrawableRes id: Int) = requireContext().drawableOf(id)

fun Fragment.colorOf(@ColorRes id: Int) = requireContext().getColor(id)
