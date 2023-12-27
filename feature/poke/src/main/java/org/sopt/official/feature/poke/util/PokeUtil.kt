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