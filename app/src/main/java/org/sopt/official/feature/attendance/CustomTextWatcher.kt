package org.sopt.official.feature.attendance

import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText

class CustomTextWatcher(private val etNext: EditText, private val etNow: EditText) : TextWatcher {
    override fun afterTextChanged(editable: Editable) {
        val text = editable.toString()
        if (text.length == 1) {
            etNext.requestFocus()
            etNow.isEnabled = false
        }
    }

    override fun beforeTextChanged(arg0: CharSequence, arg1: Int, arg2: Int, arg3: Int) {}
    override fun onTextChanged(arg0: CharSequence, arg1: Int, arg2: Int, arg3: Int) {}
}