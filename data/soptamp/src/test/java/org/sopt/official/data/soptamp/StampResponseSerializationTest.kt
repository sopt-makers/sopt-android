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
package org.sopt.official.data.soptamp

import kotlinx.serialization.json.Json
import org.junit.Test
import org.junit.jupiter.api.assertDoesNotThrow
import org.sopt.official.data.soptamp.remote.model.response.StampResponse

class StampResponseSerializationTest {
    @Test
    fun `missionId가 비어있을 시 역직렬화가 실패해야 한다`() {
        // given
        val json = """
            {
                "createdAt": "2023-01-01T00:00:00",
                "updatedAt": "2023-01-01T00:00:00",
                "id": 1,
                "contents": "contents",
                "images": ["image1", "image2"]
            }
        """.trimIndent()
        val jsonDecoder = Json {
            ignoreUnknownKeys = true
            isLenient = true
        }

        // expected
        assertDoesNotThrow {
            jsonDecoder.decodeFromString<StampResponse>(json)
        }
    }
}
