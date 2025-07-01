/*
 * MIT License
 * Copyright 2023-2025 SOPT - Shout Our Passion Together
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
package org.sopt.official.stamp

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.material.use
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject
import kotlinx.coroutines.launch
import org.sopt.official.analytics.Tracker
import org.sopt.official.analytics.compose.ProvideTracker
import org.sopt.official.designsystem.SoptTheme
import org.sopt.official.domain.soptamp.error.Error

@AndroidEntryPoint
class SoptampActivity : AppCompatActivity() {
    @Inject
    lateinit var tracker: Tracker

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val navController = rememberNavController()
            val snackbarHostState = remember { SnackbarHostState() }
            val scope = rememberCoroutineScope()

            SoptTheme {
                ProvideTracker(tracker) {
                    Scaffold(
                        modifier = Modifier,
                        snackbarHost = { SnackbarHost(snackbarHostState) }
                    ) { paddingValues ->
                        SoptampNavHost(
                            navController = navController,
                            modifier = Modifier,
                            onShowErrorToast = { throwable ->
                                val message = when (throwable) {
                                    is Error.Unknown -> "알 수 없는 오류가 발생했습니다. 다시 시도해주세요."
                                    else -> throwable.message ?: "오류가 발생했습니다."
                                }
                                scope.launch {
                                    snackbarHostState.showSnackbar(
                                        message = message,
                                        withDismissAction = true
                                    )
                                }
                            }
                        )
                    }
                }
            }
        }
    }

    companion object {
        fun getIntent(context: Context): Intent {
            return Intent(context, SoptampActivity::class.java)
        }
    }
}
