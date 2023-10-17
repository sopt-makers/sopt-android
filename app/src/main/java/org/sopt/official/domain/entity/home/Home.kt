/*
 * MIT License
 * Copyright 2023 SOPT - Shout Our Passion Together
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
package org.sopt.official.domain.entity.home

import org.sopt.official.domain.entity.UserActiveState

data class SoptUser(
    val user: User = User(),
    val operation: SoptActiveRecord = SoptActiveRecord(),
    val isAllConfirm: Boolean = false
)

data class User(
    val activeState: UserActiveState = UserActiveState.UNAUTHENTICATED,
    val name: String? = null,
    val profileImage: String? = null,
    val generationList: SoptActiveGeneration? = null,
)

data class SoptActiveGeneration(
    private val value: List<Long>
) {
    fun isLongerOrEqual(comparator: Int): Boolean {
        return value.size >= comparator
    }

    val last get() = value.last()
    val isEmpty get() = value.isEmpty()
    val first get() = value.first()
    val length get() = value.size

    fun textAt(index: Int) = value[index].toString()
}

data class SoptActiveRecord(
    val attendanceScore: Double? = null,
    val announcement: String? = null,
)

data class HomeSection(
    val topDescription: String = "",
    val bottomDescription: String = ""
)
