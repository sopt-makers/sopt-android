/*
 * Copyright 2023 SOPT - Shout Our Passion Together
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.sopt.official

import android.content.Context
import android.content.Intent
import androidx.test.core.app.ApplicationProvider
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.uiautomator.By
import androidx.test.uiautomator.UiDevice
import androidx.test.uiautomator.Until
import com.google.common.truth.Truth.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

private const val LAUNCH_TIMEOUT = 5000L
private const val SOPT_APP = "org.sopt.official"

class AppStartupTest {
    private lateinit var device: UiDevice

    @BeforeEach
    fun setInitScreen() {
        // UiDevice 초기화
        device = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation())

        // Start from the home screen
        device.pressHome()

        // Wait for launcher
        val launcherPackage: String = device.launcherPackageName
        assertThat(launcherPackage).isNotNull()
        device.wait(
            Until.hasObject(By.pkg(launcherPackage).depth(0)),
            LAUNCH_TIMEOUT
        )

        // Launch the app
        val context = ApplicationProvider.getApplicationContext<Context>()
        val intent = context.packageManager
            .getLaunchIntentForPackage(SOPT_APP)
            ?.apply {
                // Clear out any previous instances
                addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
            }
        context.startActivity(intent)

        // Wait for the app
        device.wait(
            Until.hasObject(By.pkg(SOPT_APP).depth(0)),
            LAUNCH_TIMEOUT
        )
    }

    @Test
    @DisplayName("비회원인_경우_비회원_버튼을_클릭하여_홈화면으로_간다")
    fun notMemberOfSopt_press_passing_button_should_go_main_screen() {
        // 비회원 버튼 클릭
        val button = device.findObject(
            By.text("SOPT 회원이 아니에요").clazz("android.widget.TextView")
        )

        device.performActionAndWait(
            { button.click() },
            Until.newWindow(),
            LAUNCH_TIMEOUT
        )
    }
}
