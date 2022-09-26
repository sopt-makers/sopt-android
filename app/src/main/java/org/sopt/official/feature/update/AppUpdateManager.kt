package org.sopt.official.feature.update

import android.content.Intent

fun interface OnUpdateCompleteListener {
    fun onComplete()
}

interface AppUpdateManager {
    fun launchUpdateFlow()
    fun onFlexibleUpdateCompleted(onUpdateCompleteListener: OnUpdateCompleteListener)
    fun onResume()
    fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?)
    fun onDestroy()
}
