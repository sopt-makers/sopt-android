package org.sopt.official.feature.auth

import android.content.Context
import android.content.Intent
import dagger.hilt.android.qualifiers.ApplicationContext
import org.sopt.official.network.authenticator.ProvidesIntents
import javax.inject.Inject

class ProvidesIntent @Inject constructor(
    @ApplicationContext private val context: Context
) : ProvidesIntents {
    override fun getAuthActivityIntent(): Intent =
        AuthActivity.newInstance(context)

}