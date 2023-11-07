package org.sopt.official.feature.navigator

import android.content.Context
import android.content.Intent
import dagger.hilt.android.qualifiers.ApplicationContext
import org.sopt.official.common.navigator.NavigatorProvider
import org.sopt.official.feature.auth.AuthActivity
import javax.inject.Inject

class NavigatorProviderIntent @Inject constructor(
    @ApplicationContext private val context: Context
) : NavigatorProvider {
    override fun getAuthActivityIntent(): Intent =
        AuthActivity.newInstance(context)
}