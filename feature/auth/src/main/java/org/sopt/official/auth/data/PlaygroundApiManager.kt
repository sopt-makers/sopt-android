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
package org.sopt.official.auth.data
import dev.zacsweers.metro.Inject
import okhttp3.OkHttpClient
import org.sopt.official.auth.Constants
import org.sopt.official.auth.PlaygroundInfo
import org.sopt.official.auth.data.remote.AuthService
import org.sopt.official.auth.data.remote.ServiceFactory
import retrofit2.Retrofit

@Inject
class PlaygroundApiManager private constructor(
    private val retrofit: Retrofit,
) {
    fun provideAuthService(): AuthService = retrofit.create(AuthService::class.java)

    companion object {
        @Volatile
        private var instance: PlaygroundApiManager? = null

        fun getInstance(isDebug: Boolean) = instance ?: synchronized(this) {
            instance ?: PlaygroundApiManager(createRetrofit(isDebug)).also {
                instance = it
            }
        }

        private fun createRetrofit(isDebug: Boolean) = ServiceFactory.withClient(
            url = "${Constants.SCHEME}://${if (isDebug) PlaygroundInfo.DEBUG_AUTH_HOST else PlaygroundInfo.RELEASE_AUTH_HOST}",
            client = OkHttpClient.Builder()
                .addInterceptor(ServiceFactory.loggingInterceptor)
                .build()
        )
    }
}
