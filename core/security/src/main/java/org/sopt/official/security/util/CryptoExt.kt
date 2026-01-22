/*
 * MIT License
 * Copyright 2024-2025 SOPT - Shout Our Passion Together
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
package org.sopt.official.security.util

import android.util.Base64
import org.sopt.official.security.CryptoManager
import org.sopt.official.security.model.EncryptedContent

fun ByteArray.toEncryptedContent(initializationVectorSize: Int): EncryptedContent {
    return if (this.size > initializationVectorSize) {
        EncryptedContent(
            initializationVector = this.copyOfRange(0, initializationVectorSize),
            data = this.copyOfRange(initializationVectorSize, this.size)
        )
    } else {
        EncryptedContent(
            initializationVector = byteArrayOf(),
            data = byteArrayOf()
        )
    }
}

fun ByteArray.toBase64(): String = Base64.encodeToString(this, Base64.DEFAULT)

fun String.toByteArray(): ByteArray = Base64.decode(this, Base64.DEFAULT)

fun String.getEncryptedDataOrDefault(keyAlias: String) = CryptoManager.encrypt(
    keyAlias = keyAlias,
    bytes = this.toByteArray(Charsets.UTF_8)
).mapCatching { encryptedContent ->
    encryptedContent.concatenate().toBase64()
}.getOrDefault(this)

fun String.getDecryptedDataOrDefault(keyAlias: String, initializationVectorSize: Int = 12) = CryptoManager.decrypt(
    keyAlias = keyAlias,
    encryptedContent = this.toByteArray().toEncryptedContent(initializationVectorSize = initializationVectorSize)
).mapCatching { decryptedContent ->
    decryptedContent.toString(Charsets.UTF_8)
}.getOrDefault(this)
