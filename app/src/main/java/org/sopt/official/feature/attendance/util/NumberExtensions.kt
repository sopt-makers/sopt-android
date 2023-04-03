package org.sopt.official.feature.attendance.util

import android.content.res.Resources

fun Int.dpToPx(): Int = this * Resources.getSystem().displayMetrics.density.toInt()
fun Int.pxToDp(): Int = (this / Resources.getSystem().displayMetrics.density).toInt()