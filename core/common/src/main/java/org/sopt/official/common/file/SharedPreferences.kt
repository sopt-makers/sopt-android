/*
 * MIT License
 * Copyright 2024 SOPT - Shout Our Passion Together
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
package org.sopt.official.common.file

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit
import org.sopt.official.common.BuildConfig
import org.sopt.official.common.util.toBase64
import org.sopt.official.common.util.toByteArray
import org.sopt.official.common.util.toEncryptedContent
import org.sopt.official.security.CryptoManager

fun createSharedPreference(fileName: String, context: Context): SharedPreferences =
    context.getSharedPreferences(fileName, Context.MODE_PRIVATE)

fun SharedPreferences.setSharedPreferenceData(cryptoManager: CryptoManager, keyAlias: String, key: String, value: String) {
    edit {
        putString(
            key,
            if (BuildConfig.DEBUG) value else cryptoManager.encrypt(
                keyAlias = keyAlias,
                bytes = value.toByteArray(Charsets.UTF_8)
            ).concatenate().toBase64()
        )
    }
}

fun SharedPreferences.getSharedPreferenceData(
    cryptoManager: CryptoManager,
    keyAlias: String,
    key: String,
    defaultValue: String = "",
    initializationVectorSize: Int = 12
): String = getString(key, null)?.let { sharedPreferenceData ->
        if (BuildConfig.DEBUG) sharedPreferenceData else cryptoManager.decrypt(
            keyAlias = keyAlias,
            encryptedContent = sharedPreferenceData.toByteArray().toEncryptedContent(initializationVectorSize = initializationVectorSize)
        ).toString(Charsets.UTF_8)
} ?: defaultValue