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
package org.sopt.official.feature.main

import org.junit.Assert.assertEquals
import org.junit.Test

class MainTabTest {

    @Test
    fun `Home과 MyPage는 activeServices와 무관하게 항상 포함되고 양 끝에 위치한다`() {
        val tabs = MainTab.getActiveTabs(emptyList())

        assertEquals(MainTab.Home, tabs.first())
        assertEquals(MainTab.MyPage, tabs.last())
    }

    @Test
    fun `activeServices에 있는 deeplink의 탭만 중간에 추가되고, 순서는 입력이 아닌 MainTab 선언 순서를 따른다`() {
        val tabs = MainTab.getActiveTabs(listOf("poke", "soptamp"))

        assertEquals(listOf(MainTab.Home, MainTab.Soptamp, MainTab.Poke, MainTab.MyPage), tabs)
    }

    @Test
    fun `activeServices에 없는 deeplink는 제외된다`() {
        val tabs = MainTab.getActiveTabs(listOf("poke"))

        assertEquals(listOf(MainTab.Home, MainTab.Poke, MainTab.MyPage), tabs)
    }

    @Test
    fun `알 수 없는 deeplink는 무시된다`() {
        val tabs = MainTab.getActiveTabs(listOf("poke", "unknown-service"))

        assertEquals(listOf(MainTab.Home, MainTab.Poke, MainTab.MyPage), tabs)
    }
}
