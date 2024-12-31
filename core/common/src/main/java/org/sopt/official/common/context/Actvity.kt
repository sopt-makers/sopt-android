package org.sopt.official.common.context

import android.content.Context
import android.content.ContextWrapper

inline fun <reified T> Context.findActivity(): T? {
    var context = this
    while (context is ContextWrapper) {
        if (context is T) {
            return context
        }
        context = context.baseContext
    }
    return null
}
