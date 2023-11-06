package org.sopt.official.common.navigator

import android.content.Intent

interface NavigatorProvides {
    fun getAuthActivityIntent(): Intent
}