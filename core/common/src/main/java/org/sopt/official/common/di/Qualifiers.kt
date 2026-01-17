/*
 * MIT License
 * Copyright 2022-2025 SOPT - Shout Our Passion Together
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
package org.sopt.official.common.di

import javax.inject.Qualifier

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class Logging

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class Auth

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class AppRetrofit(val authNeeded: Boolean = true)

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class OperationRetrofit

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class LocalStore

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class S3

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class AuthRetrofit

data class LocalStoreName(val value: String)
data class AppRetrofitInstance(val retrofit: retrofit2.Retrofit)
data class AuthRetrofitInstance(val retrofit: retrofit2.Retrofit)
data class OperationRetrofitInstance(val retrofit: retrofit2.Retrofit)
data class NoneAuthRetrofitInstance(val retrofit: retrofit2.Retrofit)
data class SoptampStoreKey(val value: String)
data class PokeStoreKey(val value: String)

data class AuthOkHttpClient(val client: okhttp3.OkHttpClient)
data class NonAuthOkHttpClient(val client: okhttp3.OkHttpClient)
data class LoggingInterceptor(val interceptor: okhttp3.Interceptor)
data class AuthInterceptorWrapper(val interceptor: okhttp3.Interceptor)
