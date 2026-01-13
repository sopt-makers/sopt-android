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
package org.sopt.official.data.appjamtamp.dto.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class AppjamtampStampResponseDto(
    @SerialName("id")
    val id: Int,
    @SerialName("contents")
    val contents: String,
    @SerialName("images")
    val images: List<String>,
    @SerialName("activityDate")
    val activityDate: String,
    @SerialName("createdAt")
    val createdAt: String,
    @SerialName("updatedAt")
    val updatedAt: String,
    @SerialName("missionId")
    val missionId: Int,
    @SerialName("missionTitle")
    val missionTitle: String,
    @SerialName("missionLevel")
    val missionLevel: Int,
    @SerialName("teamNumber")
    val teamNumber: String,
    @SerialName("teamName")
    val teamName: String,
    @SerialName("ownerNickname")
    val ownerNickname: String,
    @SerialName("ownerProfileImage")
    val ownerProfileImage: String?,
    @SerialName("clapCount")
    val clapCount: Int,
    @SerialName("viewCount")
    val viewCount: Int,
    @SerialName("myClapCount")
    val myClapCount: Int,
    @SerialName("isMine")
    val isMine: Boolean
)
