package org.sopt.official

import android.content.Context
import android.content.Intent
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.uiautomator.By
import androidx.test.uiautomator.UiDevice
import androidx.test.uiautomator.Until
import org.hamcrest.CoreMatchers.notNullValue
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

private const val LAUNCH_TIMEOUT = 5000L
private const val SOPT_APP = "org.sopt.official"

@RunWith(AndroidJUnit4::class)
class AppStartupTest {
    private lateinit var device: UiDevice

    @Before
    fun setInitScreen() {
        // UiDevice 초기화
        device = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation())

        // Start from the home screen
        device.pressHome()

        // Wait for launcher
        val launcherPackage: String = device.launcherPackageName
        assertThat(launcherPackage, notNullValue())
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
    fun 비회원인_경우_비회원_버튼을_클릭하여_홈화면으로_간다() {
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
