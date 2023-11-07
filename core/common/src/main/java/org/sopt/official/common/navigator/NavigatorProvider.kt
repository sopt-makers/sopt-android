package org.sopt.official.common.navigator

import android.content.Intent

interface NavigatorProvider {
    fun getAuthActivityIntent(): Intent
}