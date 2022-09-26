package org.sopt.official.feature.update

import android.content.Intent
import androidx.lifecycle.DefaultLifecycleObserver

fun interface OnUpdateCompleteListener {
    fun onComplete()
}

interface InAppUpdateManager : DefaultLifecycleObserver {
    fun setOnFlexibleUpdateCompleted(onUpdateCompleteListener: OnUpdateCompleteListener)
    fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?)
}
