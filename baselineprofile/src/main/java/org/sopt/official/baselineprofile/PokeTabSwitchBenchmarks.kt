/*
 * MIT License
 * Copyright 2026 SOPT - Shout Our Passion Together
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
package org.sopt.official.baselineprofile

import androidx.benchmark.macro.FrameTimingMetric
import androidx.benchmark.macro.StartupMode
import androidx.benchmark.macro.junit4.MacrobenchmarkRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.uiautomator.By
import androidx.test.uiautomator.Until
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

/**
 * 메인 바텀탭 "홈 -> 콕찌르기" 전환의 프레임 타이밍을 잰다.
 */
@RunWith(AndroidJUnit4::class)
@LargeTest
class PokeTabSwitchBenchmarks {

    @get:Rule
    val rule = MacrobenchmarkRule()

    @Test
    fun switchToPokeTab() = rule.measureRepeated(
        packageName = InstrumentationRegistry.getArguments().getString("targetAppId")
            ?: throw Exception("targetAppId not passed as instrumentation runner arg"),
        metrics = listOf(FrameTimingMetric()),
        iterations = 10,
        startupMode = StartupMode.WARM,
        setupBlock = {
            startActivityAndWait()
            device.wait(Until.hasObject(By.desc("홈")), 5_000)
            device.findObject(By.desc("홈")).click()
            device.waitForIdle()
        },
    ) {
        device.wait(Until.hasObject(By.desc("콕찌르기")), 5_000)
        device.findObject(By.desc("콕찌르기")).click()
        device.waitForIdle()
    }
}
