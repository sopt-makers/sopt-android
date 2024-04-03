/*
 * MIT License
 * Copyright 2023-2024 SOPT - Shout Our Passion Together
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
package org.sopt.official.data.soptamp.remote.api

import okhttp3.RequestBody
import org.sopt.official.data.soptamp.remote.model.request.StampRequest
import org.sopt.official.data.soptamp.remote.model.response.ModifyStampResponse
import org.sopt.official.data.soptamp.remote.model.response.S3URLResponse
import org.sopt.official.data.soptamp.remote.model.response.StampResponse
import retrofit2.Response
import retrofit2.http.Body


object FakeStampService : StampService {
    private val fakeStampResponse = StampResponse(
        activityDate = "",
        id = -1,
        contents = ""
    )

    private val fakeModifyStampResponse = ModifyStampResponse(
        stampId = -1
    )

    private val fakeS3URLResponse = S3URLResponse(
        preSignedURL = "",
        imageURL = ""
    )

    private val fakeResponse: Response<Unit> = Response.success(Unit)

    override suspend fun retrieveStamp(missionId: Int, nickname: String): StampResponse = fakeStampResponse

    override suspend fun modifyStamp(@Body body: StampRequest): ModifyStampResponse = fakeModifyStampResponse

    override suspend fun registerStamp(body: StampRequest): StampResponse = fakeStampResponse

    override suspend fun deleteStamp(missionId: Int) {}

    override suspend fun deleteAllStamps() {}

    override suspend fun getS3URL(): S3URLResponse = fakeS3URLResponse

    override suspend fun putS3Image(preSignedURL: String, image: RequestBody): Response<Unit> {
        return fakeResponse
    }
}
