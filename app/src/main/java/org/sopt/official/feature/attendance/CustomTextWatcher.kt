package org.sopt.official.feature.attendance

import android.widget.EditText
import androidx.core.widget.doAfterTextChanged

fun EditText.requestFocusAfterTextChanged(to: EditText, otherLogic: () -> Unit = {}) {
    doAfterTextChanged {
        if (it?.length == 1) {
            to.requestFocus()
            this.isEnabled = false
        }
        otherLogic()
    }
}
