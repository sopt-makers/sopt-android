package org.sopt.official.designsystem

import android.app.AlertDialog
import android.content.Context
import android.view.View
import androidx.annotation.StringRes
import org.sopt.official.R

class AlertDialogPositiveNegative(private val context: Context) {
    private val builder: AlertDialog.Builder by lazy {
        AlertDialog.Builder(context).setView(view)
    }

    private val view: View by lazy {
        View.inflate(context, R.layout.layout_dialog_negative_positive, null)
    }

    private var dialog: AlertDialog? = null

    fun setTitle(@StringRes titleId: Int): AlertDialogPositiveNegative {
        view.title.text = context.getText(titleId)
        return this
    }

    fun setTitle(title: CharSequence): AlertDialogPositiveNegative {
        view.title.text = title
        return this
    }

    fun setSubtitle(@StringRes subtitleId: Int): AlertDialogPositiveNegative {
        view.subtitle.text = context.getText(subtitleId)
        return this
    }

    fun setSubtitle(message: CharSequence): AlertDialogPositiveNegative {
        view.subtitle.text = message
        return this
    }

    fun setPositiveButton(@StringRes textId: Int, listener: (view: View) -> (Unit) = { dismiss() }): AlertDialogPositiveNegative {
        view.positiveButton.apply {
            text = context.getText(textId)
            setOnClickListener(listener)
        }
        return this
    }

    fun setPositiveButton(text: CharSequence, listener: (view: View) -> (Unit) = { dismiss() }): AlertDialogPositiveNegative {
        view.positiveButton.apply {
            this.text = text
            setOnClickListener(listener)
        }
        return this
    }

    fun setNegativeButton(@StringRes textId: Int, listener: (view: View) -> (Unit) = { dismiss() }): AlertDialogPositiveNegative {
        view.negativeButton.apply {
            text = context.getText(textId)
            this.text = text
            setOnClickListener(listener)
        }
        return this
    }

    fun setNegativeButton(text: CharSequence, listener: (view: View) -> (Unit) = { dismiss() }): AlertDialogPositiveNegative {
        view.negativeButton.apply {
            this.text = text
            setOnClickListener(listener)
        }
        return this
    }

    fun create() {
        dialog = builder.create()
    }

    fun show() {
        dialog = builder.create()
        dialog?.show()
    }

    fun dismiss() {
        dialog?.dismiss()
    }
}