/*
 * MIT License
 * Copyright 2023 SOPT - Shout Our Passion Together
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
package org.sopt.official.feature.poke.util

import android.app.Activity
import android.graphics.drawable.GradientDrawable
import android.view.Gravity
import android.view.LayoutInflater
import android.widget.ImageView
import android.widget.Toast
import androidx.core.content.ContextCompat
import org.sopt.official.common.util.dp
import org.sopt.official.domain.poke.type.PokeFriendType
import org.sopt.official.feature.poke.R
import org.sopt.official.feature.poke.databinding.ToastPokeAlertBinding

fun ImageView.setRelationStrokeColor(relationName: String) {
    (background as GradientDrawable).setStroke(
        2.dp,
        ContextCompat.getColor(
            context,
            when (relationName) {
                PokeFriendType.NEW.readableName -> org.sopt.official.designsystem.R.color.mds_blue_900
                PokeFriendType.BEST_FRIEND.readableName -> org.sopt.official.designsystem.R.color.mds_information
                PokeFriendType.SOULMATE.readableName -> org.sopt.official.designsystem.R.color.mds_secondary
                else -> org.sopt.official.designsystem.R.color.transparent
            }
        )
    )
}

private var toast: Toast? = null
fun Activity.showPokeToast(message: String) {
    val binding = ToastPokeAlertBinding.inflate(LayoutInflater.from(this), null, false)
    val toastMessage = when (message.isEmpty()) {
        true -> getString(R.string.toast_poke_error)
        false -> message
    }

    toast?.cancel()
    toast = Toast.makeText(this, toastMessage, Toast.LENGTH_SHORT)
    binding.textViewAlertMessage.text = toastMessage

    toast?.let {
        it.view = binding.root
        it.setGravity(Gravity.TOP, 0, 0)
        it.show()
    }
}
