/*
 * MIT License
 * Copyright 2023-2024 SOPT - Shout Our Passion Together
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package org.sopt.official.util

import android.app.AlertDialog
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import androidx.annotation.StringRes
import androidx.constraintlayout.widget.ConstraintLayout
import org.sopt.official.R
import org.sopt.official.common.util.drawableOf
import org.sopt.official.databinding.LayoutDialogOneButtonBinding

class AlertDialogOneButton(context: Context) : ConstraintLayout(context) {
    private val builder: AlertDialog.Builder = AlertDialog.Builder(context)

    val binding = LayoutDialogOneButtonBinding.inflate(LayoutInflater.from(context))

    private var dialog: AlertDialog? = null

    init {
        dialog = builder.setView(binding.root).create()
    }

    fun setTitle(@StringRes titleId: Int): AlertDialogOneButton {
        binding.title.text = context.getText(titleId)
        return this
    }

    fun setTitle(title: CharSequence): AlertDialogOneButton {
        binding.title.text = title
        return this
    }

    fun setSubtitle(@StringRes subtitleId: Int): AlertDialogOneButton {
        binding.subtitle.text = context.getText(subtitleId)
        return this
    }

    fun setSubtitle(subtitle: CharSequence): AlertDialogOneButton {
        binding.subtitle.text = subtitle
        return this
    }

    fun setPositiveButton(@StringRes textId: Int, listener: (view: View) -> (Unit) = { dismiss() }): AlertDialogOneButton {
        binding.buttonComfirm.apply {
            text = context.getText(textId)
            setOnClickListener(listener)
        }
        return this
    }

    fun setPositiveButton(text: CharSequence, listener: (view: View) -> (Unit) = { dismiss() }): AlertDialogOneButton {
        binding.buttonComfirm.apply {
            this.text = text
            setOnClickListener(listener)
        }
        return this
    }

    fun show() {
        dialog?.window?.setBackgroundDrawable(
            context.drawableOf(R.drawable.rectangle_radius_10).apply {
                this?.setTint(context.getColor(org.sopt.official.designsystem.R.color.black_60))
            }
        )

        dialog?.show()
    }

    private fun dismiss() {
        dialog?.dismiss()
    }
}
