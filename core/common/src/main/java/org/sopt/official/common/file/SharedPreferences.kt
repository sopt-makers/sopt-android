package org.sopt.official.common.file

import android.content.Context
import android.content.SharedPreferences

fun createSharedPreference(fileName: String, context: Context): SharedPreferences =
    context.getSharedPreferences(fileName, Context.MODE_PRIVATE)
