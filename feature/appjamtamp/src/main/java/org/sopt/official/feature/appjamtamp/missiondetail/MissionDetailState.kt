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
package org.sopt.official.feature.appjamtamp.missiondetail

import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import org.sopt.official.feature.appjamtamp.missiondetail.model.DetailViewType
import org.sopt.official.feature.appjamtamp.missiondetail.model.StampClapUserModel
import org.sopt.official.feature.appjamtamp.model.ImageModel
import org.sopt.official.feature.appjamtamp.model.Mission
import org.sopt.official.feature.appjamtamp.model.User

data class MissionDetailState(
    val isLoading: Boolean = true,
    val isFailed: Boolean = false,

    val viewType: DetailViewType = DetailViewType.READ_ONLY,
    val mission: Mission = Mission.DEFAULT,
    val imageModel: ImageModel = ImageModel.Empty,
    val date: String = "",
    val content: String = "",

    val stampId: Int = -1,
    val writer: User = User(),
    val teamName: String = "",
    val clapCount: Int = 0,
    val myClapCount: Int = 0,
    val unSyncedClapCount: Int = 0,
    val viewCount: Int = 0,

    val clappers: ImmutableList<StampClapUserModel> = persistentListOf(),
    val showPostSubmissionBadge: Boolean = false
)
